package com.simpleproductapp.application.parameterValidation;

import org.springframework.data.domain.Sort;

// Provjera parametara koji se salju u query za dohvat podataka iz baze
public interface ParameterValidationService {

    Sort.Direction checkSortingDirection(Integer sortDirection);

    String checkSearch(String search);

    String checkSortByForProduct(Integer sortBy);

    String checkSortByForUser(Integer sortBy);
}
