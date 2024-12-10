package com.metacoding.web_project.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;


@Import(CategoryRepository.class)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
<<<<<<< HEAD
    public void findAllCategory_test() {
        List<Category> list = categoryRepository.findAllCategory();
        System.out.println(list.size());
=======
    public void findAll_test(){
        List<Category> category = categoryRepository.findAll();

        for(Category c : category){
            System.out.println(c.getName());
        }

>>>>>>> d56edf2 ([feat]git메인화면 javascript 카테고리 전체조회 비동기 요청, [fix] 메인화면 mustache 및 css 수정)
    }
}
