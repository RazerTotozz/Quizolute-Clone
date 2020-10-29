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
public class Answer implements Serializable {

    private Questions question;
    private List<Choices> answerChoiceList;
    private boolean isCorrect = false;
    private final double score;

    public Answer(Questions question, List<Choices> answerChoiceList) {
        this.question = question;
        this.answerChoiceList = answerChoiceList;
        if (this.answerChoiceList != null) {
            int choiceCorrectCount = 0;
            int answerCorrectCount = 0;
            for (Choices choice : this.question.getChoicesList()) {
                if (choice.getIsCorrect()) {
                    choiceCorrectCount++;
                    for (Choices answerChoice : this.answerChoiceList) {
                        System.out.println(answerChoice.getId() == choice.getId());
                        if (answerChoice.getId() == choice.getId()) {
                            answerCorrectCount++;
                        }
                    }
                }
            }
            System.out.println("choice "+choiceCorrectCount);
            System.out.println("answer "+answerCorrectCount);
            System.out.println("----------------------------");
            if (choiceCorrectCount == answerCorrectCount) {
                this.isCorrect = true;
                double totalScore = 0;
                for (Choices choice : answerChoiceList) {
                    totalScore += choice.getScore();
                }
                this.score = totalScore;
            } else {
                this.isCorrect = false;
                this.score = 0;
            }

        } else {
            this.isCorrect = false;
            this.score = 0;
        }
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public List<Choices> getAnswerChoiceList() {
        return answerChoiceList;
    }

    public void setAnswerChoiceList(List<Choices> answerChoiceList) {
        this.answerChoiceList = answerChoiceList;
    }

    
    public boolean getIsCorrect() {
        return isCorrect;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Answer{" + "question=" + question + ", answerChoiceList=" + answerChoiceList + ", isCorrect=" + isCorrect + ", score=" + score + '}';
    }

}
