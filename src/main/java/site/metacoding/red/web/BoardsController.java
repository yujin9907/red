package site.metacoding.red.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.boards.mapper.BoardDetail;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.boards.UpdateDto;
import site.metacoding.red.web.dto.request.boards.WriteDto;
import site.metacoding.red.web.dto.respone.RespDto;

@RequiredArgsConstructor
@RestController
public class BoardsController {
	
	private final BoardsDao boardsDao;
	
	@GetMapping("/board/{id}")
	public RespDto<?> findById(@PathVariable Integer id) {
		return new RespDto<>(1, "한건조회", boardsDao.findByIdToDetail(id));
	}
	
	@GetMapping("/board")
	public RespDto<?> findAll(){
		return new RespDto<>(1, "전체조회", boardsDao.findAll());
	}
	
	@PostMapping("/board")
	public RespDto<?> insert(WriteDto writeDto){
		boardsDao.insert(writeDto);
		return new RespDto<>(1, "입력", null);
	}
	
	@DeleteMapping("/board/{id}/delete")
	public RespDto<?> delete(@PathVariable Integer id){
		boardsDao.delete(id);
		return new RespDto<>(1, "삭제", null);
	}
	@PutMapping("/board/{id}")
	public RespDto<?> update(@PathVariable Integer id, UpdateDto updateDto){
		Boards boardPS = boardsDao.findById(id);
		boardPS.전체수정(updateDto);
		boardsDao.update(boardPS);
		
		BoardDetail view =  boardsDao.findByIdToDetail(id);
		
		return new RespDto<>(1, "성공", view);
	}
}
