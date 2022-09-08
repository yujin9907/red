package site.metacoding.red.domain.users;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.metacoding.red.web.dto.request.users.userUpdateDto;

@Setter
@Getter
@AllArgsConstructor
public class Users {
	private Integer id;
	private String username;
	private String password;
	private String email;
	private Timestamp createdAt;
	
	public void 유저수정(userUpdateDto userUpdateDto) {
		this.password = userUpdateDto.getPassword();
		this.email = userUpdateDto.getEmail();
	}
}
