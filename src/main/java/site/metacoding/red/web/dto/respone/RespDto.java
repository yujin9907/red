package site.metacoding.red.web.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data // 데이터 쓰지 말기 헷갈림
@Getter
@Setter
@AllArgsConstructor
public class RespDto<T> {
	private Integer code; // 1정상 -1실패
	private String msg; // 통신결과 메시지
	private T body; // 바디의 타입은 항상 다름 (ex 한건찾기 전체보기) => 제네릭 사용
}
