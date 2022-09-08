package site.metacoding.red.web.dto.request.baords;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.red.domain.boards.Boards;

@Getter
@Setter
public class updateDto {
	private String title;
	private String content;
	
	// toentity로도 할 수 있지만, 영속화 변경 수정 할 거임 업데이트만.
	// 정해진 건 없지만, 업데이트시에 영속화 변경 수정이 제일 낫긴 함. (업데이트만, 다른 건 상관없음)
}
