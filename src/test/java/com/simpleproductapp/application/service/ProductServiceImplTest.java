package com.simpleproductapp.application.service;

import com.simpleproductapp.application.parameterValidation.ParameterValidationService;
import com.simpleproductapp.application.repository.ExchangeRateRepository;
import com.simpleproductapp.application.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ProductServiceImplTest {

    @Mock
    private ParameterValidationService parameterValidationService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    private AutoCloseable autoCloseable;
    private ProductServiceImpl underTest;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ProductServiceImpl(parameterValidationService, productRepository, exchangeRateRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Disabled
    void canGetProducts() {
//        when
        Integer pageSize = 10;
        Integer pageNumber = 1;
        String search = "";
        Integer sortBy = 0;
        Integer SortDirection = 0;
        underTest.getProducts(pageSize, pageNumber, search, sortBy, SortDirection);

//        then
//        verify(parameterValidationService).checkSearch(search);
//        verify(parameterValidationService).checkSortingDirection(SortDirection);
//        verify(parameterValidationService).checkSortByForProduct(sortBy);
    }
}