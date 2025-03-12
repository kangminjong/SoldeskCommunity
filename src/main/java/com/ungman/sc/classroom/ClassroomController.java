package com.ungman.sc.classroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ClassroomController {
   @GetMapping("/canvas.go")
   public String boardGo(HttpServletRequest req) {
	  req.setAttribute("menuPage", "menu");
      return "classroom/canvas";
   }  
}
