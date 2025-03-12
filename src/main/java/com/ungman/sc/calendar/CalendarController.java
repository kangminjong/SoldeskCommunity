package com.ungman.sc.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ungman.sc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CalendarController {
	
	@Autowired
	private CalenadarDAO cDAO;
	
	@Autowired
	private UserService uDAO;
	@GetMapping("/calendar.go")
	public String calendarGo(HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if(uDAO.islogin(req)) {
			cDAO.calendarGo(req);
			
			return "calendar/calendar";
		}
		return "index";
	}
}
