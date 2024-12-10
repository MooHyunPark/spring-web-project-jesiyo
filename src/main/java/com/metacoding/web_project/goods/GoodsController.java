package com.metacoding.web_project.goods;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
=======
import org.springframework.web.bind.annotation.GetMapping;
>>>>>>> d56edf2 ([feat]git메인화면 javascript 카테고리 전체조회 비동기 요청, [fix] 메인화면 mustache 및 css 수정)

@RequiredArgsConstructor
@Controller
public class GoodsController {
    private final GoodsService goodsService;

<<<<<<< HEAD
    private final HttpSession session;

    // 제품 상세페이지 불러오기
    @GetMapping("/goods-detail/{id}")
    public String goodsDetail(@PathVariable("id")Integer id, Model model) {

        // ***임시*** // 세션에 username 저장
        String username = "ssar";
        session.setAttribute("username", username);

        GoodsResponse.GoodsDetailDTO goodsDetailDTO = goodsService.getGoodsInfo(id);
        model.addAttribute("goods", goodsDetailDTO);

        return "goods-detail";
    }

    // 상품 등록 화면 열기
    @GetMapping("/myPage-goods-register")
    public String register(Model model) {
        return "goods-register";
    }
=======
    @GetMapping("/goods-list")
    public String goodsList(){
        return "goods-list";
    }

>>>>>>> d56edf2 ([feat]git메인화면 javascript 카테고리 전체조회 비동기 요청, [fix] 메인화면 mustache 및 css 수정)
}
