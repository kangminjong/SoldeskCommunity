package com.ungman.sc.vote;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.ungman.sc.user.UserService;
import com.ungman.sc.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

	private final VoteRepository voteRepository;
	private final VoteListRepository voteListRepository;
	private final UserService uDAO;
	private final VoteUserRepository voteUserRepository;

	@Autowired
	public VoteService(VoteRepository voteRepository, VoteListRepository voteListRepository, UserService uDAO,
			VoteUserRepository voteUserRepository) {
		this.voteRepository = voteRepository;
		this.voteListRepository = voteListRepository;
		this.uDAO = uDAO;
		this.voteUserRepository = voteUserRepository;
	}

	// 투표 목록 조회
	public void getVoteList(Model model, HttpServletRequest req) {
		User u = (User) req.getSession().getAttribute("login");
		List<Vote> voteList = voteRepository.findByClassId(u.getClassId());
		model.addAttribute("voteList", voteList);
	}

	// 투표 상세 조회
	public void getVoteDetail(Integer voteId, Model model) {
		Optional<Vote> vote = voteRepository.findById(voteId);
		if (vote.isPresent()) {
			model.addAttribute("vote", vote.get());
			List<VoteList> voteChoices = voteListRepository.findByVote(vote.get());
			model.addAttribute("voteChoices", voteChoices);
		} else {
			model.addAttribute("result", "투표를 찾을 수 없습니다.");
		}
	}

	public void createVote(HttpServletRequest req, Vote vote, List<String> choices, Model model) {
        try {
            if (!uDAO.islogin(req)) {
                model.addAttribute("result", "로그인 후 투표를 생성할 수 있습니다.");
                return;
            }

            // 로그인된 사용자 정보 가져오기
            User u = (User) req.getSession().getAttribute("login");
            vote.setUserEmail(u.getEmail());
            vote.setClassId(u.getClassId());

            // 다중 선택 여부 체크
            if (!"Y".equals(vote.getMultiChoice()) && !"N".equals(vote.getMultiChoice())) {
                vote.setMultiChoice("N"); // 기본값은 단일 선택
            }

            // 시작일과 종료일이 과거인 경우 처리
            LocalDate startDate = vote.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = vote.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate currentDate = LocalDate.now();
            if (startDate.isBefore(currentDate) || endDate.isBefore(currentDate)) {
                model.addAttribute("result", "시작일과 종료일은 오늘 이후여야 합니다.");
                return;
            }

            // 종료일 시간을 23:59:59로 설정
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 종료일 23:59:59로 설정
            Date endDateWithTime = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // 종료일 업데이트
            vote.setEndDate(endDateWithTime);

            // 투표 저장
            Vote savedVote = voteRepository.save(vote);
            if (savedVote == null) {
                model.addAttribute("result", "투표 생성 실패");
                return;
            }

            // 선택지 저장
            for (String choice : choices) {
                VoteList voteList = new VoteList();
                voteList.setVote(savedVote);  // Vote 설정
                voteList.setChoice(choice);
                voteListRepository.save(voteList);
            }

            model.addAttribute("result", "투표가 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            model.addAttribute("result", "투표 생성 실패: " + e.getMessage());
        }
    }

	@Transactional
	public String submitVote(HttpServletRequest req, Integer voteId, List<Integer> choices) {
		return handleVoteSubmission(req, voteId, choices);
	}

	@Transactional
	public String handleVoteSubmission(HttpServletRequest req, Integer voteId, List<Integer> choices) {
		try {
			if (!uDAO.islogin(req)) {
				return "로그인 후 투표에 참여하세요.";
			}

			User u = (User) req.getSession().getAttribute("login");
			if (u == null) {
				return "로그인 정보가 유효하지 않습니다.";
			}

			Optional<Vote> vote = voteRepository.findById(voteId);
			if (vote.isEmpty()) {
				return "해당 투표를 찾을 수 없습니다.";
			}

			// 시작일과 종료일 확인
			LocalDate startDate = vote.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = vote.get().getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate currentDate = LocalDate.now();

			if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
				return "투표 기간이 아닙니다.";
			}

			// 다중 투표 여부 체크
			if ("N".equals(vote.get().getMultiChoice()) && choices.size() > 1) {
				return "단일 선택만 가능합니다.";
			}

			// 이미 해당 투표에 대한 투표 내역이 있는지 확인
			List<VoteUser> existingVoteUsers = voteUserRepository.findByVoteAndUserEmail(vote.get(), u.getEmail());
			if (!existingVoteUsers.isEmpty()) {
				return "이미 투표하셨습니다."; // 이미 투표한 경우 처리
			}

			// 사용자가 선택한 항목들에 대해 투표 처리
			for (Integer userVoteId : choices) {
				Optional<VoteList> choiceOpt = voteListRepository.findByVoteListId(userVoteId);
				if (choiceOpt.isPresent()) {
					VoteList choice = choiceOpt.get();

					// 새로운 투표 내역 저장
					VoteUser voteUser = new VoteUser();
					voteUser.setVote(vote.get());
					voteUser.setVoteList(choice);
					voteUser.setUserEmail(u.getEmail());

					voteUserRepository.save(voteUser);

					// 선택지 투표 수 증가
					choice.setVoteCount(choice.getVoteCount() + 1);
					voteListRepository.save(choice);
				}
			}

			return "투표가 성공적으로 제출되었습니다.";
		} catch (Exception e) {
			return "투표 참여 실패: " + e.getMessage();
		}
	}

	// 투표 결과 조회
	public void getVoteResult(Integer voteId, Model model) {
		Optional<Vote> vote = voteRepository.findById(voteId);
		if (vote.isPresent()) {
			List<VoteList> voteChoices = voteListRepository.findByVote(vote.get());
			model.addAttribute("vote", vote.get());
			model.addAttribute("voteChoices", voteChoices);

			// 투표 결과가 없다면 (선택지가 없는 경우)
			if (voteChoices.isEmpty()) {
				model.addAttribute("result", "아직 투표 결과가 없습니다.");
			}
		} else {
			model.addAttribute("result", "투표를 찾을 수 없습니다.");
		}
	}

	public void voteDelete(Vote v) {
		voteRepository.delete(v);
	}
}
