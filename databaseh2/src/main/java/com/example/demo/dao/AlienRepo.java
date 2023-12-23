package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Alien;

public interface AlienRepo extends CrudRepository<Alien,Integer>
{
	List<Alien> findByaname(String tech);// automatically check method
	@Query("from alien where id =?1 order by aname")
	List<Alien> findByanam(String tech);
}
