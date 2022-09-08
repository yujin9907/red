package site.metacoding.red.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.baords.WriteDto;
import site.metacoding.red.web.dto.request.baords.updateDto;
import site.metacoding.red.web.dto.response.boards.MainDto;
import site.metacoding.red.web.dto.response.boards.PagingDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {

	private final HttpSession session; // final을 붙이는 이유 -> 롬북으로 세션자 리콰이어아규멘트 이용하기 위해서
	private final BoardsDao boardsDao;

	// 1번째 ?page=0&keyword=스프링 / 프라이머리 키, where 아니니 패스배리어블 ㄴ
	// 페이지 값을 input을 추가해서 넣어줘도 됨 type hidden으로
	@GetMapping({ "/", "/boards" })
	public String getBoardList(Model model, @Param("page") Integer page, @Param("keyword") String keyword) {
		// 페이징 리팩토링 전
		if (page == null) {
			page = 0;
		}
		int startNum = page * 3; // 10개 단위로 담페이지

		// 검색로직
		if (keyword == null || keyword.isEmpty()) {
			// 다른 컨트룰러로 만들어도 됨 굳이 그럴 필요 없지만
			// null 이면 검색 안 한다는 거니까 기존 페이징 로직 그대로

			List<MainDto> boardsList = boardsDao.findAll(startNum);

			PagingDto paging = boardsDao.paging(page,null);
			Integer currentPage = paging.getCurrentPage();
			Integer totalPage = paging.getTotalPage();

			Integer count = 5;
			Integer start = ((currentPage / count) * count) + 1;
			Integer block = (currentPage / count) + 1;
			Integer last = ((currentPage / count) + 1) * count;

			// 마지막은 어쩔 수 없는...
			if (paging.getTotalPage() < last) {
				last = paging.getTotalPage();
			}

			paging.setKeyword(keyword);
			paging.setStartPageNum(start);
			paging.setLastPageNum(last);
			paging.setBlockPage(block);
			paging.setBlockPageCount(count);

			model.addAttribute("paging", paging);
			model.addAttribute("boardsList", boardsList);



		} else {
			// 페이징 처리 쿼리 + where이 필요함 => 동적 쿼리, 어차피 웨어절만 추가되니까
			List<MainDto> boardsList = boardsDao.findSearch(startNum, keyword);

			PagingDto paging = boardsDao.paging(page, keyword);
			Integer currentPage = paging.getCurrentPage();
			Integer totalPage = paging.getTotalPage();

			Integer count = 5;
			Integer start = ((currentPage / count) * count) + 1;
			Integer block = (currentPage / count) + 1;
			Integer last = ((currentPage / count) + 1) * count;

			// 마지막은 어쩔 수 없는...
			if (paging.getTotalPage() < last) {
				last = paging.getTotalPage();
			}
			
			paging.setKeyword(keyword);
			paging.setStartPageNum(start);
			paging.setLastPageNum(last);
			paging.setBlockPage(block);
			paging.setBlockPageCount(count);
			
			System.out.println(boardsList);
			System.out.println(paging.getTotalCount());

			model.addAttribute("paging", paging);
			model.addAttribute("boardsList", boardsList);
		}
		
		return "boards/main";

	}

	@GetMapping("/boards/{id}")
	public String getBoardList(@PathVariable Integer id, Model model) {
		Boards boards = boardsDao.findById(id);
		model.addAttribute("boards", boards);
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

	// 글쓰기 구현
	@PostMapping("/boards")
	public String writeBoards(WriteDto writeDto) {
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "redirect:/loginForm";
		}
		boardsDao.insert(writeDto.toEntity(principal.getId()));
		return "redirect:/";
	}

	// 삭제 js 없이
	@PostMapping("/boards/{id}/delete")
	public String deleteBoards(@PathVariable Integer id) { // 리스폰 붙이면 데이터 리턴으로 바뀜
		// 왜 영속화를 시켜서 조건에 맞아야 딜리트를 실행할까?
		// 데이터베이스는 트랜잭션 시 부하가 가장 큼
		// 트랜잭션 시 아이솔레이션에 의해, 다른 새끼들은 못 함. 즉, select를 하는 게 delete, insert 이런 거 하는 거보다
		// 나음. select로 검사 먼저 해야 됨
		// 다이렉트로 딜리트 했는데 없으면 트랜잭션 부하 손해임으로

		// js 배우고 할 것 history.back, location.href= 를 통해 redirect 간편하게 사용 가능
//		StringBuffer sb = new StringBuffer();
//		sb.append("<script>");
//		sb.append("locatonlkdsjsdf");
//		return sb.toString(); // 이런식으로 자바스크립트를 데이터로 직접 넣음

		// 영속화
		Boards boardsPS = boardsDao.findById(id);

		// 인증, 권한 체크
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "errors/badPage";
		}
		if (principal.getId() != boardsPS.getId()) {
			return "errors/badPage";
		}

		if (boardsPS == null) { // if는 비정상 로직을 타게 해서 걸러내는 필터 역할을 하는 게 좋음(권장)
			return "errors/badPage"; // 상세보기 주소로 돌아가기(제자리)
		}
		boardsDao.delete(id);
		return "redirect:/";
	}

	@GetMapping("/boards/{id}/updateForm") // 보드테이블에 있는 key.게시글을 업데이트할 폼을 주소 : 말이 되는 주소 : restfull api : 자원에 접근하는 주소를
											// 설계하는 방식 => 주소설계규칙 검색하면 나옴, 말이 되게 읽히는 공부를 하면 됨
	// 많이 해봐야 어쩌고 자연스럽게 슬 수 있음. 연습을 많이 해야 어차피 강제는 아니니까 천천히
	public String updateForm(@PathVariable Integer id, Model model) {
		Boards boardsPS = boardsDao.findById(id);
		Users principal = (Users) session.getAttribute("principal");
		System.out.println(boardsPS);
		System.out.println(principal);
		// 비정상요청 처리
		if (boardsPS == null) {
			return "errors/badPage";
		}
		if (principal == null) {
			return "errors/badPage";
		}
		if (principal.getId() == boardsPS.getId()) {
			return "errors/badPage";
		}

		model.addAttribute("boards", boardsPS);

		return "boards/updateForm";
	}

	// 업데이트를 완료하고 무슨 페이지를 반환할까 사용자들이 가장 많이 가는 곳, 분석하는 사이트가 있음 ux를 위해서, 구글의 에널리틱스
	// 검색(자바스크립트 알아야 할 수 있음)
	@PostMapping("/boards/{id}/update")
	public String update(@PathVariable Integer id, updateDto updateDto) {
		// update, insert dto를 같이 쓰기 위해서 saveDto 이런식으로 사용하기도 함

		// 1. 영속화
		Boards boardsPS = boardsDao.findById(id);
		Users principal = (Users) session.getAttribute("principal");

		// 비정상요청 처리
		if (boardsPS == null) {
			return "errors/badPage";
		}
		if (principal == null) {
			return "errors/badPage";
		}
		if (principal.getId() == boardsPS.getId()) {
			return "errors/badPage";
		}

		// 2. 변경
		boardsPS.글수정(updateDto);

		// 3. 수행
		boardsDao.update(boardsPS);

		return "redirect:/boards/" + id; // ux에 따라서 달라짐
	}
}
