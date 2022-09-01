package site.metacoding.red.web.dto.request.boards;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteDto {
	private String title;
	private String content;
//	private Integer userId; 사용자가 입력할 것만 넣어야 됨 ; 클라이언트-컨트룰 통신을 위한
	// 세션에 아이디를 담아서 나중에 찾을 수 있게 됨
}
