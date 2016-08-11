package com.springleafengine.controllers;


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
import com.springleafengine.models.User;

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
	public String home(HttpServletRequest request, Locale locale, Model model, HttpServletRequest req) {
		logger.info("Dans test => {}.", locale);
		
		User userAdmin = new User("1", "Sami", "MADINI", "Admin", true);
		User userMember = new User("2", "Ang", "LEE", "Member", false);
		
		// Configure variables template
		model.addAttribute("title", "Dashboard");
		model.addAttribute("currentFragment", "home");
		model.addAttribute("currentFragmentTag", "content");
		model.addAttribute("userAdmin", userAdmin);
		model.addAttribute("userMember", userMember);
		model.addAttribute("baseUrl", req.getContextPath());
		model.addAttribute("region", locale);

		return "base";
	}
	
}