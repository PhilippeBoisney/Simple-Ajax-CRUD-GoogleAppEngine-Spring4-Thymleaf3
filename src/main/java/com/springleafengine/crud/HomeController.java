package com.springleafengine.crud;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	public HomeController() {
		super();
	}
	
	@RequestMapping({"/", "/dashboard"})
	public String test(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Dans test => {}.", locale);
		
		List<Product> products = new ArrayList<>();
		products.add(new Product("Produit 1", "Description du produit 1", false));
		products.add(new Product("Produit 2", "Description du produit 2", true));
		products.add(new Product("Produit 3", "Description du produit 3", true));
		products.add(new Product("Produit 4", "Description du produit 4", false));
		
		User userAdmin = new User("Sami MADINI", "Admin", true);
		User userMember = new User("Martin SMITH", "Member", false);
		
		model.addAttribute("title", "Dashboard");
		model.addAttribute("currentFragment", "home");
		model.addAttribute("currentFragmentTag", "content");
		model.addAttribute("userAdmin", userAdmin);
		model.addAttribute("userMember", userMember);
		model.addAttribute("products", products);

		return "base";
	}
	
}