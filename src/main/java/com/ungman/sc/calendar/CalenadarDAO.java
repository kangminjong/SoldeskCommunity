package com.ungman.sc.calendar;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ungman.sc.admin.ClassId;
import com.ungman.sc.admin.ClassIdRepository;
import com.ungman.sc.user.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CalenadarDAO {

	@Autowired
	private ClassIdRepository cRepository;

	@Value("${google.api.client-id}")
	private String googleClientId;

	@Value("${google.api.api-key}")
	private String googleApiKey;

	@Value("${google.api.scopes}")
	private String googleScopes;

	public void calendarGo(HttpServletRequest req) {
		User u = (User) req.getSession().getAttribute("login");
		Optional<ClassId> cOptional = cRepository.findByClassId(u.getClassId());
		if (!cOptional.isPresent()) {
			ClassId classId = cOptional.get();
			
			req.setAttribute("calendarId", classId.getClassCalendarId());
		}
		req.setAttribute("googleClientId", googleClientId);
		req.setAttribute("googleApiKey", googleApiKey);
		req.setAttribute("googleScopes", googleScopes);
	}
}
