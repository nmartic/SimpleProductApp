package com.simpleproductapp.application.repository;

import com.simpleproductapp.application.entity.Product;
import org.apache.logging.log4j.message.TimestampMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void checkIfProductCodeExists() {
//        given
        String code = "codecodeco";
        Product product = new Product();
        product.setCode(code);
        product.setName("Product1");
        product.setPriceHrk(BigDecimal.valueOf(10));
        product.setPriceEur(BigDecimal.valueOf(1.54));
        product.setAvailable(true);
        product.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        product.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        underTest.save(product);

//        when
        boolean expected = underTest.checkIfCodeExists(code);

//        then
        assertThat(expected).isTrue();
    }

    @Test
    void checkIfProductCodeDoesNotExist() {
//        given
        String code = "codecodeco";

//        when
        boolean expected = underTest.checkIfCodeExists(code);

//        then
        assertThat(expected).isFalse();
    }
}