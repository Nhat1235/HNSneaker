package com.fpoly.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.model.Cart;
import com.fpoly.model.Product;
import com.fpoly.model.Product_Detail;
import com.fpoly.model.Product_Img;
import com.fpoly.repositories.BrandRepository;
import com.fpoly.repositories.CartRepository;
import com.fpoly.repositories.Cart_ItemRepository;
import com.fpoly.repositories.CategoryRepository;
import com.fpoly.repositories.ColorRepository;
import com.fpoly.repositories.ProductRepository;
import com.fpoly.repositories.Product_DetailRepository;
import com.fpoly.repositories.Product_ImgRepository;
import com.fpoly.repositories.SizeRepository;
import com.fpoly.service.Product_DetailService;
import com.fpoly.service.ShoppingCartService;

@Controller
@RequestMapping(value = "/admin/chart")
public class ChartController {
	
	@Autowired
	CartRepository cartRepos;
	
	@Autowired
	Product_DetailService product_DetailService;

	@Autowired
	ProductRepository productRepos;

	@Autowired
	Product_ImgRepository prodimgRepo;

	@Autowired
	Product_DetailRepository product_DetailRepository;
	
	@Autowired
	SizeRepository sizeRepo;
	
	@Autowired
	Cart_ItemRepository cirepos;
	
	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	BrandRepository brandRepo;

	@Autowired
	ColorRepository colorRepo;

	@Autowired
	CategoryRepository categoryRepo;

	
	@RequestMapping("/")
	public String chart(Model model) {
		model.addAttribute("today", cartRepos.today());
		model.addAttribute("totalOrderToday", cartRepos.totalOrderToday());
		
		model.addAttribute("week", cartRepos.week());
		model.addAttribute("totalOrderWeek", cartRepos.totalOrderWeek());
		
		model.addAttribute("year", cartRepos.year());
		
		model.addAttribute("month", cartRepos.month());
		model.addAttribute("totalOrderMonth", cartRepos.totalOrderMonth());
		
		
		
		List<Product_Detail> list = product_DetailRepository.findAllByStock();
		model.addAttribute("list", list);

		List<Product> productList = productRepos.findAll();
		model.addAttribute("productList", productList);

		List<Product_Img> prodImg = prodimgRepo.findAll();
		model.addAttribute("prodImg", prodImg);

		List<String> sizeList = sizeRepo.findAllSizes();
		model.addAttribute("sizeList", sizeList);

		List<String> brandList = brandRepo.findAllBrand();
		model.addAttribute("brandList", brandList);

		List<String> colorList = colorRepo.findAllColor();
		model.addAttribute("colorList", colorList);

		List<String> categoryList = categoryRepo.findAllCategories();
		model.addAttribute("categoryList", categoryList);
		
		return "dashboard.html";
	}
	
	@RequestMapping("/searchByDate")
	public String searchByDate(@RequestParam("date1")String date1, @RequestParam("date2")String date2, Model model) {
		System.out.println(date1+" "+ date2);
		
		if(date1!=null && date2!=null) {
		String date1Temp = date1+" 00:00:00";
		String date2Temp = date2+" 00:00:00";
		
		model.addAttribute("byDate", cartRepos.byDate(date1Temp, date2Temp));
		model.addAttribute("totalByDate", cartRepos.totalByDate(date1Temp, date2Temp));
		}
		
		model.addAttribute("today", cartRepos.today());
		model.addAttribute("totalOrderToday", cartRepos.totalOrderToday());
		
		model.addAttribute("week", cartRepos.week());
		model.addAttribute("totalOrderWeek", cartRepos.totalOrderWeek());
		
		model.addAttribute("year", cartRepos.year());
		
		model.addAttribute("month", cartRepos.month());
		model.addAttribute("totalOrderMonth", cartRepos.totalOrderMonth());
		
		
		
		List<Product_Detail> list = product_DetailRepository.findAllByStock();
		model.addAttribute("list", list);

		List<Product> productList = productRepos.findAll();
		model.addAttribute("productList", productList);

		List<Product_Img> prodImg = prodimgRepo.findAll();
		model.addAttribute("prodImg", prodImg);

		List<String> sizeList = sizeRepo.findAllSizes();
		model.addAttribute("sizeList", sizeList);

		List<String> brandList = brandRepo.findAllBrand();
		model.addAttribute("brandList", brandList);

		List<String> colorList = colorRepo.findAllColor();
		model.addAttribute("colorList", colorList);

		List<String> categoryList = categoryRepo.findAllCategories();
		model.addAttribute("categoryList", categoryList);
		
		return "dashboard.html"; 
	}
}
