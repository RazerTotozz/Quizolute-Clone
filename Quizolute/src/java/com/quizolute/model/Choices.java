/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sittiwatlcp
 */
@Entity
@Table(name = "choices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Choices.findAll", query = "SELECT c FROM Choices c")
    , @NamedQuery(name = "Choices.findById", query = "SELECT c FROM Choices c WHERE c.id = :id")
    , @NamedQuery(name = "Choices.findByName", query = "SELECT c FROM Choices c WHERE c.name = :name")
    , @NamedQuery(name = "Choices.findByScore", query = "SELECT c FROM Choices c WHERE c.score = :score")
    , @NamedQuery(name = "Choices.findByIsCorrect", query = "SELECT c FROM Choices c WHERE c.isCorrect = :isCorrect")})
public class Choices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "score")
    private double score;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_correct")
    private boolean isCorrect;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Questions questionId;

    public Choices() {
    }

    public Choices(Integer id) {
        this.id = id;
    }

    public Choices(Integer id, String name, double score, boolean isCorrect) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.isCorrect = isCorrect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Questions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Questions questionId) {
        this.questionId = questionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Choices)) {
            return false;
        }
        Choices other = (Choices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.quizolute.model.Choices[ id=" + id + " ]";
    }
    
}
