package com.fpoly.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.fpoly.model.Product_Detail;

@Repository
public interface Product_DetailRepository extends JpaRepository<Product_Detail, Integer>, JpaSpecificationExecutor<Product_Detail> {

	@Query(value = "select * from Product_Detail where id=:idp",nativeQuery = true)
	Product_Detail findByIdp(@Param("idp")Integer id);
	
	@Query(value = "select * from Product_Detail where title=:name",nativeQuery = true)
	Product_Detail findByName(@Param("name")String name);
	
	@Query(value = "select * from Product_Detail where sku=:name",nativeQuery = true)
	Product_Detail findByTitle(@Param("name") String name);

	Page<Product_Detail> findByProductStatusTrue(Specification<Product_Detail> filterBy, Pageable pageable);
	
}
