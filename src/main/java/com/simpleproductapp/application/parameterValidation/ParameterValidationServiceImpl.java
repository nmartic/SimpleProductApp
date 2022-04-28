package com.simpleproductapp.application.parameterValidation;

import com.simpleproductapp.application.exceptions.RequestParameterException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ParameterValidationServiceImpl implements ParameterValidationService {

    public Sort.Direction checkSortingDirection(Integer sortDirection){
        if (sortDirection != null && sortDirection == 0) {
            return Sort.Direction.ASC;
        }
        else if (sortDirection != null && sortDirection == 1) {
            return Sort.Direction.DESC;
        }
        else{
            throw new RequestParameterException("Sort Direction nije u redu");
        }
    }

    @Override
    public String checkSearch(String search) {
        if(search==null || search.equals("")){
            return "%%";
        }
        else{
            return "%"+search.toLowerCase()+"%";
        }
    }

    @Override
    public String checkSortByForProduct(Integer sortBy) {
        switch (sortBy) {
            case 0:
                return "code";
            case 1:
                return "description";
            case 2:
                return "name";
            case 3:
                return "is_available";
            case 4:
                return "price_eur";
            case 5:
                return "price_hrk";
            default:
                return "";
        }
    }

    @Override
    public String checkSortByForUser(Integer sortBy) {
        switch (sortBy) {
            case 0:
                return "first_name";
            case 1:
                return "last_name";
            case 2:
                return "username";
            case 3:
                return "uuid";
            case 4:
                return "active";
            default:
                return "";
        }
    }
}
