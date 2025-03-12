package com.ungman.sc.calendar;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ungman.sc.admin.ClassId;
import com.ungman.sc.admin.ClassIdRepository;
import com.ungman.sc.user.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CalenadarDAO {
	
	@Autowired
	private ClassIdRepository cRepository;
	public void calendarGo(HttpServletRequest req) {
		User u = (User)req.getSession().getAttribute("login");
		Optional<ClassId> cOptional = cRepository.findByClassId(u.getClassId());
		if(!cOptional.isPresent()) {
			ClassId classId = cOptional.get();
			req.setAttribute("calendarId", classId.getClassCalendarId());
		}
	}
}
