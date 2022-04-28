package com.simpleproductapp.application.service;

import com.simpleproductapp.SimpleProductAppApplication;
import com.simpleproductapp.application.constants.AppConstants;
import com.simpleproductapp.application.entity.MyUser;
import com.simpleproductapp.application.repository.MyUserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService{

    private final MyUserRepository myUserRepository;

    public PdfServiceImpl(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    ApplicationHome applicationHome = new ApplicationHome(SimpleProductAppApplication.class);

    private final String savePath = "src/main/resources/";
//    private final String savePath = applicationHome.getDir().toString()+File.separator;


    @Override
    public ResponseEntity<byte[]> viewMyUserList() throws JRException {
        List<MyUser> lista = myUserRepository.findAll();
        lista.add(0, null);                               // Dodano zbog problema s Jasperom (https://community.jaspersoft.com/questions/538339/first-row-missing-after-following-tip-faq-subreport-totally-empty)
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

        HashMap<String, Object> map = new HashMap<>();
        map.put("dummy", "beanCollectionDataSource");
        map.put("CollectionBeanParam", beanCollectionDataSource);

        JasperReport compileReport;
        try{
            String listPaths =  applicationHome.getDir().toString()+ File.separator+"pdfTemplates"+ File.separator+"myUsersList.jrxml";
            compileReport = JasperCompileManager.compileReport(new FileInputStream(listPaths));
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Putanja do jrxml-a nije dobra", e);

        }

        JasperPrint report = JasperFillManager.fillReport(compileReport, map, beanCollectionDataSource);
        byte[] pdf= JasperExportManager.exportReportToPdf(report);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+"myUsersList"+".pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @Override
    public void createUsersPdf() {
        List<MyUser> lista = myUserRepository.findAll();
        lista.add(0, null);                               // Dodano zbog problema s Jasperom (https://community.jaspersoft.com/questions/538339/first-row-missing-after-following-tip-faq-subreport-totally-empty)
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

        HashMap<String, Object> map = new HashMap<>();
        map.put("CollectionBeanParam", beanCollectionDataSource);

        JasperReport compileReport;
        try{
            String listPaths =  applicationHome.getDir().toString()+ File.separator+"pdfTemplates"+ File.separator+"myUsersList.jrxml";
            compileReport = JasperCompileManager.compileReport(new FileInputStream(listPaths));
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Putanja do jrxml-a nije dobra", e);

        }

        try{
            JasperPrint report = JasperFillManager.fillReport(compileReport, map, beanCollectionDataSource);

            JasperExportManager.exportReportToPdfFile(report, savePath+"myUsersList"+".pdf");
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Export liste u pdf nije uspio", e);
        }

    }
}
