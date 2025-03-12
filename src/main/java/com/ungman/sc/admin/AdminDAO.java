package com.ungman.sc.admin;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.ungman.sc.board.Board;
import com.ungman.sc.board.BoardRepository;
import com.ungman.sc.user.User;
import com.ungman.sc.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AdminDAO {

	@Autowired
	private UserRepository ur;

	@Autowired
	private BoardRepository br;

	@Autowired
	private NoticeRepository nr;

	private int postPerPage;

	@Autowired
	private NoticeImageRepository nir;

	@Autowired
	private NoticeFileRepository nfr;

	@Value("${notice.img.folder}")
	private String noticeImgFolder;

	@Value("${photo.maxSize}")
	private int photoMaxSize;

	@Value("${notice.file.folder}")
	private String noticeFileFolder;

	@Value("${file.maxSize}")
	private int fileMaxSize;

	@Autowired
	private ClassIdRepository cRepository;

	public AdminDAO() {
		postPerPage = 6;
	}

	public Resource getImage(String fileName) {
		try {
			return new UrlResource("file:" + noticeImgFolder + "/" + fileName);
		} catch (Exception e) {
			return null;
		}
	}

	public ResponseEntity<Resource> getFile(String fileName) {
		try {
			UrlResource ur = new UrlResource("file:" + noticeFileFolder + "/" + fileName);
			// attachment; filename="$2A.png"
			String h = "attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8") + "\"";
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, h).body(ur);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isAdmin(HttpServletRequest req) {
		User user = (User) req.getSession().getAttribute("login");
		return user != null && "Y".equals(user.getRole());
	}

	// 회원 목록 조회
	public void getUsers(HttpServletRequest req) {
		List<User> users = ur.findAll();
		req.setAttribute("users", users);
	}

	// 회원 삭제
	public void deleteUser(String email, HttpServletRequest req) {
		try {
			ur.findByEmail(email).ifPresentOrElse(user -> {
				// 해당 사용자가 작성한 게시물이 있는지 확인
				List<Board> boards = br.findByUserEmailIsDeletedNot("Y"); // 게시물 중 삭제되지 않은 것만 조회
				boolean hasBoards = boards.stream().anyMatch(board -> board.getUserEmail().getEmail().equals(email));
				
				if (hasBoards) {
					// 게시물이 있으면 isDeleted를 "Y"로 설정
					user.setIsDeleted("Y");
					ur.save(user); // 변경된 유저 정보를 저장
					req.setAttribute("result", "회원 삭제 성공, 게시물은 남아있습니다.");
				} else {
					// 게시물이 없으면 유저 삭제
					ur.delete(user);
					req.setAttribute("result", "회원 삭제 성공");
				}
			}, () -> req.setAttribute("result", "회원이 존재하지 않습니다."));
		} catch (Exception e) {
			req.setAttribute("result", "회원 삭제 실패");
		}
	}

	// 게시물 목록 조회
	public void getBoards(HttpServletRequest req) {
		List<Board> boards = br.findAll();
		req.setAttribute("boards", boards);
	}

	// 게시물 삭제
	public void deleteBoard(Integer boardId, HttpServletRequest req) {
		try {
			Board board = br.findById(boardId).orElse(null);
			if (board != null) {
				br.delete(board);
				req.setAttribute("result", "게시물 삭제 성공");
			} else {
				req.setAttribute("result", "게시물이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			req.setAttribute("result", "게시물 삭제 실패");
		}
	}

	public void noticeGo(HttpServletRequest req, int page) {
		try {
			List<Notice> posts;
			Sort s = Sort.by(Sort.Order.desc("noticeDate"));
			String searchTxt = (String) req.getSession().getAttribute("searchTxt");
			long postCount = nr.count();
			if (searchTxt == null) {
				searchTxt = "";
			} else {
				postCount = nr.countByNoticeTitleContainingOrNoticeContentContaining(searchTxt, searchTxt);
			}
			int pageCount = (int) Math.ceil((double) postCount / postPerPage);
			PageRequest pg = PageRequest.of(page - 1, postPerPage, s);

			posts = nr.findByNoticeTitleContainingOrNoticeContentContaining(pg, searchTxt, searchTxt);

			req.setAttribute("pageCount", pageCount);
			req.setAttribute("curPage", page);

			req.setAttribute("np", posts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeShow(HttpServletRequest req, Notice n) {
		Notice notice = nr.findByNoticeID(n.getNoticeID());
		
		req.setAttribute("notices", notice);
		
		NoticeImage noticeImage = nir.findByNoticeID(n);
		
		req.setAttribute("noticeImage", noticeImage);
		NoticeFile noticeFile = nfr.findByNoticeID(n);
		req.setAttribute("noticeFile", noticeFile);

	}

	public void noticeWriteDo(HttpServletRequest req, Notice n, NoticeImage noticeImage, NoticeFile noticeFile,
			MultipartFile mfImage, MultipartFile mfFile) {
		String imageName = noticeImageWriteDo(mfImage);
		String fileName = noticeFileWriteDo(mfFile);

		try {
			User u = (User) req.getSession().getAttribute("login");
			String token = req.getParameter("token");
			String oldSuccessToken = (String) req.getSession().getAttribute("successToken");

			if (oldSuccessToken != null && token.equals(oldSuccessToken)) {
				req.setAttribute("result", "등록실패(새로고침)");
				return;
			}

			n.setNoticeContent(n.getNoticeContent().replace("\r\n", "<br>"));
			System.out.println(n.getNoticeContent());
			nr.save(n);
			req.setAttribute("result", "등록성공");
			nr.flush();
			req.getSession().setAttribute("successToken", token);

			if (imageName != null) {
				noticeImage.setNoticeImageUrl(imageName);
				noticeImage.setNoticeID(n);
				nir.save(noticeImage);

			}
			if (fileName != null) {
				noticeFile.setNoticeFileUrl(fileName);
				noticeFile.setNoticeID(n);
				nfr.save(noticeFile);

			}
		} catch (Exception e) {
			e.printStackTrace();

			req.setAttribute("result", "등록실패");
		}

	}

	public String noticeImageWriteDo(MultipartFile mf) {
		if (mf != null && !mf.isEmpty()) {
			try {
				if (mf.getSize() > photoMaxSize) {
					throw new Exception();
				}

				String fileName = UngmanFileNameTokenGenerator.generate(mf);
				mf.transferTo(new File(noticeImgFolder + "/" + fileName));
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return null;

	}

	public String noticeFileWriteDo(MultipartFile mf) {
		if (mf != null && !mf.isEmpty()) {
			try {
				if (mf.getSize() > fileMaxSize) {
					throw new Exception();
				}
				String fileName = UngmanFileNameTokenGenerator.generate(mf);
				mf.transferTo(new File(noticeFileFolder + "/" + fileName));
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return null;

	}

	public void deleteNotice(HttpServletRequest req, Notice n) {
		try {
			NoticeImage noticeImage = nir.findByNoticeID(n);

			NoticeFile noticeFile = nfr.findByNoticeID(n);

			if (noticeImage != null) {

				new File(noticeImgFolder + "/" + noticeImage.getNoticeImageUrl()).delete();
			}
			if (noticeFile != null) {

				new File(noticeFileFolder + "/" + noticeFile.getNoticeFileUrl()).delete();
			}

			nr.deleteById(n.getNoticeID());
			req.setAttribute("result", "글삭제성공");
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("result", "글삭제실패..");
		}
	}

	public void noticeUpdateDo(HttpServletRequest req, Notice n, NoticeImage noticeImage, NoticeFile noticeFile,
			MultipartFile mfImage, MultipartFile mfFile) {
		User u = (User) req.getSession().getAttribute("login");
		// 현재 작성되어있는 값들
		n.setNoticeContent(n.getNoticeContent().replace("\r\n", "<br>"));
		String imageName = noticeImageWriteDo(mfImage);
		String fileName = noticeFileWriteDo(mfFile);

		nr.save(n);
		nr.flush();
		if (imageName != null) {
			noticeImage.setNoticeImageUrl(imageName);
			noticeImage.setNoticeID(n);
			nir.save(noticeImage);
			nir.flush();
		}
		if (fileName != null) {
			noticeFile.setNoticeFileUrl(fileName);
			noticeFile.setNoticeID(n); 
			nfr.save(noticeFile);
			nfr.flush();
		}
	}

	public void adminClassId(HttpServletRequest req) {
		List<ClassId> classIds = cRepository.findAllByOrderByClassIdAsc();
		req.setAttribute("cls", classIds);
	}

	public void adminClassAppend(HttpServletRequest req, ClassId classId) {
		try {

			String token = req.getParameter("token");
			String oldSuccessToken = (String) req.getSession().getAttribute("successToken");

			if (oldSuccessToken != null && token.equals(oldSuccessToken)) {
				req.setAttribute("result", "등록실패(새로고침)");
				return;
			}
			req.getSession().setAttribute("successToken", token);
			cRepository.save(classId);
		} catch (Exception e) {
			req.setAttribute("result", "등록실패");
		}
		req.setAttribute("result", "등록성공");

	}

	public void adminClassDelete(HttpServletRequest req, ClassId classId) {
		cRepository.deleteById(classId.getClassId());
	}

	public void searchNotice(HttpServletRequest req) {
		String searchTxt = req.getParameter("searchTxt");
		req.getSession().setAttribute("searchTxt", searchTxt);
	}

	public void clearSearch(HttpServletRequest req) {
		req.getSession().setAttribute("searchTxt", null);
	}

	public classIds toJson(Integer classId, String classPw) {
		List<ClassId> classList = new ArrayList<>();
		Optional<ClassId> classIdTemp = cRepository.findByClassId(classId);
		if (classIdTemp.isPresent()) {
			ClassId clId = classIdTemp.get();

			if (clId.getClassPw().equals(classPw)) {
				classList.add(classIdTemp.get());
			}
		}

		return new classIds(classList);
	}

	public void adminNotice(HttpServletRequest req) {
		List<Notice> notice = nr.findAll();
		req.setAttribute("notices", notice);
	}

}
