package com.fpoly.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpoly.model.Cart;
import com.fpoly.model.Cart_Item;
import com.fpoly.model.Product;
import com.fpoly.model.Product_Detail;
import com.fpoly.model.Product_Img;
import com.fpoly.model.Role;
import com.fpoly.model.ShoppingCart;
import com.fpoly.model.Size;
import com.fpoly.model.User;
import com.fpoly.model.User_Role;
import com.fpoly.repositories.BrandRepository;
import com.fpoly.repositories.CartRepository;
import com.fpoly.repositories.Cart_ItemRepository;
import com.fpoly.repositories.CategoryRepository;
import com.fpoly.repositories.ColorRepository;
import com.fpoly.repositories.ProductRepository;
import com.fpoly.repositories.Product_DetailRepository;
import com.fpoly.repositories.Product_ImgRepository;
import com.fpoly.repositories.RoleRepository;
import com.fpoly.repositories.SizeRepository;
import com.fpoly.repositories.UserRepository;
import com.fpoly.repositories.User_RoleRepository;
import com.fpoly.service.OrderService;
import com.fpoly.service.Product_DetailService;
import com.fpoly.service.ShoppingCartService;
import com.fpoly.service.UserService;

@Controller
@RequestMapping("admin")
public class AdminControlle {
	
	@Autowired
	UserRepository ureps;
	
	@Autowired
	Product_DetailRepository product_DetailRepository;
	
	@Autowired
	Cart_ItemRepository cartItemRepos;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserRepository user;

	@Autowired
	Product_DetailService product_DetailService;

	@Autowired
	ProductRepository productRepos;

	@Autowired
	Product_ImgRepository prodimgRepo;

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

	@Autowired
	private CartRepository cartRepos;

	@Autowired
	private Cart_ItemRepository cIRepos;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private User_RoleRepository userRoleRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@RequestMapping(value = "")
	public String getAll1(Model model) {
		List<Product_Detail> list = product_DetailRepository.findAll();
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

		return "admin/show";
	}

	@RequestMapping(value = "orderlist", method = RequestMethod.GET)
	public String getOrderList(Model model) {
		String status = "";
		status = "Chờ xét duyệt";
		List<Cart> cartList = cartRepos.findByDate(status);
		model.addAttribute("cartList", cartList);
		return "admin/OrderList";
	}

	@RequestMapping(value = "orderlistfilter", method = RequestMethod.GET)
	public String getOrderListFilter(Model model, @ModelAttribute Cart cart, @ModelAttribute("orderDate1")Date orderDate1, @ModelAttribute("orderDate2")Date orderDate2  ) {
		System.out.println(cart.getOrderStatus());

		String status = "";
		if (cart.getOrderStatus() != "") {
			status = cart.getOrderStatus();
			List<Cart> cartList = cartRepos.findByDate(status);
			model.addAttribute("cartList", cartList);
		} else if (cart.getOrderStatus() == null) {
			status = "Chờ xét duyệt";
			List<Cart> cartList = cartRepos.findByDate(status);
			model.addAttribute("cartList", cartList);
		
		} else {
			List<Cart> cartList = cartRepos.findByDate2();
			model.addAttribute("cartList", cartList);
		}
		
		return "admin/OrderList";
	}

	@RequestMapping("/order-detail")
	public String orderDetail(@RequestParam("order") Integer id, Model model) {

		List<String> status = cartRepos.findStatus();
		model.addAttribute("status", status);

		for (int i = 0; i < status.size(); i++) {
			System.out.println(status.get(i));
		}

		List<Product_Detail> product = product_DetailRepository.findAll();
		model.addAttribute("product", product);

		List<String> size = sizeRepo.findAllSizes();
		model.addAttribute("size", size);

		Cart order = orderService.findOrderWithDetails(id);
		model.addAttribute("order", order);

		return "admin/orderDetails";
	}

