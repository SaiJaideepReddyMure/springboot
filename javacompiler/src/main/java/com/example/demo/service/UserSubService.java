package com.example.demo.service;
 
import org.springframework.stereotype.Service;

import com.example.demo.model.usersub;
import com.example.demo.reposistory.UserSubRepository;

@Service
public class UserSubService {

    private final UserSubRepository userSubRepository;

    public UserSubService(UserSubRepository userSubRepository) {
        this.userSubRepository = userSubRepository;
    }

    public void saveUserSub(usersub userSub) {
    	 
    	 
        userSubRepository.save(userSub);

    	
    	}
   
}
