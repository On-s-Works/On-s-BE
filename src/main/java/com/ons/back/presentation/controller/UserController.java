package com.ons.back.presentation.controller;

import com.ons.back.application.service.UserService;
import com.ons.back.commons.dto.PrincipalDetails;
import com.ons.back.presentation.dto.request.UpdatePasswordRequest;
import com.ons.back.presentation.dto.response.ReadUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ReadUserResponse> getUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.getUserByKey(user.getUsername()));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal PrincipalDetails user, @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(user.getUsername(), request.password());
        return ResponseEntity.noContent().build();
    }
}
