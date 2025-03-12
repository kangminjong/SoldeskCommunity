package com.ungman.sc.swagger;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/users") // User API 공통 경로
public class SwaggerUserApi {

    @Operation(summary = "회원 가입", description = "사용자를 등록합니다.")
    @PostMapping
    public String createUser() {
        return "User created";
    }

    @Operation(summary = "사용자 정보 조회", description = "이메일로 사용자의 정보를 조회합니다.")
    @GetMapping("/{email}")
    public String getUser(@PathVariable String email) {
        return "User info for: " + email;
    }

    @Operation(summary = "사용자 정보 수정", description = "이메일로 사용자의 정보를 수정합니다.")
    @PutMapping("/{email}")
    public String updateUser(@PathVariable String email) {
        return "User updated: " + email;
    }

    @Operation(summary = "사용자 삭제", description = "이메일로 사용자를 삭제합니다.")
    @DeleteMapping("/{email}")
    public String deleteUser(@PathVariable String email) {
        return "User deleted: " + email;
    }
}
