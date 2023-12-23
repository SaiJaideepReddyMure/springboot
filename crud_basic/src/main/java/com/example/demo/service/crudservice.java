package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.Datainforepo;
import com.example.demo.model.*;

@Service
public class crudservice {
	@Autowired
	 private Datainforepo datarepo;

	public ResponseEntity<List<Info>> display() {
		try {
		 
		return new ResponseEntity<>(datarepo.findAll(),HttpStatus.OK);
		}
		catch(Exception e)
		{
		 e.printStackTrace();	
		}
		return new ResponseEntity<>(datarepo.findAll(),HttpStatus.BAD_REQUEST);
	}

	public  String add(Info info) {
		// TODO Auto-generated method stub
		   datarepo.save(info);
		   return "Sucess";
	}

	public String del(int id) {
		// TODO Auto-generated method stub
		try {
		datarepo.deleteById(id);
		return "Sucess";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		return "error";
		
	}

	public String update(Integer id, Info inr)
	{
		Optional<Info> in = datarepo.findById(id);
		if(in.isPresent()) {
			Info inr2 = in.get();
			 inr2.setName(inr.getName());
			 inr2.setEmail(inr.getEmail());
			 datarepo.save(inr2);
			 System.out.print("Sucess");
			
		}
		else
		{
			System.out.println("Failed to update");
		}
		
		return null;
	}

	public List<Info> dis() {
		// TODO Auto-generated method stub
		return datarepo.findAll();
	}

	public Optional<Info> displayone(Integer id) {
		// TODO Auto-generated method stub
		return  datarepo.findById(id);
	}
	
	
	

}
