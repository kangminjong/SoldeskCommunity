package com.ungman.sc.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanTokenGenerator;
import com.ungman.sc.admin.AdminDAO;
import com.ungman.sc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BoardController {
	

	@Autowired
	private UngmanTokenGenerator utg;

	@Autowired
	private UserService uDAO;

	@Autowired
	private BoardDAO bDAO;
	
	@Autowired
	private AdminDAO aDAO;
	
	@GetMapping("/downloadFile/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fn) {
		return bDAO.getFile(fn);
	}

	@GetMapping("/board.go")
	public String boardGo(HttpServletRequest req) {  
		req.setAttribute("menuPage", "menu");
		aDAO.adminClassId(req);
		if (uDAO.islogin(req)) {
		bDAO.clearSearch(req);
		bDAO.writeGo(req, 1);
		return "board/board";
		}
		return "index";
	}

	@GetMapping("/write.go")
	public String writeGo(HttpServletRequest req) {
		utg.generate(req);

		return "board/writeboard";
	}

	@PostMapping("/write.Do")
	public String writeDO(HttpServletRequest req, Board b, BoardImage boardImage,
			@RequestParam(name = "boardImageFile") MultipartFile mfImage,
			@RequestParam(name = "boardFile") MultipartFile mfFile, BoardFile boardFile) {
		req.setAttribute("menuPage", "menu");
		if (uDAO.islogin(req)) {
			utg.generate(req);
			uDAO.islogin(req);
			
			bDAO.writeDo(req, b, boardImage, mfImage, mfFile, boardFile);
			bDAO.writeGo(req, 1);
		}
		return "board/board";
	}
	
	@GetMapping("/sns.page.change")
	public String snsPageChange(@RequestParam(name = "page") int page, HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if (uDAO.islogin(req)) {
			utg.generate(req);
			bDAO.writeGo(req, page);
		}
		return "board/board";
	}

	@GetMapping("/write.update.go")
	public String writeUpdateGo(HttpServletRequest req, Board b) {
		bDAO.writeShow(req, b);
		return "board/updateBoard";
	}

	@GetMapping("/boardImage/{imageName}")
	public @ResponseBody Resource getImage(@PathVariable(name = "imageName") String fileName) {
		return bDAO.getImage(fileName);
	}

	@GetMapping("/board.delete")
	public String boardDelete(@RequestParam(name = "boardID") int boardID, HttpServletRequest req, Board b) {
		req.setAttribute("menuPage", "menu");
		utg.generate(req);
		bDAO.deleteBoard(req, b);
		bDAO.writeGo(req, 1);
		return "board/board";
	}

	@PostMapping("/board.Search")
	public String searchBoard(HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		utg.generate(req);
		bDAO.searchPost(req);
		bDAO.writeGo(req, 1);
		return "board/board";
	}

	@PostMapping("/write.update.do")
	public String writeUpdateDo(HttpServletRequest req, Board b, BoardImage boardImage,
			@RequestParam(name = "boardImageFile") MultipartFile mfImage,
			@RequestParam(name = "boardFile") MultipartFile mfFile, BoardFile boardFile) {
		bDAO.writeUpdateDo(req, b, boardImage, mfImage, mfFile, boardFile);
		System.out.println(boardImage.getBoardImageUrl());
		bDAO.writeShow(req, b);
		return "board/readboard";
	}

	@GetMapping("/board/readboard")
	public String readBoard(HttpServletRequest req, @RequestParam(name = "boardID") int boardID, Board b) {
		b.setBoardID(boardID);
		bDAO.writeShow(req, b);
		return "board/readboard";

	}

	@GetMapping("/write.show")
	public String writeShow(HttpServletRequest req, Board b) {
		bDAO.writeShow(req, b);
		return "board/readboard";
	}

	@GetMapping("/board.post.reply.write")
	public String boardPostCommentWrite(BoardComments bpc, Board b, HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			bDAO.writePostReply(bpc, req, b);
			utg.generate(req);
			bDAO.writeShow(req, b);
		}
		return "board/readboard";
	}

	@GetMapping("/board.post.reply.delete")
	public String boardPostCommentDelete(BoardComments bpc, Board b, HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			bDAO.deletePostReply(bpc, req);
			utg.generate(req);
			bDAO.writeShow(req, b);
		}
		return "board/readboard";
	}

	@GetMapping("/board.post.reply22.write")
	public String boardPostCO2CommentWrite(BoardCOComments bpcoc, Board b, HttpServletRequest req, BoardComments bpc) {
		if (uDAO.islogin(req)) {
			bDAO.writePostReply22(bpcoc, req, b, bpc);
			utg.generate(req);
			bDAO.writeShow(req, b);
		}
		return "board/readboard";
	}

	@GetMapping("/board.post.reply22.delete")
	public String boardPostCO2CommentDelete(BoardCOComments bpcoc, Board b, HttpServletRequest req, BoardComments bpc) {
		if (uDAO.islogin(req)) {
			bDAO.deletePostReply22(bpcoc, req);
			utg.generate(req);
			bDAO.writeShow(req, b);
		}
		return "board/readboard";
	}

}