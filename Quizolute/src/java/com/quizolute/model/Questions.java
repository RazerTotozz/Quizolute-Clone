/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sittiwatlcp
 */
@Entity
@Table(name = "questions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Questions.findAll", query = "SELECT q FROM Questions q")
    , @NamedQuery(name = "Questions.findById", query = "SELECT q FROM Questions q WHERE q.id = :id")
    , @NamedQuery(name = "Questions.findByName", query = "SELECT q FROM Questions q WHERE q.name = :name")
    , @NamedQuery(name = "Questions.findByMaxScore", query = "SELECT q FROM Questions q WHERE q.maxScore = :maxScore")})
public class Questions implements Serializable {

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
    @Column(name = "max_score")
    private double maxScore;
    @JoinColumn(name = "question_set_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private QuestionSets questionSetId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionId")
    private List<Choices> choicesList;

    public Questions() {
    }

    public Questions(Integer id) {
        this.id = id;
    }

    public Questions(Integer id, String name, double maxScore) {
        this.id = id;
        this.name = name;
        this.maxScore = maxScore;
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

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public QuestionSets getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(QuestionSets questionSetId) {
        this.questionSetId = questionSetId;
    }

    @XmlTransient
    public List<Choices> getChoicesList() {
        return choicesList;
    }

    public void setChoicesList(List<Choices> choicesList) {
        this.choicesList = choicesList;
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
        if (!(object instanceof Questions)) {
            return false;
        }
        Questions other = (Questions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.quizolute.model.Questions[ id=" + id + " ]";
    }
    
}
