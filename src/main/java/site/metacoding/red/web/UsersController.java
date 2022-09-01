package site.metacoding.red.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.updateDto;
import site.metacoding.red.web.dto.respone.RespDto;


// di를 하는 가장 베스트, allargs는 모든 값을 넣어야 하므로 require가 더 좋음, id의 조건은 객체가 ioc에 있어야 됨. 
@RequiredArgsConstructor // ioc에 있는 모든 객체를 생성자주입을 통해 받을 수 있게 하는 어노테이션, final이 붙은 거만 생성자를 만들어줌. 더 검색해보기
@RestController
public class UsersController {
	//널 처리와, 깔끔한 데이터 전송(공공데이터처럼) 전송하기 위해 형식 맞추기 -> dto 적용

	// 파이널로 정의된 필드는, new 시킬 때 값이 있어야 됨. 즉, 생성자주입을 무조건 해줘야 됨.
	private final UsersDao usersDao;
	
	@GetMapping("/users/{id}")
	public RespDto<?> getUsers(@PathVariable Integer id) {
		return new RespDto<>(1, "성공", usersDao.findById(id)); // 제네릭 뒤 타입은 안써줘도 됨 (문법바뀜)
	}
	@GetMapping("/users")
	public RespDto<?> getUsersList(){
		return new RespDto<>(1, "성공", usersDao.findAll());
	}
	@PostMapping("/users")
	public RespDto<?> insert(JoinDto joinDto) { // 와일드카드=ODJECT 아무거나 적어도 됨
		//insert 완료
		// 기본 파싱 전략 : x, json으로 받으려면 @requestbody 지금은 그냥 폼으로 ㅂ다는ㄷ
		usersDao.insert(joinDto);
		return new RespDto<>(1, "성공", null); // 완료됐음 표시, 201 -INSERT 됨
	}
	
	// 리팩토링
	@PutMapping("/users/update/{id}")
	public RespDto<?> update(@PathVariable Integer id, updateDto updateDto) {
		// 1번 : id로 셀렉 (영속화) 데이터베이스의 로우를 자바 오브젝트로 옮기는 행위
		Users usersPS = usersDao.findById(id);
		// 2번 : 변경(기존 코드대로 하면 자유도가 너무 높기 때문에 수정
		usersPS.전체수정(updateDto);
		// 3번 : 영속화된 오브젝트를 업데이트하기
		usersDao.update(usersPS);
		return new RespDto<>(1, "성공", null);
	}
	
	@DeleteMapping("/users/delete/{id}")
	public RespDto<?> delete(@PathVariable Integer id) {
		usersDao.delete(id);
		return new RespDto<>(1, "성공", null);
	}
	
	// 업데이트 리팩토링
	@PutMapping("/user/{id}/password")
	public RespDto<?> updatePassword(@PathVariable Integer id, String password){
		// 개념은 전체 업데이트지만 영속화를 통해 부분업데이트를 함
		
		//1번 영속화
		Users users = usersDao.findById(id);
		//2번 부분변경
		users.패스워드수정(password);
		//3번 전체업데이트
		usersDao.update(users);
		
		return null;
	}
}
