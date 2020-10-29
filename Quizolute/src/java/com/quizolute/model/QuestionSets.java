/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sittiwatlcp
 */
@Entity
@Table(name = "question_sets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuestionSets.findAll", query = "SELECT q FROM QuestionSets q")
    , @NamedQuery(name = "QuestionSets.findById", query = "SELECT q FROM QuestionSets q WHERE q.id = :id")
    , @NamedQuery(name = "QuestionSets.findByName", query = "SELECT q FROM QuestionSets q WHERE q.name = :name")
    , @NamedQuery(name = "QuestionSets.findByPublic1", query = "SELECT q FROM QuestionSets q WHERE q.public1 = :public1")
    , @NamedQuery(name = "QuestionSets.findByCreatedAt", query = "SELECT q FROM QuestionSets q WHERE q.createdAt = :createdAt")
    , @NamedQuery(name = "QuestionSets.findByUpdatedAt", query = "SELECT q FROM QuestionSets q WHERE q.updatedAt = :updatedAt")
    , @NamedQuery(name = "QuestionSets.findQuestionSetByIdAndPublic", query = "SELECT q FROM QuestionSets q WHERE q.public1 = :public1 OR q.ownerId = :ownerId")
})
public class QuestionSets implements Serializable {

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
    @Column(name = "public")
    private boolean public1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionSetId")
    private List<Rooms> roomsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionSetId")
    private List<Questions> questionsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionSetId")
    private List<UserAnswers> userAnswersList;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users ownerId;

    public QuestionSets() {
    }

    public QuestionSets(Integer id) {
        this.id = id;
    }

    public QuestionSets(Integer id, String name, boolean public1, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.public1 = public1;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public double getQuestionSetMaxScore(){
        double maxScore = 0;
        for (Questions questions : this.questionsList) {
            maxScore += questions.getMaxScore();
        }
        return maxScore;
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

    public boolean getPublic1() {
        return public1;
    }

    public void setPublic1(boolean public1) {
        this.public1 = public1;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @XmlTransient
    public List<Rooms> getRoomsList() {
        return roomsList;
    }

    public void setRoomsList(List<Rooms> roomsList) {
        this.roomsList = roomsList;
    }

    @XmlTransient
    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<Questions> questionsList) {
        this.questionsList = questionsList;
    }

    @XmlTransient
    public List<UserAnswers> getUserAnswersList() {
        return userAnswersList;
    }

    public void setUserAnswersList(List<UserAnswers> userAnswersList) {
        this.userAnswersList = userAnswersList;
    }

    public Users getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Users ownerId) {
        this.ownerId = ownerId;
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
        if (!(object instanceof QuestionSets)) {
            return false;
        }
        QuestionSets other = (QuestionSets) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.quizolute.model.QuestionSets[ id=" + id + " ]";
    }
    
}
