package ua.tss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ua.tss.model.Cart;
import ua.tss.model.Product;
import ua.tss.service.CartService;
import ua.tss.service.ImageService;
import ua.tss.service.ProductService;


@Controller
@RequestMapping("product")
public class ProductController extends SuperController{

	@Autowired
    private ImageService imageService;

	@Autowired
    private ProductService productService;

	@Autowired
	private CartService cartService;


	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String list(Model model) {
		model.addAttribute("products", productService.findAll());
		model.addAttribute("product", new Product());
		return "product-list";
	}

	/* test **** needs update */
	/*#################################################################*/
	@GetMapping("/products")
	public String products(Model model,HttpServletRequest request) {
		Cart cart = cartService.findBySessionId(request.getSession(true).getId());

		List<Product> products = new ArrayList<Product>();
		for (Product product : productService.findAll()) {
			if(product.getStock()!=null&&product.getStock()>0) {
				products.add(product);
			}
		}
		model.addAttribute("cart", cart);
		model.addAttribute("products", products);
		return "products";
	}


	/*#################################################################*/


	@GetMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(@PathVariable("id") long id,Model model) {
		Product product = productService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		model.addAttribute("product", product);
		return "product-update";
	}

	@PostMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String update(Product product,BindingResult result,Model model,@RequestParam("files") MultipartFile[] files) {
		if (result.hasErrors()) {
			model.addAttribute("products", productService.findAll());
			return "product-list";
		}
		if (productService.findByName(product.getName()) == null) {
			FieldError existError = new FieldError("product", "name", "Product is not Exists!");
			result.addError(existError);
			model.addAttribute("products", productService.findAll());
			return "product-list";
		}
		productService.save(product);
	    Arrays.asList(files).stream().forEach(file -> {
	    	if(!file.isEmpty()) {
				try {
					imageService.storeImage(file,product);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		});
		return "redirect:/product/list";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String create(Product product,BindingResult result,Model model,@RequestParam("files") MultipartFile[] files) {
		if (result.hasErrors()) {
			model.addAttribute("products", productService.findAll());
			return "product-list";
		}
		if (productService.findByName(product.getName()) != null) {
			FieldError existError = new FieldError("product", "name", "Product Exists!");
			result.addError(existError);
			model.addAttribute("products", productService.findAll());
			return "product-list";
		}
		productService.save(product);
		if(files!=null) {
	         Arrays.asList(files).stream().forEach(file -> {
				try {
					imageService.storeImage(file,product);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		return "redirect:/product/list";
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	public String delete(@PathVariable("id") Long id, Model model) {
		Product product = productService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		if(!product.getImages().isEmpty()&&product.getImages()!=null) {
	        product.getImages().stream().forEach(image -> imageService.delete(image));
		}
		productService.delete(product);
		return "redirect:/product/list";
	}
}