	@RequestMapping(value = "order-detail-update/{id}", method = RequestMethod.POST)
	public String updateOD(@ModelAttribute("order") Cart order, @PathVariable("id") Integer id, RedirectAttributes attr,
			 Authentication authentication,
			Model model) {

		Cart orderTemp = cartRepos.getById(id);
		
		if (orderTemp.getOrderStatus().equals("Đang vận chuyển") && order.getOrderStatus().equals("Hủy đơn")) {
			attr.addFlashAttribute("alert", true);
			attr.addFlashAttribute("message", "Đang trong quá trình vận chuyển, không thể hủy đơn hàng!");
			return "redirect:/admin/order-detail?order=" + id;
		}

		if (orderTemp.getOrderStatus().equals("Chờ xét duyệt") && order.getOrderStatus().equals("Đang vận chuyển")) {
			attr.addFlashAttribute("alert", true);
			attr.addFlashAttribute("message", "Đơn hàng đang chờ xét duyệt, không thể vận chuyển!");
			return "redirect:/admin/order-detail?order=" + id;
		}

		if (orderTemp.getOrderStatus().equals("Đang đóng gói") && order.getOrderStatus().equals("Hoàn thành")) {
			attr.addFlashAttribute("alert", true);
			attr.addFlashAttribute("message", "Đơn hàng đang đóng gói, không thể hoàn thành!");
			return "redirect:/admin/order-detail?order=" + id;
		}

		if (orderTemp.getOrderStatus().equals("Chờ xét duyệt") && order.getOrderStatus().equals("Hoàn thành")) {
			attr.addFlashAttribute("alert", true);
			attr.addFlashAttribute("message", "Đơn hàng đang chờ xét duyệt, không thể hoàn thành!");
			return "redirect:/admin/order-detail?order=" + id;
		}

		if (orderTemp.getOrderStatus().equals("Hủy đơn") && order.getOrderStatus().equals("Hoàn thành")) {
			attr.addFlashAttribute("alert", true);
			attr.addFlashAttribute("message", "Đơn hàng đã bị hủy");
			return "redirect:/admin/order-detail?order=" + id;
		}

		if (order.getOrderStatus().equals("Hủy đơn") && order.getMessage().equals("")) {
			attr.addFlashAttribute("alert", true);
			attr.addFlashAttribute("message", "Đơn hàng hủy phải có lý do, xin mời nhập tại ghi chú.");
			return "redirect:/admin/order-detail?order=" + id;
		}	
			orderTemp.setOrderStatus(order.getOrderStatus());
			orderTemp.setBuyer_status(order.getBuyer_status());
			orderTemp.setMessage(order.getMessage());
			cartRepos.save(orderTemp);
		

		return "redirect:/admin/orderlist";
	}

	@RequestMapping(value = "/order-update/{id}")
	public String createOrder(Model model, @PathVariable("id") Integer id) {
		List<Product_Detail> product = product_DetailRepository.findAll();
		model.addAttribute("product", product);

		List<String> size = sizeRepo.findAllSizes();
		model.addAttribute("size", size);
		
		Cart order = orderService.findOrderWithDetails(id);
		model.addAttribute("order", order);
		model.addAttribute("idCart", id);
		return "admin/createOrder";
	}
	
	@RequestMapping(value = "/deleteProduct/{id}/{idCart}")
	public String deleteProduct(Model model, @PathVariable("id") Integer id,@PathVariable("idCart") Integer idCart) {
		Cart_Item cartItem = cIRepos.getById(id);
	    
		
		Cart order = orderService.findOrderWithDetails(idCart);
		Float totalOld = order.getOrderTotal().floatValue();
		Float totalNew = cartItem.getSubtotal().floatValue();
		Float totalCash = (totalOld - totalNew);
		order.setOrderTotal(BigDecimal.valueOf(totalCash));
		cartRepos.save(order);
		
		cIRepos.delete(cartItem);
		model.addAttribute("order", order);
		model.addAttribute("idCart", id);
		return "admin/createOrder";
	}
	
