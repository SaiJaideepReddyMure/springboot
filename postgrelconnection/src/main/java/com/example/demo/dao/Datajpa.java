package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Person;

public interface Datajpa extends JpaRepository<Person,Integer>{

}
