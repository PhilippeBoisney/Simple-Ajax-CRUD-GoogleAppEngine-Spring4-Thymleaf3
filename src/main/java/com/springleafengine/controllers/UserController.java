package com.springleafengine.controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.springleafengine.interfaces.IApiObject;
import com.springleafengine.models.User;
import com.springleafengine.services.ApiJsonResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UserController {
	
	private final static String AJAX_INFO = "info";
	private final static String AJAX_SUCCESS = "success";
	private final static String AJAX_WARNING = "warn";
	private final static String AJAX_DANGER = "error";
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private static int currentIndex = -1;
	private static List<User> database;
	private User user;
	
	@Resource(name = "messageSource")
    private MessageSource messageSource;
	
	public UserController() {
		super();
		
		// Init database
		database = new ArrayList<>();
		database.add(new User("1", "Sami", "MADINI", "Admin", true));
		database.add(new User("2", "Ang", "LEE", "Member", false));
		database.add(new User("3", "Bruce", "BANNER", "Member", false));
		database.add(new User("4", "Hawkgirl", "INJUSTICE", "Member", false));
		database.add(new User("5", "Spider", "MAN", "Member", false));
		database.add(new User("6", "Iron", "MAN", "Member", false));
		database.add(new User("7", "Super", "MAN", "Member", false));
		database.add(new User("8", "James", "HOWLETT", "Member", false));
		database.add(new User("9", "Black", "WIDOW", "Member", false));
		database.add(new User("10", "Green", "ARROW", "Member", false));
	}
	
	@RequestMapping({"/users/list"})
	public String simpleCrud(HttpServletRequest request, Locale locale, Model model, HttpServletRequest req) {
		
		User userAdmin = new User("1", "Sami", "MADINI", "Admin", true);
		User userMember = new User("2", "Ang", "LEE", "Member", false);		
	
		// Configure variables template
		model.addAttribute("title", "Simple CRUD");
		model.addAttribute("currentFragment", "crud");
		model.addAttribute("currentFragmentTag", "content");
		model.addAttribute("userAdmin", userAdmin);
		model.addAttribute("userMember", userMember);
		model.addAttribute("users", database);
		model.addAttribute("totalItems", 10);
		model.addAttribute("baseUrl", req.getContextPath());
		model.addAttribute("region", locale);

		return "base";
	}
	
	/**
	 * Search user by NAME
	 * 
	 * @param String lastname
	 * 
	 * @return ApiJsonResponse
	 */
	@RequestMapping({"/users/api-search"})
	public @ResponseBody byte[] apiSearch(Locale locale, @RequestParam(value = "lastname", required = false) String lastname) {
		
			byte[] json;
			Gson gson = new Gson();
			ApiJsonResponse obj = new ApiJsonResponse();
			
			try {
				if(lastname.isEmpty()) {
					obj.setStatus(503);
					obj.setStyleClass(AJAX_WARNING);
					obj.setMessage(messageSource.getMessage("ajax.users.search.pleaseEnterData", new Object[0], locale));
				} else {
					List<IApiObject> users = this.findAllUsersInDatabase(lastname);
					
					if(!users.isEmpty()) {
						obj.setStatus(200);
						obj.setStyleClass(AJAX_SUCCESS);
						obj.setListObjs(users);
						obj.setMessage(messageSource.getMessage("ajax.users.search.userRetrieve", new Object[0], locale));
					} else {
						obj.setStatus(201);
						obj.setStyleClass(AJAX_WARNING);
						obj.setMessage(messageSource.getMessage("ajax.users.search.userNoRetrieve", new Object[0], locale) + " " + lastname);
					}
				}
				json = gson.toJson(obj).getBytes("UTF-8");
			} catch(Exception e) {
				obj.setStatus(500);
				obj.setStyleClass(AJAX_DANGER);
				obj.setMessage(messageSource.getMessage("ajax.users.search.error", new Object[0], locale) + " " + e.getMessage());
				json = gson.toJson(obj).getBytes();
			}
			return json;
	}

	/**
	 * Add a user in database
	 * 
	 * @param String name
	 * @param String surname
	 * @param String type
	 * @param boolean admin
	 * 
	 * @return ApiJsonResponse
	 */
	@RequestMapping({"/users/api-add"})
	public @ResponseBody byte[] apiAdd(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "lastname", required = false) String lastname,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "admin", required = false) boolean admin) {
		
			byte[] json;
			Gson gson = new Gson();
			ApiJsonResponse obj = new ApiJsonResponse();
			
			try {
				if(lastname.isEmpty() || name.isEmpty() || type.isEmpty()) {
					obj.setStatus(503);
					obj.setStyleClass(AJAX_WARNING);
					obj.setMessage("Veuillez remplir tous les champs du formulaire");
				} else {
					int nb = database.size() + 1;
					String id = Integer.toString(nb);
					this.user = new User(id, name, lastname, type, admin);
					// add user in database with service layer
					database.add(this.user);
					
					obj.setStatus(200);
					obj.setStyleClass(AJAX_SUCCESS);
					obj.setMessage("Utilisateur ajouté avec succès");
					obj.setObj(this.user);
				}
				json = gson.toJson(obj).getBytes("UTF-8");
			} catch(Exception e) {
				obj.setStatus(500);
				obj.setStyleClass(AJAX_DANGER);
				obj.setMessage("Une erreur est survenue : " + e.getMessage());
				json = gson.toJson(obj).getBytes();
			}
			return json;
	}
	
	/**
	 * Update a user in database
	 * 
	 * @param String name
	 * @param String surname
	 * @param String type
	 * @param boolean admin
	 * 
	 * @return ApiJsonResponse
	 */
	@RequestMapping({"/users/api-update"})
	public @ResponseBody byte[] apiUpdate(@RequestParam(value = "id", required = false) String userId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "lastname", required = false) String lastname, 
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "admin", required = false) boolean admin) {
		
			byte[] json;
			Gson gson = new Gson();
			ApiJsonResponse obj = new ApiJsonResponse();
			
			try {
				if(lastname.isEmpty() || name.isEmpty() || type.isEmpty()) {
					obj.setStatus(503);
					obj.setStyleClass(AJAX_WARNING);
					obj.setMessage("Veuillez remplir tous les champs du formulaire");
				} else {
					// find user in database with service layer
					this.user = this.findUserInDatabase(userId);
					this.bindParam(name, lastname, type, admin);
					// update user in database with service layer
					if(currentIndex != -1) {
						database.set(currentIndex, this.user);
					}
					
					obj.setStatus(200);
					obj.setStyleClass(AJAX_SUCCESS);
					obj.setMessage("Utilisateur modifié avec succès");
					obj.setObj(this.user);
				}
				json = gson.toJson(obj).getBytes("UTF-8");
			} catch(Exception e) {
				obj.setStatus(500);
				obj.setStyleClass(AJAX_DANGER);
				obj.setMessage("Une erreur est survenue : " + e.getMessage());
				json = gson.toJson(obj).getBytes();
			}
			return json;
	}

	/**
	 * Delete a user in database
	 * 
	 * @param String userId
	 * 
	 * @return ApiJsonResponse
	 */
	@RequestMapping({"/users/api-delete"})
	public @ResponseBody byte[] apiDelete(@RequestParam(value = "id", required = false) String userId) {
		
			byte[] json;
			Gson gson = new Gson();
			ApiJsonResponse obj = new ApiJsonResponse();
			
			try {
				if(userId.isEmpty()) {
					obj.setStatus(503);
					obj.setStyleClass(AJAX_WARNING);
					obj.setMessage("Impossible de retrouver l'utilisateur sélectionné.");
				} else {
					this.user = this.findUserInDatabase(userId);
					// remove user in database with service layer
					if(currentIndex != -1) {
						database.remove(currentIndex);
					}
					
					obj.setStatus(200);
					obj.setStyleClass(AJAX_SUCCESS);
					obj.setMessage("Utilisateur supprimé avec succès");
					obj.setOldId(userId);
				}
				json = gson.toJson(obj).getBytes("UTF-8");
			} catch(Exception e) {
				obj.setStatus(500);
				obj.setStyleClass(AJAX_DANGER);
				obj.setMessage("Une erreur est survenue : " + e.getMessage());
				json = gson.toJson(obj).getBytes();
			}
			return json;
	}
	
	/**
	 * Bind request parameters with private user object
	 * 
	 * @param String name
	 * @param String surname
	 * @param String type
	 * @param boolean admin
	 */
	private void bindParam(String name, String lastname, String type, boolean admin) {
		this.user.setName(name);
		this.user.setLastname(lastname);
		this.user.setType(type);
		if(admin) {
			this.user.setAdmin(admin);
		}
	}
	
	private User findUserInDatabase(String userId) {
		int count = 0;
		for(User u : database) {
			if(u.getId().equals(userId)) {
				count++;
				this.currentIndex = count; 
				return u;
			}
		}
		return null;
	}
	
	private List<IApiObject> findAllUsersInDatabase(String name) {
		List<IApiObject> users = new ArrayList<>();
		for(User u : database) {
			if(u.getLastname().equals(name)) {
				users.add(u);
			}
		}
		return users;
	}
	
}