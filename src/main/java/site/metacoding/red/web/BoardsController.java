package site.metacoding.red.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.baords.WriteDto;
import site.metacoding.red.web.dto.response.boards.MainDto;
import site.metacoding.red.web.dto.response.boards.PagingDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {

	private final HttpSession session; // final을 붙이는 이유 -> 롬북으로 세션자 리콰이어아규멘트 이용하기 위해서
	private final BoardsDao boardsDao;

	@GetMapping({ "/", "/boards" })
	public String getBoardList(Model model, Integer page, MainDto mainDto) {
		if(page==null) page=0;
		int startNum = page*3; // 10개 단위로 담페이지
		
		List<MainDto> boardsList = boardsDao.findAll(startNum);
		
		PagingDto paging = boardsDao.paging(page);
		Integer currentPage = paging.getCurrentPage();
		Integer totalPage = paging.getTotalPage();
	
		Integer count= 5;
		Integer start = ((currentPage/count) * count)+1;
		Integer block = (currentPage/count)+1;
		Integer last=((currentPage/count)+1) * count;
		
		// 마지막은 어쩔 수 없는...
		if(paging.getTotalPage()<last) {
			last = paging.getTotalPage();
		}
		
		paging.setStartPageNum(start);
		paging.setLastPageNum(last);
		paging.setBlockPage(block);
		paging.setBlockPageCount(count);
		
		model.addAttribute("paging", paging);
		model.addAttribute("boardsList", boardsList);
		
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
		if(principal == null) {
			return "redirect:/loginForm";
		}
		boardsDao.insert(writeDto.toEntity(principal.getId()));
		return "redirect:/";
	}
	
	// 삭제 js 없이
	@PostMapping("/boards/{id}/delete")
	public String deleteBoards(@PathVariable Integer id) {
		// 왜 영속화를 시켜서 조건에 맞아야 딜리트를 실행할까? 
		// 데이터베이스는 트랜잭션 시 부하가 가장 큼
		// 트랜잭션 시 아이솔레이션에 의해, 다른 새끼들은 못 함. 즉, select를 하는 게 delete, insert 이런 거 하는 거보다 나음. select로 검사 먼저 해야 됨
		// 다이렉트로 딜리트 했는데 없으면 트랜잭션 부하 손해임으로
		
		// 영속화
		Boards boardsPS = boardsDao.findById(id);
		
		// 인증, 권한 체크
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "redirect:/boards/"+id;
		}
		if (principal.getId()!=boardsPS.getId()){
			return "redirect:/boards/"+id;
		}
		
		if(boardsPS == null) { // if는 비정상 로직을 타게 해서 걸러내는 필터 역할을 하는 게 좋음(권장)
			return "redirect:/boards/"+id; // 상세보기 주소로 돌아가기(제자리)
		}
		boardsDao.delete(id);
		return "redirect:/";
	}
}
