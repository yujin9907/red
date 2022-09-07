package site.metacoding.red.web.dto.response.boards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagingDto {
	private Integer totalCount;
	private Integer totalPage;
	private Integer currentPage;
	private boolean isFirst;
	private boolean isLast;

	private Integer startPageNum; // 1 -> 6 -> 11 pageBlock을 기준으로 잡고 넘어가야함.
	private Integer lastPageNum; // 5 -> 10 -> 15
	private Integer blockPage; // 변수
	private Integer blockPageCount;
	// ?page=0 기준 blockPage = 1, startPageNum = 1, lastPageNum = 5, blockPageCount =
	// 1, 2, 3, 4, 5 (5개)
	// ?page=5 기준 blockPage = 2, startPageNum = 6, lastPageNum = 10, blockPageCount
	// = 6, 7, 8, 9, 10 (5개)
	
	public PagingDto(Integer totalCount, Integer totalPage, Integer currentPage,boolean isFirst, boolean isLast) {
		this.totalCount=totalCount;
		this.totalPage=totalPage;
		this.currentPage=currentPage;
		this.isFirst=isFirst;
		this.isLast=isLast;
	}
}
