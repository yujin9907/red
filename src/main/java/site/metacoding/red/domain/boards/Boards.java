package site.metacoding.red.domain.boards;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.metacoding.red.web.dto.request.baords.updateDto;

@Getter
@AllArgsConstructor
public class Boards {
	private Integer id;
	private String title;
	private String content;
	private Integer usersId;
	private Timestamp createdAt;
	
	
	public Boards(String title, String content, Integer usersId) {
		this.title = title;
		this.content = content;
		this.usersId = usersId;
	}
	public void 글수정(updateDto updateDto) {
		this.title=updateDto.getTitle();
		this.content=updateDto.getContent();
	}
	
}
