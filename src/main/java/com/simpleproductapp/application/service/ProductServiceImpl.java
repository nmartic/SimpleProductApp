package com.simpleproductapp.application.service;

import com.simpleproductapp.application.dto.ProductDto;
import com.simpleproductapp.application.entity.Product;
import com.simpleproductapp.application.exceptions.ProductException;
import com.simpleproductapp.application.parameterValidation.ParameterValidationService;
import com.simpleproductapp.application.repository.ExchangeRateRepository;
import com.simpleproductapp.application.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ParameterValidationService parameterValidationService;
    private final ProductRepository productRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public ProductServiceImpl(ParameterValidationService parameterValidationService, ProductRepository productRepository, ExchangeRateRepository exchangeRateRepository) {
        this.parameterValidationService = parameterValidationService;
        this.productRepository = productRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public Object getProducts(Integer pageSize, Integer pageNumber, String search, Integer sortBy, Integer sortDirection) {

        String checkColumn = parameterValidationService.checkSortByForProduct(sortBy);
        Sort.Direction checkSortDirection = parameterValidationService.checkSortingDirection(sortDirection);
        String checkSearch = parameterValidationService.checkSearch(search);
        Pageable sort = PageRequest.of(pageNumber, pageSize, Sort.by(checkSortDirection, checkColumn));

        return productRepository.findProducts(checkSearch, sort);
    }

    @Transactional
    @Override
    public void addProducts(List<ProductDto> productDtoList) {
        List<Product> newProductsList = new ArrayList<>();

        for (int i=0;i<productDtoList.size();i++) {
            Product product = new Product();

//            Product Code
            if (productDtoList.get(i).getCode() != null) {
                if (productDtoList.get(i).getCode().length() == 10) {
                    if (productRepository.checkIfCodeExists(productDtoList.get(i).getCode())) {
                        throw new ProductException("'code' already exists in database");
                    }
                    product.setCode(productDtoList.get(i).getCode());
                } else {
                    throw new ProductException("'code' length is not 10");
                }
            } else {
                throw new ProductException("'code' value is null");
            }

//            Product Name
            if (productDtoList.get(i).getName() != null) {
                product.setName(productDtoList.get(i).getName());
            } else {
                throw new ProductException("'name' value is null");
            }

//            Product Price
            try {
                if (productDtoList.get(i).getPriceHrk() != null) {
                    if (productDtoList.get(i).getPriceHrk().compareTo(BigDecimal.ZERO) < 0) {
                        throw new ProductException("'price_hrk' has negative value");
                    }
                    product.setPriceHrk(productDtoList.get(i).getPriceHrk());
                    BigDecimal exchangeRate = BigDecimal.valueOf(Double.parseDouble(exchangeRateRepository.getLastExchangeRate().replace(",",".")));
                    BigDecimal priceInEur = productDtoList.get(i).getPriceHrk().divide(exchangeRate, 2, RoundingMode.HALF_UP);
                    product.setPriceEur(priceInEur);
                } else {
                    throw new ProductException("'price_hrk' value is null");
                }
            } catch (Exception e) {
                throw new ProductException("Problem with 'price_hrk'");
            }

//            Product Description
            if (productDtoList.get(i).getDescription() != null) {
                product.setDescription(productDtoList.get(i).getDescription());
            } else {
                throw new ProductException("'description' value is null");
            }

//            Product isAvailable
            try {
                if (productDtoList.get(i).isAvailable()) {
                    product.setAvailable(true);
                } else {
                    product.setAvailable(false);
                }
            } catch (Exception e) {
                throw new ProductException("Problem with 'available'");
            }
            product.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            product.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            newProductsList.add(product);
        }
        productRepository.saveAll(newProductsList);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        if(productDto.getCode()==null){
            throw new ProductException("'code' is null");
        }
        Product product = productRepository.findByCode(productDto.getCode());
        if (product==null){
            throw new ProductException("Product with code " + productDto.getCode() + " does not exist");
        }

//        Product Name
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }

//        Product Price
        try {
            if (productDto.getPriceHrk() != null) {
                if (productDto.getPriceHrk().compareTo(BigDecimal.ZERO) < 0) {
                    throw new ProductException("'price_hrk' has negative value");
                }
                product.setPriceHrk(productDto.getPriceHrk());
                BigDecimal exchangeRate = BigDecimal.valueOf(Double.parseDouble(exchangeRateRepository.getLastExchangeRate().replace(",",".")));
                BigDecimal priceInEur = productDto.getPriceHrk().divide(exchangeRate, 2, RoundingMode.HALF_UP);
                product.setPriceEur(priceInEur);
            }
        }catch (Exception e){
            throw new ProductException("Problem with 'price_hrk'");
        }

//        Product Description
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }

//        Product isAvailable
        try{
            if (productDto.isAvailable()) {
                product.setAvailable(true);
            } else {
                product.setAvailable(false);
            }
        }catch (Exception e){
            throw new ProductException("Problem with 'available'");
        }
        product.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(String code) {
        if(code==null){
            throw new ProductException("'code' is null");
        }
        Product product = productRepository.findByCode(code);
        if (product==null){
            throw new ProductException("Product with code " + code + " does not exist");
        }
        else{
            product.setDeleted(Timestamp.valueOf(LocalDateTime.now()));
            productRepository.save(product);
        }
    }
}
