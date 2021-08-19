package com.fpoly.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpoly.model.Cart;
import com.fpoly.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findByUser(User user);
	
	@Query(value = "select * from Cart where orderStatus=?1 and orderDate>=?2 and orderDate<=?3 order by orderDate desc",nativeQuery = true)
	List<Cart> findByDate(String status, Date orderDate1, Date orderDate2);
	
	@Query(value = "select * from Cart where orderStatus=?1 order by orderDate desc",nativeQuery = true)
	List<Cart> findByDate(String status);
	
	@Query(value = "select * from Cart order by orderDate desc",nativeQuery = true)
	List<Cart> findByDate2();
	
	@Query(value = "select distinct orderStatus from Cart order by orderStatus asc",nativeQuery = true)
	List<String> findStatus();
}
