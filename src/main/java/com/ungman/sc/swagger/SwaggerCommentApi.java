package com.ungman.sc.swagger;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/comments") // Comment API 공통 경로
public class SwaggerCommentApi {

    @Operation(summary = "댓글 추가", description = "게시글에 댓글을 추가합니다.")
    @PostMapping
    public String createComment() {
        return "Comment created";
    }

    @Operation(summary = "댓글 조회", description = "ID로 댓글을 조회합니다.")
    @GetMapping("/{id}")
    public String getComment(@PathVariable Integer id) {
        return "Comment info for ID: " + id;
    }

    @Operation(summary = "댓글 수정", description = "ID로 댓글을 수정합니다.")
    @PutMapping("/{id}")
    public String updateComment(@PathVariable Integer id) {
        return "Comment updated: " + id;
    }

    @Operation(summary = "댓글 삭제", description = "ID로 댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Integer id) {
        return "Comment deleted: " + id;
    }
}
