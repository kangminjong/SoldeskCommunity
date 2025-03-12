package com.ungman.sc.dataroom;

import java.io.File;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanFileNameTokenGenerator;
import com.ungman.sc.user.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DataroomService {

	@Autowired
	private DataroomRepo dr;

	@Value("${dataroom.file.folder}")
	private String fileFolder;

	@Value("${file.maxSize}")
	private int fileMaxSize;

	public void deleteFile(DataroomFile df, HttpServletRequest req) {
		try {
			df = dr.findById(df.getNo()).get();
			dr.delete(df);
			req.setAttribute("result", "삭제 성공");
			new File(fileFolder + "/" + df.getFile()).delete();
		} catch (Exception e) {
			req.setAttribute("result", "삭제 실패");
		}
	}

	public ResponseEntity<Resource> downloadFile(String file) {
		try {
			UrlResource ur = new UrlResource("file:" + fileFolder + "/" + file);
			String h = "attachment; filename=\"" + URLEncoder.encode(file, "utf-8") + "\"";
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, h).body(ur);
		} catch (Exception e) {
			return null;
		}
	}

	public void getFile(HttpServletRequest req) {
		req.setAttribute("gs", dr.findByCategoryOrderByDateDesc("g"));
		req.setAttribute("ys", dr.findByCategoryOrderByDateDesc("y"));
		req.setAttribute("ps", dr.findByCategoryOrderByDateDesc("p"));
		req.setAttribute("es", dr.findByCategoryOrderByDateDesc("e"));
	}

	public void uploadFile(MultipartFile mf, DataroomFile df, HttpServletRequest req) {
		String fileName = null;

		
		try {
			if (mf.getSize() > fileMaxSize) {
				throw new Exception("파일 크기가 너무 큽니다.");
			}
			fileName = UngmanFileNameTokenGenerator.generate(mf);
			mf.transferTo(new File(fileFolder + "/" + fileName));
		} catch (Exception e) {
			req.setAttribute("result", "업로드 실패(파일): " + e.getMessage());
			return;
		}
		try {
			
			String oldSuccessToken = (String) req.getSession().getAttribute("successToken");
			String token = req.getParameter("token");
			if (oldSuccessToken != null && token.equals(oldSuccessToken)) {
				req.setAttribute("result", "등록실패(새로고침)");
				return;
			}
			User u = (User) req.getSession().getAttribute("login");
			df.setUserEmail(u.getEmail());
			df.setClassId(u.getClassId());
			df.setFile(fileName);
			dr.save(df);
			req.getSession().setAttribute("successToken", token);
			req.setAttribute("result", "업로드 성공");
		} catch (Exception e) {
			req.setAttribute("result", "업로드 실패(DB): " + e.getMessage());
			if (fileName != null) {
				new File(fileFolder + "/" + fileName).delete();
			}
		}
	}
}
