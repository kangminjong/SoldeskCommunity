package com.ungman.sc.academy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AcademyController {

	@GetMapping("/academy.go")
    public String academyGo(HttpServletRequest req) { 
		req.setAttribute("menuPage", "menu");
        return "academy/academy"; 
    }
}
