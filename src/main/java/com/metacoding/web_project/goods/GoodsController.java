package com.metacoding.web_project.goods;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class GoodsController {
    private final GoodsService goodsService;

    @GetMapping("/goods-list")
    public String goodsList(){
        return "goods-list";
    }

}
