/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Summary implements Serializable {

    private QuestionSets questionSet;
    private List<Answer> answerList;
    private final double totalScore;

    public Summary(QuestionSets questionSet) {
        this(questionSet, null);
    }

    public Summary(QuestionSets questionSet, List<Answer> answerList) {
        this.questionSet = questionSet;
        this.answerList = answerList;
        double sumOfScore = 0;
        for (Answer answer : answerList) {
            sumOfScore += answer.getScore();
        }
        this.totalScore = sumOfScore;
    }

    public QuestionSets getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(QuestionSets questionSet) {
        this.questionSet = questionSet;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public double getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        return "Summary{" + "questionSet=" + questionSet + ", answerList=" + answerList + ", totalScore=" + totalScore + '}';
    }
    
    

}