	@RequestMapping(value = "/order-update-search/{id}")
	public String search(Model model, @PathVariable("id") Integer id, @ModelAttribute("productName")String productName) {
		Product_Detail productDetail = product_DetailRepository.findByTitle(productName.trim());
		System.out.println(productName);
		if(productDetail !=null) {
			
			model.addAttribute("productDetail", productDetail);
		}
		Cart order = orderService.findOrderWithDetails(id);
		model.addAttribute("order", order);
		List<String> size = sizeRepo.findAllSizes();
		model.addAttribute("size", size);
		model.addAttribute("idCart", id);
		return "admin/createOrder";
	}
	
	@RequestMapping(value="/orderSave/")
	public String orderSave(Model model,RedirectAttributes attr, @ModelAttribute("size")String size,  @ModelAttribute("product")Integer product,  @ModelAttribute("order")Integer order,  @ModelAttribute("qty")Integer qty ) {
		Cart orders = orderService.findOrderWithDetails(order);
		Product_Detail pd = product_DetailRepository.getById(product);
		Cart_Item cartItem = new Cart_Item();
		
			if(size!=null && Integer.parseInt(size)>0) {
				cartItem.setSize(size);
			}else {
				attr.addFlashAttribute("alert", "Xin mời chọn kích cỡ!");
				return "redirect:/admin/order-update/"+order;
			}
		
			if(pd!=null) {
				if (!pd.hasStock(qty)) {
					attr.addFlashAttribute("alert", "Không thể thêm vào giỏ hàng, không đủ sản phẩm trong kho.");
					return "redirect:/admin/order-update/"+order;
				}
				cartItem.setProductDetail(pd);
			}
			
			if (qty!=null) {
				if(qty==0) {
					attr.addFlashAttribute("alert", "Không thể thêm vào giỏ hàng, số lượng sản phẩm không hợp lệ");
					return "redirect:/admin/order-update/"+order;
				}
				cartItem.setQty(qty);
			}
			
			cartItem.setCart(orders);
			
			cartItemRepos.save(cartItem);
			
			Float totalOld = orders.getOrderTotal().floatValue();
			Float totalNew = cartItem.getSubtotal().floatValue();
			Float totalCash = (totalOld + totalNew);
			orders.setOrderTotal(BigDecimal.valueOf(totalCash));
			cartRepos.save(orders);
			
		
		return "redirect:/admin/order-update/"+order;
	}
	
	@RequestMapping(value="/employee")
	public String getAllEmpl(Model model) {
		List<User> list = ureps.findByRolel();
		model.addAttribute("list", list);
		return "admin/employee";
		
	}
	
	@RequestMapping(value ="/saveEmployee")
	public String editEmployee(Model model) {
		
		return "admin/saveEmployee";
	}
	
	@PostMapping("/saveEmployee")
	public String sE(Model model, @ModelAttribute("user") User user, BindingResult bindingResults,
			RedirectAttributes redirectAttributes) {
		
		boolean invalidFields = false;

		if (bindingResults.hasErrors()) {
			return "redirect:/admin/saveEmployee";
		}
		if (userService.findByUsername(user.getUsername()) != null) {
			redirectAttributes.addFlashAttribute("usernameExists", true);
			invalidFields = true;
		}
		if (invalidFields) {
			return "redirect:/admin/saveEmployee";
		}
		User userTemp = ureps.save(user);
		
		if (userTemp != null) {
			Role role = roleRepo.findByRoleName("ROLE_ADMIN");
			User_Role u = new User_Role();
			u.setRole(role);
			u.setUser(userTemp);
			userRoleRepo.save(u);
		}
		return "redirect:/admin/employee";
	}
	
	
	@RequestMapping(value ="/editEmployee/{id}")
	public String editEmployee(Model model,@PathVariable Integer id) {
		User userList = ureps.getById(id);
		model.addAttribute("userList", userList);
		return "admin/editEmployee";
	}
	
	@PostMapping("/updateEmployee")
	public String updateE(Model model, @ModelAttribute("user") User user) {
		ureps.save(user);
		return "redirect:/admin/employee";
	}
	
	@DeleteMapping("/deleteEmployee/{id}")
	public String delE(Model model,@PathVariable Integer id) {
		User u = ureps.getById(id);
		ureps.delete(u);
		return "redirect:/admin/employee";
	}
}
