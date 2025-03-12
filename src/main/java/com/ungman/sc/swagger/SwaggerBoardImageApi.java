package com.ungman.sc.swagger;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/board-images") // Board Image API 공통 경로
public class SwaggerBoardImageApi {

    @Operation(summary = "이미지 업로드", description = "게시글에 이미지를 업로드합니다.")
    @PostMapping
    public String uploadBoardImage() {
        return "Board image uploaded";
    }

    @Operation(summary = "이미지 조회", description = "ID로 게시글 이미지를 조회합니다.")
    @GetMapping("/{id}")
    public String getBoardImage(@PathVariable Integer id) {
        return "Board image for ID: " + id;
    }
}
