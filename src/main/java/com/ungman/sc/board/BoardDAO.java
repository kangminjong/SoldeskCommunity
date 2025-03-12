package com.ungman.sc.board;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanFileNameTokenGenerator;
import com.ungman.sc.user.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BoardDAO {

	private int postPerPage;

	@Value("${board.img.folder}")
	private String boardImgFolder;

	@Value("${board.file.folder}")
	private String boardFileFolder;

	@Value("${photo.maxSize}")
	private int photoMaxSize;

	@Value("${file.maxSize}")
	private int fileMaxSize;
	@Autowired
	private BoardRepository br;

	@Autowired
	private BoardCommentsRepo bpcr;

	@Autowired
	private BoardCOCommentsRepo bcocr;

	@Autowired
	private BoardImageRepository bir;

	@Autowired
	private WriteBoardRepository wbr;

	@Autowired
	private BoardFileRepository bfr;

	public BoardDAO() {
		postPerPage = 6;
	}

	public Resource getImage(String fileName) {
		try {
			return new UrlResource("file:" + boardImgFolder + "/" + fileName);
		} catch (Exception e) {
			return null;
		}
	}

	public ResponseEntity<Resource> getFile(String fileName) {
		try {
			UrlResource ur = new UrlResource("file:" + boardFileFolder + "/" + fileName);
			// attachment; filename="$2A.png"
			String h = "attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8") + "\"";
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, h).body(ur);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void writeDo(HttpServletRequest req, Board b, BoardImage boardImage, MultipartFile mfImage,
			MultipartFile mfFile, BoardFile boardFile) {
		String imageName = boardImageWriteDo(mfImage);
		String fileName = boardFileWriteDo(mfFile);

		try {
			User u = (User) req.getSession().getAttribute("login");
			String token = req.getParameter("token");
			String oldSuccessToken = (String) req.getSession().getAttribute("successToken");

			if (oldSuccessToken != null && token.equals(oldSuccessToken)) {
				req.setAttribute("result", "등록실패(새로고침)");
				return;
			}

			b.setContent(b.getContent().replace("\r\n", "<br>"));
			b.setUserEmail(u);
			String checkBoxValue = req.getParameter("anonymous");
			String hideName = (checkBoxValue != null && checkBoxValue.equals("true")) ? "Y" : "N";
			b.setHideName(hideName);
			b.setCommentCount(0);
			br.save(b);

			br.flush();
			req.getSession().setAttribute("successToken", token);

			if (imageName != null) {
				boardImage.setBoardImageUrl(imageName);
				boardImage.setBoardID(b);
				bir.save(boardImage);

			}
			if (fileName != null) {
				boardFile.setBoardFileUrl(fileName);
				boardFile.setBoardID(b);
				bfr.save(boardFile);

			}
		} catch (Exception e) {
			req.setAttribute("result", "등록실패");
		}
		req.setAttribute("result", "등록성공");

	}

	public String boardImageWriteDo(MultipartFile mf) {
		if (mf != null && !mf.isEmpty()) {
			try {
				if (mf.getSize() > photoMaxSize) {
					throw new Exception();
				}

				String fileName = UngmanFileNameTokenGenerator.generate(mf);
				mf.transferTo(new File(boardImgFolder + "/" + fileName));
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return null;

	}

	public String boardFileWriteDo(MultipartFile mf) {
		if (mf != null && !mf.isEmpty()) {
			try {
				if (mf.getSize() > photoMaxSize) {
					throw new Exception();
				}
				String fileName = UngmanFileNameTokenGenerator.generate(mf);
				mf.transferTo(new File(boardFileFolder + "/" + fileName));
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return null;

	}

	public void writeGo(HttpServletRequest req, int page) {
		try {
			List<Board> posts;
			Sort s = Sort.by(Sort.Order.desc("boardDate"));
			String searchTxt = (String) req.getSession().getAttribute("searchTxt");
			long postCount = br.count();
			if (searchTxt == null) {
				searchTxt = "";
			} else {
				postCount = br.countByTitleContainingOrContentContainingOrHideNameAndUserEmailNameContaining(searchTxt ,searchTxt, "N", searchTxt);
			}
			int pageCount = (int) Math.ceil((double) postCount / postPerPage);
			PageRequest pg = PageRequest.of(page - 1, postPerPage, s);

			posts = br.findByTitleContainingOrContentContainingOrHideNameAndUserEmailNameContaining(pg, searchTxt ,searchTxt, "N", searchTxt);

			req.setAttribute("pageCount", pageCount);
			req.setAttribute("curPage", page);

			req.setAttribute("bp", posts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeShow(HttpServletRequest req, Board b) {
		Optional<Board> posts = wbr.findById(b.getBoardID());
		Board dbBoard = posts.get();
		req.setAttribute("showdbBoard", dbBoard);

		BoardImage images = bir.findByboardID(b);

		req.setAttribute("imagedbBoard", images);

		BoardFile files = bfr.findByboardID(b);

		req.setAttribute("filedbBoard", files);

	}

	public void writeUpdateDo(HttpServletRequest req, Board b, BoardImage boardImage, MultipartFile mfImage,
			MultipartFile mfFile, BoardFile boardFile) {
		User u = (User) req.getSession().getAttribute("login");
		// 현재 작성되어있는 값들
		b.setContent(b.getContent().replace("\r\n", "<br>"));
		b.setCommentCount(0);
		String checkBoxValue = req.getParameter("anonymous");
		String hideName = (checkBoxValue != null && checkBoxValue.equals("true")) ? "Y" : "N";
		b.setHideName(hideName);
		b.setUserEmail(u);
		String imageName = boardImageWriteDo(mfImage);
		String fileName = boardFileWriteDo(mfFile);

		br.save(b);
		br.flush();
		if (imageName != null) {

			boardImage.setBoardImageUrl(imageName);
			boardImage.setBoardID(b);
			bir.save(boardImage);
		}
		if (fileName != null) {
			boardFile.setBoardFileUrl(fileName);
			boardFile.setBoardID(b);
			bfr.save(boardFile);

		}
	}

	public void deleteBoard(HttpServletRequest req, Board b) {
		try {
			BoardImage boardImage = bir.findByboardID(b);
			if (boardImage != null) {

				new File(boardImgFolder + "/" + boardImage.getBoardImageUrl()).delete();
			}

			BoardFile boardFile = bfr.findByboardID(b);
			if (boardFile != null) {
				new File(boardFileFolder + "/" + boardFile.getBoardFileUrl()).delete();
			}

			br.deleteById(b.getBoardID());
			req.setAttribute("result", "글삭제성공");
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("result", "글삭제실패..");
		}
	}

	public void searchPost(HttpServletRequest req) {
		String searchTxt = req.getParameter("searchTxt");
		req.getSession().setAttribute("searchTxt", searchTxt);
	}

	public void clearSearch(HttpServletRequest req) {
		req.getSession().setAttribute("searchTxt", null);
	}

	public void writePostReply(BoardComments bpc, HttpServletRequest req, Board b) {
		try {
			String token = req.getParameter("token");
			if (token == null) {
				token = UUID.randomUUID().toString();
				req.getSession().setAttribute("successToken", token);
			}
			String lastSuccessToken = (String) req.getSession().getAttribute("successToken");
			if (lastSuccessToken != null && token.equals(lastSuccessToken)) {
				req.setAttribute("result", "댓글쓰기 실패(새로고침)");
				return;
			}

			User u = (User) req.getSession().getAttribute("login");
			bpc.setUserEmail(u);
			bpc.setBoard(b);

			String checkBoxValue = req.getParameter("anonymous");
			String hideName = (checkBoxValue != null && checkBoxValue.equals("true")) ? "Y" : "N";
			bpc.setHideName(hideName);
			bpcr.save(bpc);
			req.setAttribute("result", "댓글쓰기 성공");
			req.getSession().setAttribute("successToken", token);

		} catch (Exception e) {
			req.setAttribute("result", "댓글쓰기 실패");
		}
	}

	public void deletePostReply(BoardComments bpc, HttpServletRequest req) {
		try {
			BoardComments comment = bpcr.findById(bpc.getCommentId()).orElseThrow();
			comment.setContent("삭제된 댓글입니다.");
			bpcr.save(comment);
			req.setAttribute("result", "댓글 삭제 성공");
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("result", "댓글 삭제 실패");
		}
	}

	public void writePostReply22(BoardCOComments bpcoc, HttpServletRequest req, Board b, BoardComments bpc) {
		try {
			String token = req.getParameter("token");
			if (token == null) {
				token = UUID.randomUUID().toString();
				req.getSession().setAttribute("successToken", token);
			}
			String lastSuccessToken = (String) req.getSession().getAttribute("successToken");
			if (lastSuccessToken != null && token.equals(lastSuccessToken)) {
				req.setAttribute("result", "대댓글쓰기 실패(새로고침)");
				return;
			}

			User u = (User) req.getSession().getAttribute("login");
			Optional<BoardComments> comments = bpcr.findBycommentId(bpc.getCommentId());
			if (comments.isPresent()) {
				BoardComments dbBoardComments = comments.get();
				bpcoc.setCommentN(dbBoardComments);
			} else {
				req.setAttribute("result", "부모 댓글을 찾을 수 없습니다.");
				return;
			}
			bpcoc.setUserEmail(u);
			bpcoc.setBoard(b);

			String checkBoxValue = req.getParameter("anonymous");
			String hideName = (checkBoxValue != null && checkBoxValue.equals("true")) ? "Y" : "N";
			bpcoc.setHideName(hideName);
			bcocr.save(bpcoc);

			req.setAttribute("result", "대댓글쓰기 성공");
			req.getSession().setAttribute("successToken", token);

		} catch (Exception e) {
			req.setAttribute("result", "대댓글쓰기 실패");
			e.printStackTrace();
		}
	}

	public void deletePostReply22(BoardCOComments bpcoc, HttpServletRequest req) {
		try {
			bcocr.delete(bpcoc);
			req.setAttribute("result", "대댓글 삭제 성공");
		} catch (Exception e) {
			req.setAttribute("result", "대댓글 삭제 실패");
		}
	}
}
