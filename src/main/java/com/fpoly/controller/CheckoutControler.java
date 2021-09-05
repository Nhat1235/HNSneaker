package com.fpoly.controller;

import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.model.Address;
import com.fpoly.model.Bill;
import com.fpoly.model.Cart;
import com.fpoly.model.ShoppingCart;
import com.fpoly.model.User;
import com.fpoly.repositories.AddressRepository;
import com.fpoly.repositories.UserRepository;
import com.fpoly.service.OrderService;
import com.fpoly.service.ShoppingCartService;
import com.fpoly.service.UserService;


@Controller
public class CheckoutControler {
	
	@Autowired 
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressRepository Adrep;
	
	@Autowired
	private UserRepository UserRepo;
	
	@RequestMapping("/checkout")
	public String checkout( @RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField, Model model, Authentication authentication, Principal principal, RedirectAttributes attr) {		
		User user = (User) authentication.getPrincipal();	
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);
		if(shoppingCart.isEmpty()) {
			attr.addFlashAttribute("alert", true);
			model.addAttribute("emptyCart", true);
			return "redirect:/shopping-cart/cart";
		}
		//Mã vận đơn
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();

	    System.out.println(generatedString);
		model.addAttribute("shipmentCode", generatedString.toUpperCase());
		
		//Thông tin, địa chỉ giao hàng
		User userAddress = userService.findByUsername(principal.getName());
		System.out.println(userAddress);
		model.addAttribute("user", userAddress);
		
		model.addAttribute("cartItemList", shoppingCart.getCartItems());
		model.addAttribute("shoppingCart", shoppingCart);
		if(missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}		
		return "Cart/checkout";		
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String placeOrder(@ModelAttribute("address") Address address, RedirectAttributes redirectAttributes, Authentication authentication,Model model) {		
		User user = (User) authentication.getPrincipal();		
		System.out.println(address.getCity() + address.getReciverPhoneNumber());
		
		if(user.getAddress()==null) {
			Address ad = new Address();
			ad.setCity(address.getCity());
			ad.setReciverPhoneNumber(address.getReciverPhoneNumber());
			ad.setReciverName(address.getReciverName());
			ad.setStreetAddress(address.getStreetAddress());
			
			if(ad!=null) {
				 Adrep.save(ad);
				 user.setAddress(ad);
				 UserRepo.save(user);
			}
			
		}
		
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);	
		if (!shoppingCart.isEmpty()) {
			Cart order = orderService.createOrder(shoppingCart, user);	
//			Bill order2 = orderService.createOrder(shoppingCart, user);
			redirectAttributes.addFlashAttribute("order", order);
		}else {
			redirectAttributes.addFlashAttribute("missingRequiredField", true);
		}
		return "redirect:/order-submitted";
	}
	
	@RequestMapping(value = "/order-submitted", method = RequestMethod.GET)
	public String orderSubmitted(Model model) {
		Cart order = (Cart) model.asMap().get("order");
		if (order == null) {
			return "redirect:/store";
		}
		model.addAttribute("order", order);
		return "Cart/orderSubmitted";	
	}

}
