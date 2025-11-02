package com.demo.quizapp.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class QuizDTO {
    private int id;
    private String title;
    private String category;
    private int numOfQuestions;
    private List<QuestionWapper> questions;

    public QuizDTO(Quiz quiz) {
        this.id = quiz.getId();
        this.title = quiz.getTitle();
        this.numOfQuestions = quiz.getQuestions().size();
        this.category = (numOfQuestions > 0) ? quiz.getQuestions().get(0).getCategory() : "N/A";
        this.questions = new ArrayList<>();
    }

    public QuizDTO(Quiz quiz, List<QuestionWapper> listQuestions) {
        this.id = quiz.getId();
        this.title = quiz.getTitle();
        this.numOfQuestions = quiz.getQuestions().size();
        this.category = (numOfQuestions > 0) ? quiz.getQuestions().get(0).getCategory() : "N/A";
        this.questions = listQuestions;

    }

}