package com.metacoding.web_project.category;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {
    private final EntityManager em;

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class).getResultList();
    }
}
