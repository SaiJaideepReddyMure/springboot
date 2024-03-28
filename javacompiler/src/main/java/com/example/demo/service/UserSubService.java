package com.example.demo.service;
 
import org.springframework.stereotype.Service;

import com.example.demo.model.usersub;
import com.example.demo.reposistory.UserSubRepository;

@Service
public class UserSubService {

    private final UserSubRepository usersubRepository;

    public UserSubService(UserSubRepository userSubRepository) {
        this.usersubRepository = userSubRepository;
    }

    public void saveUserSub(usersub userSub) {
    	 
    	 
        usersubRepository.save(userSub);

    	
    	}

	public boolean checking(String s) {
		// TODO Auto-generated method stub
		return  usersubRepository.existsById(s);
	}

	 
   
}
