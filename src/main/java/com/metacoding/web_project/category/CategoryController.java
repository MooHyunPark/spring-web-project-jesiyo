package com.metacoding.web_project.category;

import com.metacoding.web_project._core.CommonResp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {
    private final CategoryService categoryService;

    // 일단 임시로 나두겠음
    @GetMapping("/")
    public String main(Model model) {
        return "main";
    }

    @ResponseBody
    @GetMapping("/api/v1/category")
    public ResponseEntity<?> category() {
         List<CategoryReponse.AllDTO> dto = categoryService.findAll();
         CommonResp<List<CategoryReponse.AllDTO>> resp = CommonResp.success(dto);
         return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
