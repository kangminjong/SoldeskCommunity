package com.ungman.sc.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ungman.sc.UngmanDateManager;
import com.ungman.sc.UngmanTokenGenerator;
import com.ungman.sc.admin.AdminDAO;
import com.ungman.sc.admin.classIds;
import com.ungman.sc.auth.EmailService;
import com.ungman.sc.auth.VerificationCodeRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	private final EmailService emailService;
	private final VerificationCodeRepository verificationCodeRepository;
	private final UserService uDAO;
	private final UserRepository userRepository;

	@Autowired
	private AdminDAO aDAO;

	@Autowired
	public UserController(EmailService emailService, VerificationCodeRepository verificationCodeRepository,
			UserService userService, UserRepository userRepository) {
		this.emailService = emailService;
		this.verificationCodeRepository = verificationCodeRepository;
		this.uDAO = userService;
		this.userRepository = userRepository;
	}

	@Autowired
	private UngmanTokenGenerator utg;

	// 로그인 페이지로 이동
	@GetMapping("/login.go")
	public String loginGo() {

		return "user/login";
	}

	@PostMapping("/login.do")
	public String loginDo(HttpServletRequest req, User u) {
		req.setAttribute("menuPage", "menu");
		uDAO.login(req, u);
		uDAO.islogin(req);
		utg.generate(req);
		aDAO.adminClassId(req);
		return "index";
	}

	// 회원가입 페이지로 이동
	@GetMapping("/join.go")
	public String joinGo(HttpServletRequest req) {
		UngmanDateManager.getCurYear(req);
		return "user/signup";
	}

	// 이메일 찾기 페이지로 이동
	@GetMapping("/findEmail.go")
	public String findEmailGo() {
		return "user/findEmail";
	}

	// 이메일 찾기 처리
	@PostMapping("/findEmail.do")
	public String findEmailDo(HttpServletRequest req, @RequestParam("name") String name,
			@RequestParam("tel") String tel) {
		uDAO.findUserEmail(req, name, tel);
		return "user/foundEmail";
	}

	// 비밀번호 찾기 페이지로 이동
	@GetMapping("/findpw.go")
	public String findpwGo() {
		return "user/findpassword";
	}

	@GetMapping("/repw.go")
	public String repwGo(@RequestParam("email") String email, Model model) {
		model.addAttribute("email", email); // 이메일 전달
		return uDAO.findUserAndRedirect(email, model); // 서비스 호출
	}

	@PostMapping("/repw.do")
	public String repwDo(HttpServletRequest req, @RequestParam("email") String email,
			@RequestParam("pw") String newpw) {
		uDAO.resetPw(req, email, newpw);
		return "user/login"; // 비밀번호 재설정 후 로그인 페이지로 이동
	}

	@GetMapping("/logout.do")
	public String logoutDo(HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		aDAO.adminClassId(req);
		uDAO.logout(req);
		uDAO.islogin(req);
		return "index";
	}

	@PostMapping("/join")
	public String joinDo(@RequestParam(name = "photoTemp") MultipartFile mf, User u, HttpServletRequest req) {
		aDAO.adminClassId(req);
		req.setAttribute("menuPage", "menu");
		uDAO.join(mf, u, req);
		uDAO.islogin(req);
		return "index";
	}

	// 사용자 정보 반환
	@GetMapping("/user-info")
	public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal User user) {
		Map<String, Object> response = new HashMap<>();
		if (user != null) {
			String email = user.getEmail();
			Integer classId = uDAO.getClassIdByEmail(email);
			response.put("email", email);
			response.put("classId", classId);
			return ResponseEntity.ok(response);
		} else {
			response.put("error", "User not authenticated");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}

	@GetMapping("/info.go")
	public String infoGo(HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			uDAO.splitAddress(req);
		}
		return "user/mypage";
	}

	@PostMapping("/user.update.do")
	public String userUpdateDo(@RequestParam(name = "photoTemp") MultipartFile mf, User u, HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			aDAO.adminClassId(req);
			uDAO.update(mf, u, req);
			uDAO.splitAddress(req);
			req.setAttribute("menuPage", "menu");
		}
		return "index";
	}

	@GetMapping("/bye.do")
	public String byeDo(HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			aDAO.adminClassId(req);
			req.setAttribute("menuPage", "menu");
			uDAO.bye(req);
			uDAO.logout(req);
			uDAO.islogin(req);
		}
		return "index";
	}

	@GetMapping("/user/{fileName}")
	public @ResponseBody Resource getImage(@PathVariable(name = "fileName") String fileName) {
		return uDAO.getImage(fileName);
	}
	
	
	
	@GetMapping(value = "/UserEmail.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Users tkdGet(@RequestParam("email") String email,
			@RequestParam("pw") String pw, HttpServletResponse res) {
		return uDAO.toJson(email, pw);
	}
}