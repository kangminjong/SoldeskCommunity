package com.ungman.sc.location;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LocationController {
   @GetMapping("/location.go")
   public String locationGo(HttpServletRequest req) {
	   req.setAttribute("menuPage", "menu");
      return "location/location";
   }
}