package com.kimmjen.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kimmjen.domain.ReplyVO;
import com.kimmjen.service.ReplyService;

@Controller
@RequestMapping("/reply/*")
public class ReplyController {

	@Inject
	private ReplyService replyService;
	
	// 댓글 조회
	
	// 댓글 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String postWrite(ReplyVO vo) throws Exception {
		replyService.write(vo);
		
		return "redirect:/board/view?bno=" + vo.getBno();
	}
	// 댓글 수정
	
	// 댓글 삭제
	
}
