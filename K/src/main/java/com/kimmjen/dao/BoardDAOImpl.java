package com.kimmjen.dao;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kimmjen.domain.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {
	
	@Inject
	private SqlSession sql;
	
	private static String namesapce = "com.kimmjen.mappers.board";

	// 게시물 목록
	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		
		return sql.selectList(namesapce + ".list");
	}
	
	// 게시물 작성
	@Override
	public void write(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		
		sql.insert(namesapce + ".write", vo);
		
	}

	// 게시물 조회
	@Override
	public BoardVO view(int bno) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne(namesapce + ".view", bno);
	}
	
	// 게시물 수정
	@Override
	public void modify(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.update(namesapce + ".modify", vo);
		
	}

	// 게시물 삭제
	@Override
	public void delete(int bno) throws Exception {
		// TODO Auto-generated method stub
		sql.delete(namesapce + ".delete", bno);
	}

	// 게시물 총 갯수
	@Override
	public int count() throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne(namesapce + ".count");
	}

	// 게시물 목록과 페이징
	@Override
	public List<BoardVO> listPage(int displayPost, int postNum) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		
		data.put("displayPost", displayPost);
		data.put("postNum", postNum);
		
		return sql.selectList(namesapce + ".listPage", data);
	}
	
	// 게시물 목록과 페이징 + 검색
	@Override
	public List<BoardVO> listPageSearch(
			int displayPost, 
			int postNum, 
			String searchType, 
			String keyword)
			throws Exception {
		// TODO Auto-generated method stub
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		data.put("displayPost", displayPost);
		data.put("postNum", postNum);
		
		data.put("searchType", searchType);
		data.put("keyword", keyword);
		
		return sql.selectList(namesapce + ".listPageSearch", data);
	}

	// 게시물 총 갯수 + 검색적용
	@Override
	public int searchCount(String searchType, String keyword) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		data.put("searchType", searchType);
		data.put("keyword", keyword);
		
		return sql.selectOne(namesapce + ".searchCount", data);
	}
	


}
