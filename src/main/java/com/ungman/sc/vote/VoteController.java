package com.ungman.sc.vote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ungman.sc.UngmanTokenGenerator;
import com.ungman.sc.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class VoteController {

	private final VoteService voteService;
	private final UserService uDAO;

	@Autowired
	private UngmanTokenGenerator utg;

	@Autowired
	public VoteController(VoteService voteService, UserService uDAO) {
		this.voteService = voteService;
		this.uDAO = uDAO;
	}

	// 투표 목록 조회
	@GetMapping("/votelist.go")
	public String voteLists(Model model, HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		if (uDAO.islogin(req)) {
			voteService.getVoteList(model, req);
			return "vote/votelist";
		}
		return "redirect:/login.go";
	}

	// 투표 상세 조회
	@GetMapping("/vote/{voteId}")
	public String voteDetail(@PathVariable("voteId") Integer voteId, Model model, HttpServletRequest req) {
		if (uDAO.islogin(req)) {
			voteService.getVoteDetail(voteId, model);
			return "vote/vote";
		}
		return "redirect:/login.go";
	}

	// 투표 생성 페이지로 이동
	@GetMapping("/votecreate.go")
	public String createVotePage(HttpServletRequest req) {
		utg.generate(req);
		if (uDAO.islogin(req)) {
			return "vote/createvote";
		}
		return "redirect:/login.go";
	}

	// 투표 생성 처리
	@PostMapping("/votecreate.do")
	public String createVote(HttpServletRequest request, Vote vote, @RequestParam("choice") List<String> choices,
			Model model, HttpServletRequest req) {
		utg.generate(req);
		req.setAttribute("menuPage", "menu");
		if (uDAO.islogin(request)) {
			voteService.createVote(request, vote, choices, model);
			voteService.getVoteList(model, req);
			return "vote/createvote";
		}
		return "redirect:/login.go";
	}

	// 투표 제출 처리 (다중 선택)
	@PostMapping("/vote/{voteId}/submit")
	public String submitVote(HttpServletRequest request, @PathVariable("voteId") Integer voteId,
			@RequestParam(value = "choices", required = false) List<Integer> choices,
			RedirectAttributes redirectAttributes) {
		if (!uDAO.islogin(request)) {
			return "redirect:/login.go";
		}

		// 투표 제출 처리 및 메시지 설정
		String resultMessage = voteService.submitVote(request, voteId, choices);
		redirectAttributes.addFlashAttribute("result", resultMessage);

		// 투표 결과 페이지로 리디렉션
		return "redirect:/vote/" + voteId;
	}

	@GetMapping("/vote/{voteId}/result")
	public String voteResult(@PathVariable("voteId") Integer voteId, Model model, HttpServletRequest req) {
		// 로그인 여부 체크
		if (uDAO.islogin(req)) {
			// 투표 결과 조회 메서드 호출
			voteService.getVoteResult(voteId, model);

			// 결과가 없으면 "아직 투표 결과가 없습니다." 메시지 출력
			if (model.containsAttribute("result") && model.getAttribute("result") == null) {
				return "vote/result"; // 결과 페이지로 리턴
			}

			return "vote/result"; // 결과 페이지로 리턴
		}
		return "redirect:/login.go"; // 로그인 안 된 경우 로그인 페이지로 리다이렉트
	}

	@GetMapping("/vote/votice.delete")
	public String deleteVotice(Model model, @RequestParam("voteId") int voteId, Vote v, HttpServletRequest req) {
		req.setAttribute("menuPage", "menu");
		voteService.voteDelete(v);
		voteService.getVoteList(model, req);
		return "vote/votelist";
	}
}
