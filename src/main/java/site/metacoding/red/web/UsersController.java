package site.metacoding.red.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import lombok.experimental.PackagePrivate;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;
import site.metacoding.red.web.dto.request.users.userUpdateDto;

import java.util.List;

@RequiredArgsConstructor // 디펜더시 인젝션
@Controller
public class UsersController {
	
	private final UsersDao usersDao; // 컴포지션 인젝션
	private final BoardsDao boardsDao;
	private final HttpSession sessoin; // 스프링이 서버 시작시 ioc 컨테이너에 보관함 => 어노테이션으로 만들 수도 있음
	
	
	@GetMapping("/logout")
	public String logout() {
		sessoin.invalidate();
		return "redirect:/boards"; // 이거 왜  /로 리다이렉트 하면 안 됨?
	}
	
	@PostMapping("/login") // 셀렉이지만 포스트, 로그인만 예외로 select인데 post로 함
	public String login(LoginDto loginDto, HttpServletRequest request) { // 필드가 두개밖에 없느니까 직접 필드 넣..? 안 됨 패턴 지켜서
		Users usersPS = usersDao.login(loginDto); // 웬만하면 DB에서 들고온에는 PS 붙여서 변수명 생성, 헷갈리니까
		if(usersPS != null) { // 인증
			// session 사용 필요, 로그인 정보는 전달되야 하는데, 리퀘스트에 넣으면 응답후 사라지니까
			// 세션에 접근하려면 일단 리ㅜ케스트 객체에 접근해야 됨
			// HttpSession sessoin = request.getSession(); // 세션에 접근하는 주소(만듦)
			// 근데? 세션 너무 자주 써서 ioc 컨테이너에 세션이 있음 -> 디펜던시 인젝션만 하면 끝남
			
			// 세션 30분간 안찾으면 날라감(이렇게 설계돼있음), 로그아웃요청, 브라우저에 쿠키가 삭제되거나 => 로그아웃 3가지 방법
			// 쿠키는 브라우저의 저장소, 세션은 서버의 저장소
			
			sessoin.setAttribute("principal", usersPS); // principal 인증된 유저 // 세션에 키값과 유저오브젝트 저장
			
			return "redirect:/";
		} else { // 노인증
		
			return "redirect:/loginForm";
		}
	}
	
	@PostMapping("/join") // 인증에 관련된 주소는 엔티티명을 붙이지 않음 : 규칙
	public String join(JoinDto joinDto) {
		usersDao.insert(joinDto);
		return "redirect:/loginForm"; // 리다이렉트 메서드 때리는 거임...
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		return "users/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "users/joinForm";
	}
	
	@GetMapping("/updateForm")
	public String usersUpdateForm(Model model) {
		Users principal = (Users) sessoin.getAttribute("principal");
		if (principal == null) {
			return "errors/badPage";
		}
		System.out.println(principal.getId());
		model.addAttribute("principal", principal);
		return "users/updateForm";
	}
	
	@PostMapping("/update/{id}")
	public String usersUpdate(@PathVariable Integer id, userUpdateDto userUpdateDto) {
		Users usersPS = usersDao.findById(id);
		if(usersPS==null) {
			return "errors/badPage";
		}
		usersPS.유저수정(userUpdateDto);
		usersDao.update(usersPS);
		
		return "redirect:/";
	}
	@PostMapping("/users/{id}/delete")
	public String userdelete(@PathVariable Integer id){

		Users users = usersDao.findById(id);
		if(users!=null) {
			usersDao.delete(id);
//			List<Boards> boards = boardsDao.findByUsersId(id);
//			if(boards!=null) {
//					for (Boards b : boards) {
//						Integer bid = b.getId();
//						boardsDao.updateAnony(bid);
//					}
//			}
			// 이 로직이 뭐가 잘못 돼서 null -> 2 가 실행 안 된 거임?
			boardsDao.updateAnony();
			sessoin.invalidate(); // 세션 무효화 시키기
		}
		return "redirect:/";
	}
}
