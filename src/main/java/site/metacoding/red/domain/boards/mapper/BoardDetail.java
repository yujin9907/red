package site.metacoding.red.domain.boards.mapper;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BoardDetail {
	private Integer id;
	private String title;
	private String content;
	private Integer usersId;
	private Timestamp createdAt;
	private String username;
	private String password;
	private String email;
}
