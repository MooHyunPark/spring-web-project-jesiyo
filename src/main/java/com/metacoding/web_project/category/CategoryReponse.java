package com.metacoding.web_project.category;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CategoryReponse {

    @Data
    public static class AllDTO{
        private Integer id;
        private String name;
        private String imgUrl;

        public AllDTO(Category category){
            this.id = category.getId();
            this.name = category.getName();
            this.imgUrl = category.getImgUrl();
        }
    }

}
