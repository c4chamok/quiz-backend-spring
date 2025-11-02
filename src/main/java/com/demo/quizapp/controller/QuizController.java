package com.demo.quizapp.controller;

import com.demo.quizapp.entity.Response;
import com.demo.quizapp.entity.QuizResult;
import com.demo.quizapp.service.QuizService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.demo.quizapp.entity.QuizDTO;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Create Quiz
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(
            @RequestParam String category,
            @RequestParam int numQ,
            @RequestParam String title) {
        return quizService.creatQuiz(category, numQ, title);
    }

    // Get Quiz Questions
    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizQuestions(@PathVariable Integer id) {
        return quizService.getQuizQuestions(id);
    }
    //get all quizzes
    @GetMapping("/allQuizzes")
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzeList = quizService.fetchAllQuizzes();
        return new ResponseEntity<>(quizzeList, HttpStatus.OK);
    }

    // Submit Quiz and Calculate Result
    @PostMapping("/submit/{id}")
    public ResponseEntity<QuizResult> submitQuiz(
            @PathVariable Integer id,
            @RequestBody List<Response> responses) {
        return quizService.calculateQuiz(id, responses);
    }
}

