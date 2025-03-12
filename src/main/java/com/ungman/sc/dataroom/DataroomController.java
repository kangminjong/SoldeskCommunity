package com.ungman.sc.dataroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanTokenGenerator;
import com.ungman.sc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DataroomController {

	@Autowired
	private DataroomService dDAO;

	@Autowired
	private UserService uDAO;

	@Autowired
	private UngmanTokenGenerator utg;
	@GetMapping("/dataroom.file.delete")  
	public String dataroomFileDelete(DataroomFile df, HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			req.setAttribute("menuPage", "menu");
			dDAO.deleteFile(df, req);
			dDAO.getFile(req);
		}
		return "dataroom/dataroom";
	}

	@GetMapping("/dataroom/{file}")
	public ResponseEntity<Resource> dataroomFileDownload(@PathVariable(name = "file") String file) {
		return dDAO.downloadFile(file);
	}

	@PostMapping("/dataroom.file.upload")
	public String dataroomFileUpload(@RequestParam(name = "fileTemp") MultipartFile mf, DataroomFile df, HttpServletRequest req) {
		utg.generate(req);
		if (uDAO.islogin(req)) {
			req.setAttribute("menuPage", "menu");
			dDAO.uploadFile(mf, df, req);
			dDAO.getFile(req);
			
		} 
	return "dataroom/dataroom";
	}

	@GetMapping("/dataroom.go")
	public String dataroomGo(HttpServletRequest req) {
		utg.generate(req);
		if (uDAO.islogin(req)) {
			req.setAttribute("menuPage", "menu");
			dDAO.getFile(req);
		} 
		return "dataroom/dataroom";
	}
}
