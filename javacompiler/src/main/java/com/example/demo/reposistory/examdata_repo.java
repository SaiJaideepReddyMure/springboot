package com.example.demo.reposistory;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.exam_data;

public interface examdata_repo extends JpaRepository<exam_data,Integer>
{
	@Query(value = "SELECT DISTINCT e.class_id " +
            "FROM questions q " +
            "INNER JOIN exam_data e ON q.exam_id = e.exam_id " +
            "WHERE q.exam_id = ?1", nativeQuery = true)
String findClassIdsForExam(int examId);
    @Query(value="SELECT timeact from exam_data where exam_id = ?1",nativeQuery = true)
	LocalDateTime findtimeactBy(int exam_ids);
    @Query(value="SELECT timer from exam_data where exam_id=?1",nativeQuery = true)
	int gettimer(Integer exam_id);
    @Query(value = "select no_questions from exam_data where exam_id= ?1",nativeQuery = true)
	int getnoofquestions(int exam_id);

}
