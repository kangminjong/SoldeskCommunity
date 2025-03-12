package com.ungman.sc.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanTokenGenerator;
import com.ungman.sc.board.Board;
import com.ungman.sc.user.User;
import com.ungman.sc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminController {
	@Autowired
	private AdminDAO aDAO;

	@Autowired
	private UserService uDAO;

	@Autowired
	private UngmanTokenGenerator utg;

	
	@Autowired
	private NoticeImageRepository nir;
	@GetMapping("/noticeImage/{imageName}")
	public @ResponseBody Resource getImage(@PathVariable(name = "imageName") String fileName) {
		return aDAO.getImage(fileName);
	}

	@GetMapping("/noticeDownloadFile/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fn) {
		return aDAO.getFile(fn);
	}

	// 회원 관리 페이지
	@GetMapping("/admin.user")
	public String adminUser(HttpServletRequest req, Model model) {
		req.setAttribute("menuPage", "menu");
		if (aDAO.isAdmin(req)) {
			aDAO.getUsers(req);
			List<User> users = (List<User>) req.getAttribute("users");
			model.addAttribute("users", users);
			model.addAttribute("user", req.getSession().getAttribute("login"));
			return "admin/user";
		} else {
			return "index";
		}
	}

	// 회원 삭제
	@GetMapping("/admin.user.delete")
	public String adminUserDelete(@RequestParam("email") String email, HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if (aDAO.isAdmin(req)) {
			aDAO.deleteUser(email, req);
			aDAO.getUsers(req);
		}
		return "admin/user";
	}

	// 게시물 관리 페이지
	@GetMapping("/admin.board")
	public String adminBoard(HttpServletRequest req, Model model) {
		req.setAttribute("menuPage", "menu");
		if (aDAO.isAdmin(req)) {
			aDAO.getBoards(req);
			
			List<Board> boards = (List<Board>) req.getAttribute("boards");
			model.addAttribute("boards", boards);
			model.addAttribute("user", req.getSession().getAttribute("login"));
			return "admin/adminboard";
		} else {
			return "index";
		}
	}

	// 게시물 삭제
	@GetMapping("/admin.board.delete")
	public String adminBoardDelete(@RequestParam("boardID") Integer boardId, HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if (aDAO.isAdmin(req)) {
			aDAO.deleteBoard(boardId, req);
			aDAO.getBoards(req); // 게시물 목록 갱신
		}
		return "admin/adminboard"; // 페이지 리다이렉트
	}

	@GetMapping("notice.go")
	public String noticeGo(HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		aDAO.adminClassId(req);
		if (uDAO.islogin(req)) {
			aDAO.clearSearch(req);
			aDAO.noticeGo(req, 1);
			return "admin/notice";
		}
		return "index";
	}

	@GetMapping("notice.write.show")
	public String noticeWriteShow(HttpServletRequest req, Notice n) {
		req.setAttribute("menuPage", "menu");
		if (uDAO.islogin(req)) {
			aDAO.noticeGo(req, 1);
			aDAO.writeShow(req, n);
			return "admin/readnotice";
		}
		return "index";
	}

	@GetMapping("/notice.write.go")
	public String noticeWriteGo(HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if (aDAO.isAdmin(req)) {
			utg.generate(req);
			return "admin/writenotice";
		} else {
			utg.generate(req);
			aDAO.noticeGo(req, 1);
			return "admin/notice";
		}

	}

	@PostMapping("/notice.write.do")
	public String noticeWriteDo(HttpServletRequest req, Notice n, NoticeImage noticeImage,
			@RequestParam(name = "noticeImageFile") MultipartFile mfImage,
			@RequestParam(name = "noticeFile") MultipartFile mfFile, NoticeFile noticeFile) {
		req.setAttribute("menuPage", "menu");
		if (aDAO.isAdmin(req)) {
			req.setAttribute("menuPage", "menu");
			aDAO.noticeWriteDo(req, n, noticeImage, noticeFile, mfImage, mfFile);
			
			aDAO.noticeGo(req, 1);
			return "admin/notice";
		}
		return "index";
	}

	@GetMapping("/notice.delete")
	public String boardDelete(@RequestParam(name = "noticeID") int noticeID, HttpServletRequest req, Notice n) {
		if (aDAO.isAdmin(req)) {
			req.setAttribute("menuPage", "menu");
			utg.generate(req);
			aDAO.deleteNotice(req, n);
			aDAO.noticeGo(req, 1);
			return "admin/notice";
		}
		return "admin/notice";

	}

	@GetMapping("/notice.update.go")
	public String noticeUpdateGo(HttpServletRequest req, Notice n) {
		if (aDAO.isAdmin(req)) {
			req.setAttribute("menuPage", "menu");
			aDAO.writeShow(req, n);
			return "admin/updatenotice";
		}
		return "admin/updateNotice";
	}

	@PostMapping("/notice.update.do")
	public String noticeUpdateDo(HttpServletRequest req, Notice n, NoticeImage noticeImage,
	                           @RequestParam(name = "noticeImageFile") MultipartFile mfImage,
	                           @RequestParam(name = "noticeFile") MultipartFile mfFile, NoticeFile noticeFile) {
	    if (aDAO.isAdmin(req)) {
	        // 기존 이미지 정보 가져오기
	        NoticeImage existingImage = nir.findByNoticeID(n);
	        
	        // 이미지 파일이 없으면(새로 업로드 하지 않았으면) 기존 이미지 정보 유지
	        if (mfImage == null || mfImage.isEmpty()) {
	            if (existingImage != null) {
	                // 기존 이미지 정보로 업데이트
	                noticeImage.setNoticeImageUrl(existingImage.getNoticeImageUrl());
	                noticeImage.setNoticeImageId(existingImage.getNoticeImageId());
	            }
	        }
	        
	        // 업데이트 처리
	        aDAO.noticeUpdateDo(req, n, noticeImage, noticeFile, mfImage, mfFile);
	        aDAO.writeShow(req, n);
	        
	        return "admin/readnotice";
	    }
	    return "index";
	}


	// Class ID 관리 페이지
	@GetMapping("/admin.classid")
	public String adminClassID(HttpServletRequest req) {
		if (aDAO.isAdmin(req)) {
			aDAO.adminClassId(req);
			utg.generate(req);

			return "admin/classid";
		}
		return "index";
	}

	@PostMapping("/admin.classId.append")
	public String adminClassIdAppend(HttpServletRequest req, ClassId classId) {
		if (aDAO.isAdmin(req)) {
			aDAO.adminClassAppend(req, classId);
			utg.generate(req);
			aDAO.adminClassId(req);
			return "admin/classid";
		}
		return "index";
	}

	@GetMapping("/admin.classId.delete")
	public String adminClassIdDelete(HttpServletRequest req, @RequestParam(name = "classId") int classId, ClassId c) {
		if (uDAO.islogin(req)) {
			aDAO.adminClassDelete(req, c);
			aDAO.adminClassId(req);
			return "admin/classid";
		}
		return "admin/classid";
	}

	@GetMapping("/notice.page.change")
	public String noticePageChange(@RequestParam(name = "page") int page, HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if (uDAO.islogin(req)) {
			utg.generate(req);
			aDAO.noticeGo(req, page);
		}
		return "admin/notice";
	}

	@PostMapping("/notice.Search")
	public String searchNotice(HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			req.setAttribute("menuPage", "menu");
			utg.generate(req);
			aDAO.searchNotice(req);
			aDAO.noticeGo(req, 1);
		}
		return "admin/notice";
	}

	@GetMapping(value = "/classId.get", produces = "application/json;charset=utf-8")
	public @ResponseBody classIds tkdGet(@RequestParam("classId") Integer classId,
			@RequestParam("classPw") String classPw, HttpServletResponse res) {
		return aDAO.toJson(classId, classPw);
	}

	@GetMapping("admin.notice")
	public String adminNotice(HttpServletRequest req) {
		if (aDAO.isAdmin(req)) {
			aDAO.adminNotice(req);
			return "admin/adminnotice";
		}
		return "index";
	}

	@GetMapping("/admin.notice.delete")
	public String adminNoticeDelete(@RequestParam(name = "noticeID") int noticeID, HttpServletRequest req, Notice n) {
		if (aDAO.isAdmin(req)) {
			utg.generate(req);
			aDAO.deleteNotice(req, n);
			aDAO.adminNotice(req);
			return "admin/adminnotice";
		}
		return "admin/notice";

	}
}
