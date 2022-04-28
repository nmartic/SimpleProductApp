package com.simpleproductapp.application.service;

import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

public interface PdfService {

    ResponseEntity<byte[]> viewMyUserList() throws JRException;

    void createUsersPdf();
}
