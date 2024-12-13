package com.metacoding.web_project.user;

import com.metacoding.web_project._core.CommonResp;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm(Model model) {

        return "join";
    }
    @PostMapping("/join")
    public String join(@Valid UserRequest.JoinDTO joinDTO) {
        userService.회원가입(joinDTO);
        return "redirect:/login-form";
    }

    @PostMapping("/check-id")
    public @ResponseBody int checkId(@RequestBody UserRequest.CheckIdDTO username){
        int result = userService.아이디중복확인(username);
        return result;
    }

    @GetMapping("/login-form")
    public String loginForm(String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }


    // /s/붙이기  신용점수 보이는 개인정보 페이지
    @GetMapping("/s/user-info/")
    public String userInfo(@AuthenticationPrincipal User user ,Model model) {
        UserResponse.CreditDTO credits = userService.내정보보기(user.getId());
        model.addAttribute("model", credits);
        System.out.println(model);
        return "user-info";
    }

    // /s/붙이기 개인정보 수정페이지
    @GetMapping("/s/user-info/change-form")
    public String userInfoChangeForm(@AuthenticationPrincipal User user, Model model) {
        UserResponse.InfoDTO infoDTO = userService.유저정보보기(user.getId());
        model.addAttribute("info", infoDTO);
        return "user-info-change";
    }

    // 개인정보 수정 + 계좌등록하기
    @PostMapping("/s/user-info/change")
    public String userInfoChange(@AuthenticationPrincipal User user,UserRequest.UpdateDTO updateDTO) {
        userService.유저정보수정하기(user.getId() ,updateDTO);
        return "redirect:/s/user-info/change-form";
    }


    @PostMapping("/s/user-info/pw-change")
    public String pwChange(@AuthenticationPrincipal User user, UserRequest.ChangePwDTO changePwDTO) {
        userService.비밀번호변경(user.getId(),changePwDTO);
        return "redirect:/s/user-info/change-form";
    }

    // 아이디/비밀번호 찾기 form
    @GetMapping("/user-find-form")
    public String findUser() {
        return "user-find";
    }

    @PostMapping("/user-find-id")
    @ResponseBody
    public ResponseEntity<?> findId(@RequestBody UserRequest.FindUserDTO findUserDTO) {
        String result = String.valueOf(userService.아이디찾기(findUserDTO));
        return ResponseEntity.ok(Map.of("result", result));

    }

    // 인증
    @ResponseBody
    @GetMapping("/api/v1/authentication")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        CommonResp<UserDetails> resp = CommonResp.success(userDetails);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/user-find-pw")
    public  @ResponseBody Integer findPw(@RequestBody UserRequest.FindPwDTO findPwDTO) {
        int result = userService.비번찾기(findPwDTO);
        return result; // 0 실패, 1 이상은 성공
    }

    // 1. 비밀번호 변경 페이지 줘
    @GetMapping("/change-pw-form/{id}")
    public String changepwForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "change-pw";
    }

    @PostMapping("/change-pw/{id}")
    public String changepw(@PathVariable("id") int id, UserRequest.ChPwDTO pwDTO) {
        userService.비번변경(id,pwDTO);
        return "redirect:/";
    }

}