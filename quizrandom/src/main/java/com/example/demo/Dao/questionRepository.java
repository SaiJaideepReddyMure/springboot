package com.example.demo.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Question;

public interface questionRepository extends JpaRepository<Question,Long>{

}
