package com.ungman.sc.swagger;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/boards") // Board API 공통 경로
public class SwaggerBoardApi {

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping
    public String createBoard() {
        return "Board created";
    }

    @Operation(summary = "게시글 조회", description = "ID로 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public String getBoard(@PathVariable Integer id) {
        return "Board info for ID: " + id;
    }

    @Operation(summary = "게시글 목록 조회", description = "전체 게시글 목록을 조회합니다.")
    @GetMapping
    public String getBoards() {
        return "List of boards";
    }

    @Operation(summary = "게시글 수정", description = "ID로 게시글을 수정합니다.")
    @PutMapping("/{id}")
    public String updateBoard(@PathVariable Integer id) {
        return "Board updated: " + id;
    }

    @Operation(summary = "게시글 삭제", description = "ID로 게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable Integer id) {
        return "Board deleted: " + id;
    }
}
