package com.fpoly.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fpoly.model.Bill;
import com.fpoly.model.Cart;
import com.fpoly.model.ShoppingCart;
import com.fpoly.model.User;

@Service
public interface OrderService {

	Cart createOrder(ShoppingCart shoppingCart, User user);
	
	List<Cart> findByUser(User user);
	
	Cart findOrderWithDetails(Integer id);
	
//	Bill createOrderBill(ShoppingCart shoppingCart, User user);
//	
//	List<Bill> findByUserBill(User user);
//	
//	Bill findOrderWithDetailsBill(Integer id);
}
