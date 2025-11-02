package com.demo.quizapp.service;

import com.demo.quizapp.entity.*;
import com.demo.quizapp.repository.QuestionRepository;
import com.demo.quizapp.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public List<QuizDTO> fetchAllQuizzes() {
        List<QuizDTO> quizzes = quizRepository.findAll().stream().map(quiz -> new QuizDTO(quiz)).collect(Collectors.toList());
        return quizzes;
    }

    public ResponseEntity<String> creatQuiz(String category, int numQ, String title) {
        List<Question> question = questionRepository.findRandomQuestionByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(question);
        quizRepository.save(quiz);
        return new ResponseEntity<>("Quiz Created", HttpStatus.CREATED);

    }

    @Transactional
    public ResponseEntity<QuizDTO> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWapper> questionsForUser = new ArrayList<>();
        for (Question q : questionsFromDB) {
            QuestionWapper qw = new QuestionWapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(new QuizDTO(quiz.get(), questionsForUser), HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<QuizResult> calculateQuiz(Integer id, List<Response> responses) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        List<Question> questions = quiz.get().getQuestions();

        int right = 0;
        int size = Math.min(responses.size(), questions.size());

        for (int i = 0; i < size; i++) {
            String userResponse = responses.get(i).getResponse();
            int questionId = responses.get(i).getId();
            System.out.println("userResponse : "+responses.get(i));
            Question question = questions.stream()
                    .filter(q -> q.getId() == questionId)
                    .findFirst()
                    .orElse(null);
            System.out.println("question from db: "+question);

            boolean isOption = userResponse.equals(question.getOption1())
                    || userResponse.equals(question.getOption2())
                    || userResponse.equals(question.getOption3())
                    || userResponse.equals(question.getOption4());

            System.out.println("User Response: " + userResponse + ", Right Answer: " + question.getRightAnswer() + ", isOption: " + isOption);
            if (userResponse.equals(question.getRightAnswer())) {
                right++;
                responses.get(i).setCheck("correct answer");

            } else if (!isOption) {
                responses.get(i).setCheck("This is not a valid option. Select a valid option.");

            } else {
                responses.get(i).setCheck("Wrong answer");
                responses.get(i).setCorrectAnswer(question.getRightAnswer());
            }
        }

        QuizResult result = new QuizResult(responses, right);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
