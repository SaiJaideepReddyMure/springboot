package com.example.demo.service;

 
import java.util.List;

 
import org.springframework.stereotype.Service;

import com.example.demo.model.Questions;
 
import com.example.demo.reposistory.Jpadatabase;
import com.example.demo.reposistory.examdata_repo;

@Service
public class QuestionService {

    private final Jpadatabase jpadatabase;
    private final  examdata_repo examrepo;

     
    public QuestionService(Jpadatabase jpadatabase,examdata_repo examrepo) {
        this.jpadatabase = jpadatabase;
         this.examrepo = examrepo;;
    }

    public List<Questions> getAllQuestions() {
        return jpadatabase.findAll();
    }

    public Questions getQuestionById(int id) {
        java.util.Optional<Questions> optionalQuestion = jpadatabase.findById(id);
        return optionalQuestion.orElse(null);
    }

//    

	public void save(Questions question) {
	 
 		jpadatabase.save(question);
	}

	public void saveAll(List<Questions> questions) {
	 jpadatabase.saveAll(questions);
		
	}
	public List<Questions> getquestions(int exam_id)
	{
		return jpadatabase.data(exam_id);
	}
	  
	   
}