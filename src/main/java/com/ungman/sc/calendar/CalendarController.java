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
			// 기존 캘린더 정보 가져오기
			cDAO.calendarGo(req);
			
			// Google Calendar API 정보 추가
			
			
			return "calendar/calendar";
		}
		return "index";
	}
}