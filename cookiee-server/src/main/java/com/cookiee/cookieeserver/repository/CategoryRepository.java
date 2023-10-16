package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
