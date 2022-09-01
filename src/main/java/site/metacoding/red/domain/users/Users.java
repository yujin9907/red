package site.metacoding.red.domain.users;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.red.web.dto.request.users.updateDto;

@Getter
public class Users {
	private Integer id;
	private String username;
	private String password;
	private String email;
	private Timestamp createdAt;
	
	// 정확하게 세터를 이름지어줌으로서 자유도를 낮추고 확실한 코드, 깔끔한 코드를 구현함.
	public void 패스워드수정(String password) {
		this.password = password;
	}
	
	// 세터를 지움 -> 의미있는 메서드로 구현
	public void 전체수정(updateDto updateDto) {
		this.username = updateDto.getUsername();
		this.password = updateDto.getPassword();
		this.email = updateDto.getEmail();
	}
}
