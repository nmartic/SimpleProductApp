package com.simpleproductapp.application.repository;

import com.simpleproductapp.application.dto.ReturnProductDto;
import com.simpleproductapp.application.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query(value = "select case when exists (select true from product p where p.code= :code) then 'true' else 'false' end", nativeQuery = true)
    boolean checkIfCodeExists(@RequestParam("code") String code);

    @Query(value = "select * from product p where p.code = :code", nativeQuery = true)
    Product findByCode(@RequestParam("code") String code);

    @Query(value = "select p.code as code, p.name as name, p.price_hrk as priceHrk, p.description as description, p.is_available as available from product p " +
            "where (lower(p.code) like :search or lower(p.name) like :search or cast(p.price_hrk as varchar) like :search or lower(p.description) like :search) and p.deleted is null", nativeQuery = true)
    List<ReturnProductDto> findProducts(@RequestParam("search") String search, Pageable sort);
}
