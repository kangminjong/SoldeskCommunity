package com.ungman.sc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ungman.sc.admin.AdminDAO;
import com.ungman.sc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController { 
	@Autowired
	private UserService uDAO;
	@Autowired
	private UngmanTokenGenerator utg;

	@Autowired
	private AdminDAO aDAO;
	
	@GetMapping("/")
	   public String index(HttpServletRequest req) {
		  req.setAttribute("menuPage", "menu");
		  aDAO.adminClassId(req);
		  uDAO.islogin(req);
		  utg.generate(req);
	      return "index";
	   }
	   
	   @GetMapping("/main.go")
	   public String main(HttpServletRequest req) {
		   aDAO.adminClassId(req);
		   return index(req);
	   }
}
