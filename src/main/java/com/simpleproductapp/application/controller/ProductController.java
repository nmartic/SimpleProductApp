package com.simpleproductapp.application.controller;

import com.simpleproductapp.application.dto.ProductDto;
import com.simpleproductapp.application.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    public ResponseEntity<?> getProducts(@RequestParam (defaultValue = "10", required = false) Integer pageSize,
                                                @RequestParam (defaultValue = "0", required = false) Integer pageNumber,
                                                @RequestParam (defaultValue = "", required = false) String search,
                                                @RequestParam(defaultValue = "0", required = false) Integer sortBy,
                                                @RequestParam(defaultValue = "0", required = false) Integer sortDirection,
                                                HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/product\", method: GET, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        return new ResponseEntity<>(productService.getProducts(pageSize, pageNumber, search, sortBy, sortDirection), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    public ResponseEntity<?> addProducts(@RequestBody List<ProductDto> productDtoList,
                            HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/product\", method: POST, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        productService.addProducts(productDtoList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto,
                              HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/product\", method: PUT, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        productService.updateProduct(productDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    public ResponseEntity<?> deleteProduct(@RequestParam String code,
                              HttpServletRequest request){
        log.debug("User: "+ request.getRemoteUser()+", endpoint: \"/product\", method: DELET, time: "+ LocalDateTime.now()+", IP adress: "+request.getRemoteAddr()+", Browser info: "+ request.getHeader("User-Agent"));
        productService.deleteProduct(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
