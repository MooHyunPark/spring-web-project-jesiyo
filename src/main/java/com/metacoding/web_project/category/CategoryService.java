package com.metacoding.web_project.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryReponse.AllDTO> findAll(){
        return categoryRepository.findAll().stream().map(CategoryReponse.AllDTO::new).toList();
    }
}
