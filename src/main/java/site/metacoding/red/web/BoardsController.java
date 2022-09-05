package site.metacoding.red.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.baords.WriteDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {

	private final HttpSession session; // final을 붙이는 이유 -> 롬북으로 세션자 리콰이어아규멘트 이용하기 위해서
	private final BoardsDao boardsDao;

	@PostMapping("/boards")
	public String writeBoards(WriteDto writeDto) {
		Users principal = (Users) session.getAttribute("principal");

		// 리팩토링-인증 ; 이런 if-else는 좋은 코드가 아님...
		if (principal == null) {
			return "redirect:/loginForm";
		}
		
		// 본코드 리팩토링
		
		boardsDao.insert(writeDto.toEntity(principal.getId())); // 
		return "redirect:/";

	}

	@GetMapping({ "/", "/boards" })
	public String getBoardList() {
		return "boards/main";
	}

	@GetMapping("/boards/{id}")
	public String getBoardList(@PathVariable Integer id) {
		return "boards/detail";
	}

	@GetMapping("/boards/writeForm")
	public String writeForm() {

		Users principal = (Users) session.getAttribute("principal"); // getAttribute(키값)

		if (principal == null) {
			return "redirect:/loginForm"; // -> 리다이렉트 하는 이유? 이미 메서드가 있으니까 파일을 열 필요없이 재사용해야됨 무조건(단일책임원칙 같은 걸 생각을 해보자)
		}
		return "boards/writeForm";

	}
}
