package com.kimmjen.jpa.model;

import com.kimmjen.jpa.dto.BoardRequestDto;
import com.kimmjen.jpa.dto.BoardResponseDto;
import com.kimmjen.jpa.entity.Board;
import com.kimmjen.jpa.entity.BoardRepository;
import com.kimmjen.jpa.exception.CustomException;
import com.kimmjen.jpa.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public Long save(final BoardRequestDto params) {

        Board entity = boardRepository.save(params.toEntity());
        return entity.getId();
    }

    /**
     * 게시글 리스트 조회
     */
    public List<BoardResponseDto> findAll() {

        Sort sort = Sort.by(Sort.Direction.DESC, "id", "createdDate");
        List<Board> list = boardRepository.findAll(sort);
        return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }
    /*public List<BoardResponseDto> findAll() {

        Sort sort = Sort.by(Direction.DESC, "id", "createdDate");
        List<Board> list = boardRepository.findAll(sort);

        *//* Stream API를 사용하지 않은 경우 *//*
        List<BoardResponseDto> boardList = new ArrayList<>();

        for (Board entity : list) {
            boardList.add(new BoardResponseDto(entity));
        }

        return boardList;
    }*/

    /**
     * 게시글 수정
     */
    @Transactional
    public Long update(final Long id, final BoardRequestDto params) {

        Board entity = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        entity.update(params.getTitle(), params.getContent(), params.getWriter());
        return id;
    }

}