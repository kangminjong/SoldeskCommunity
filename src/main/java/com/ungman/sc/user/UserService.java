package com.ungman.sc.user;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanFileNameTokenGenerator;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

	private BCryptPasswordEncoder bcpe;
	private SimpleDateFormat sdf;

	@Autowired
	private UserRepository ur;

	@Value("${user.photo.folder}")
	private String userPhotofolder;

	@Value("${photo.maxSize}")
	private int photoSize;
   
	public UserService() {
		bcpe = new BCryptPasswordEncoder();
		sdf = new SimpleDateFormat("yyyyMMdd");

	}

	public Resource getImage(String fileName) {
		try {
			return new UrlResource("file:" + userPhotofolder + "/" + fileName);
		} catch (Exception e) {
			return null;
		}
	}

	public void splitAddress(HttpServletRequest req) {
		User u = (User) req.getSession().getAttribute("login");
		String addr = u.getAddr();
		String[] addrAR = addr.split("!");

		req.setAttribute("addr1", addrAR[0]);
		req.setAttribute("addr2", addrAR[1]);
		req.setAttribute("addr3", addrAR[2]);
	}

	public void join(MultipartFile mf, User u, HttpServletRequest req) {
		String fileName = null;
		try {
			if (mf.getSize() > photoSize) {
				throw new Exception();
			}
			fileName = UngmanFileNameTokenGenerator.generate(mf);
			mf.transferTo(new File(userPhotofolder + "/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("result", "가입 실패(파일)");
			return;
		}
		try {
			u.setPw(bcpe.encode(u.getPw()));
			String year = req.getParameter("y");
			String month = req.getParameter("m");
			String day = req.getParameter("d");

			// 생일 정보 체크
			if (year == null || month == null || day == null) {
				throw new Exception("생일 정보가 누락되었습니다.");
			}

			int monthInt = Integer.parseInt(month);
			int dayInt = Integer.parseInt(day);
			String bd = String.format("%s%02d%02d", year, monthInt, dayInt);

			System.out.println("생일 포맷: " + bd); // 생일 정보 확인용 로그
			u.setBirthday(sdf.parse(bd)); // 생일 파싱

			// 전화번호 체크
			String tel = req.getParameter("tel");
			if (tel == null || tel.trim().isEmpty()) {
				throw new Exception("전화번호가 누락되었습니다.");
			}
			u.setTel(tel); // 전화번호 설정

			// 주소 설정
			String addr = req.getParameter("addr2") + "!" + req.getParameter("addr3") + "!" + req.getParameter("addr1");
			u.setAddr(addr);

			// 클래스 ID 체크
			String classIdParam = req.getParameter("classId");
			if (classIdParam == null || classIdParam.trim().isEmpty()) {
				throw new Exception("클래스 ID가 누락되었습니다.");
			}

			int classID = Integer.parseInt(classIdParam);
			u.setClassId(classID); // 클래스 ID 설정
			
			String roleValue = req.getParameter("role");
			String role = (roleValue != null && roleValue.equals("true")) ? "Y" : "N";
			u.setRole(role);
			
			String isDeletedValue = req.getParameter("isDeleted");
			String isDeleted = (isDeletedValue != null && isDeletedValue.equals("true")) ? "Y" : "N";
			u.setIsDeleted(isDeleted);

			// 파일 처리
			u.setPhoto(fileName);

			// ID 중복 체크
			if (ur.existsById(u.getEmail())) {
				throw new Exception("이미 존재하는 ID입니다.");
			}

			// 사용자 저장
			ur.save(u);
			req.setAttribute("result", "가입 성공");

		} catch (Exception e) {
			System.out.println("회원가입 실패(DB): " + e.getMessage()); // 오류 메시지 출력
			req.setAttribute("result", "가입 실패(DB)");
			if (fileName != null) {
				new File(userPhotofolder + "/" + fileName).delete(); // 파일 삭제
			}
		}
	}

	public void login(HttpServletRequest req, User u) {
		try {
			Optional<User> userTemp = ur.findById(u.getEmail());
			
			if (userTemp.isPresent()) {
				User dbUser = userTemp.get();
				if (bcpe.matches(u.getPw(), dbUser.getPw())) {
					req.setAttribute("result", "로그인성공");
					req.getSession().setAttribute("login", dbUser);
				} else {
					req.setAttribute("result", "로그인실패(비밀번호)");
				}
			} else {
				req.setAttribute("result", "로그인실패(미가입ID)");
			}
		} catch (Exception e) {
			e.printStackTrace();

			req.setAttribute("result", "로그인실패");
		}
	}

	public Boolean islogin(HttpServletRequest req) {
		if (req.getSession().getAttribute("login") != null) {
			req.setAttribute("loginPage", "user/logined");
			return true;
		} else {
			req.setAttribute("loginPage", "loginIndex");
			return false;
		}
	}

	public void logout(HttpServletRequest req) {
		req.getSession().setAttribute("login", null);
	}

	public void update(MultipartFile mf, User u, HttpServletRequest req) {
		User login = (User) req.getSession().getAttribute("login");
		String oldFile = login.getPhoto();
		String newFile = null;
		try {
			if (mf.getSize() > photoSize) {
				throw new Exception();
			}
			newFile = UngmanFileNameTokenGenerator.generate(mf);
			mf.transferTo(new File(userPhotofolder + "/" + newFile));
		} catch (StringIndexOutOfBoundsException e) {
			newFile = oldFile;
		} catch (Exception e) {
			req.setAttribute("result", "수정 실패(파일)");
			return;
		}

		try {
			login.setPw(bcpe.encode(u.getPw()));
			login.setName(u.getName());
			login.setTel(u.getTel());
			String addr = req.getParameter("addr2") + "!" + req.getParameter("addr3") + "!" + req.getParameter("addr1");
			login.setAddr(addr);
			login.setPhoto(newFile);

			ur.save(login);

			req.setAttribute("result", "수정 성공");
			req.getSession().setAttribute("login", login);
			if (!newFile.equals(oldFile)) {
				new File(userPhotofolder + "/" + oldFile).delete();
			}
		} catch (Exception e) {
			req.setAttribute("result", "수정 실패(DB)");
			if (!newFile.equals(oldFile)) {
				new File(userPhotofolder + "/" + newFile).delete();
			}
		}
	}

	public void bye(HttpServletRequest req) {
		try {
			System.out.println("탈퇴성공");
			User u = (User) req.getSession().getAttribute("login");
			System.out.println(u);
			ur.delete(u);
			req.setAttribute("result", "탈퇴 성공");
			new File(userPhotofolder + "/" + u.getPhoto()).delete();
		} catch (Exception e) {
			
			req.setAttribute("result", "탈퇴 실패");
		}
	}
	
	public String findUserAndRedirect(String email, Model model) {
	    try {
	        if (ur.findByEmail(email).isPresent()) {
	            return "user/resetpw"; // 이메일 존재 -> 비밀번호 재설정 페이지로 이동
	        } else {
	            model.addAttribute("alertMessage", "이메일을 찾을 수 없습니다.");
	            return "user/findpassword"; // 이메일 없음 -> 비밀번호 찾기 페이지로 이동
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("alertMessage", "오류가 발생했습니다.");
	        return "user/findpassword"; // 예외 발생 시에도 비밀번호 찾기 페이지로 이동
	    }
	}

	public void resetPw(HttpServletRequest req, String email, String newPw) {
	    try {
	        // 이메일로 유저 찾기
	        Optional<User> userTemp = ur.findByEmail(email);

	        if (userTemp.isPresent()) {
	            User dbUser = userTemp.get();
	            
	            // 비밀번호 암호화
	            String encodedPassword = bcpe.encode(newPw);
	            dbUser.setPw(encodedPassword);

	            // 비밀번호 업데이트
	            ur.save(dbUser);
	            req.setAttribute("result", "비밀번호 재설정 성공");
	        } else {
	            req.setAttribute("result", "이메일을 찾을 수 없습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        req.setAttribute("result", "비밀번호 재설정 실패");
	    }
	}


	
	public void findUserEmail(HttpServletRequest req, String name, String tel) {
	    try {
	        if (name == null || name.trim().isEmpty() || tel == null || tel.trim().isEmpty()) {
	            req.setAttribute("result", "이름과 전화번호를 모두 입력해 주세요.");
	            return;
	        }

	        Optional<String> emailOpt = ur.findEmailByNameAndTel(name, tel);

	        if (emailOpt.isPresent()) {
	            req.setAttribute("result", "회원님의 아이디는 " + emailOpt.get() + "입니다.");
	        } else {
	            req.setAttribute("result", "해당 정보로 가입된 이메일을 찾을 수 없습니다.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        req.setAttribute("result", "아이디 찾기 중 오류가 발생했습니다.");
	    }
	}



	public Users toJson(User u) {
		Optional<User> userTemp = ur.findByEmail(u.getEmail());
		List<User> user = new ArrayList<>();
		if (userTemp.isPresent()) {
			user.add(userTemp.get());
		}

		return new Users(user);

	}
	// 이메일로 classId 찾기
	   public Integer getClassIdByEmail(String email) {
	       return ur.findClassIdByEmail(email);
	   }
	   
	   public Users toJson(String email, String pw) {
			List<User> userList = new ArrayList<>();
			Optional<User> userEmailTemp = ur.findByEmail(email);
			System.out.println(email);
			if (userEmailTemp.isPresent()) {
				User user = userEmailTemp.get();
				if (bcpe.matches(pw, user.getPw())) {
					userList.add(user);
				}
			}
			return new Users(userList);
		}


}