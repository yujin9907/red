package site.metacoding.red.web.dto.request.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class updateDto {
	private String username;
	private String password;
	private String email;
}
