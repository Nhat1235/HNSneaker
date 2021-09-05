package com.fpoly.repositories;

import java.math.BigDecimal;
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
	
	@Query(value="select sum(orderTotal) from cart where orderDate = curdate()", nativeQuery = true)
	BigDecimal today();
	
	@Query(value="select count(*) from cart where orderDate = curdate()", nativeQuery = true)
	BigDecimal totalOrderToday();
	
	@Query(value="select sum(orderTotal) from cart where orderDate > now() - interval 1 week", nativeQuery = true)
	BigDecimal week();
	
	@Query(value="select count(*) from cart where orderDate > now() - interval 1 week", nativeQuery = true)
	BigDecimal totalOrderWeek();
	
	@Query(value="select sum(orderTotal) from cart where MONTH(orderDate)=MONTH(now())", nativeQuery = true)
	BigDecimal month();
	
	@Query(value="select count(*) from cart where MONTH(orderDate)=MONTH(now())", nativeQuery = true)
	BigDecimal totalOrderMonth();
	
	@Query(value="SELECT sum(orderTotal) FROM cart WHERE YEAR(orderDate) ='2021'", nativeQuery = true)
	BigDecimal year();
	
	@Query(value="select sum(orderTotal) from cart where orderDate between ?1 and ?2", nativeQuery = true)
	BigDecimal byDate(String date1, String date2);
	
	@Query(value="select count(*) from cart where orderDate between ?1 and ?2", nativeQuery = true)
	BigDecimal totalByDate(String date1, String date2);
	
}
