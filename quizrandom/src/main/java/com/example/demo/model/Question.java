
package com.example.demo.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
 
 

@Entity
@Table(name = "Question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String correctanswer;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 

	 
   

	public String getText() {
		return text;
	}

	public void setText(String questionText) {
		this.text = questionText;
	}

	public String getOption_a() {
		return option_a;
	}

	public void setOption_a(String option_a) {
		this.option_a = option_a;
	}

	public String getOption_b() {
		return option_b;
	}

	public void setOption_b(String option_b) {
		this.option_b = option_b;
	}

	public String getOption_c() {
		return option_c;
	}

	public void setOption_c(String option_c) {
		this.option_c = option_c;
	}

	public String getOption_d() {
		return option_d;
	}

	public void setOption_d(String option_d) {
		this.option_d = option_d;
	}

	public String getCorrectanswer() {
		return correctanswer;
	}

	public void setCorrectanswer(String correctanswer) {
		this.correctanswer = correctanswer;
	}
	public List<String> getOptions() {
        return Arrays.asList(option_a, option_b, option_c, option_d);
    }

	public void setOptions(List<String>  options) {
		this.option_a = options.get(0);
        this.option_b = options.get(1);
        this.option_c = options.get(2);
        this.option_d = options.get(3);
	}

	public List<String> shuffleOptions() {

	    List<String> options = Arrays.asList(option_a, option_b, option_c, option_d);

	    Collections.shuffle(options);

	    return options;
     
}
}