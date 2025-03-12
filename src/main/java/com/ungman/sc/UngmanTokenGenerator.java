package com.ungman.sc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UngmanTokenGenerator {
	private SimpleDateFormat sdf;

	public UngmanTokenGenerator() {
		sdf = new SimpleDateFormat("yyyyMMddHHmmssSS"); 
	}

	public void generate(HttpServletRequest req) {
		Date now = new Date();
		String token = sdf.format(now);
		req.setAttribute("token", token);
	}
}
