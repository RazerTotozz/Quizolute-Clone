/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quizolute.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sittiwatlcp
 */
@Entity
@Table(name = "user_answers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAnswers.findAll", query = "SELECT u FROM UserAnswers u")
    , @NamedQuery(name = "UserAnswers.findById", query = "SELECT u FROM UserAnswers u WHERE u.id = :id")
    , @NamedQuery(name = "UserAnswers.findByCreatedAt", query = "SELECT u FROM UserAnswers u WHERE u.createdAt = :createdAt")})
public class UserAnswers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "data_obj")
    private byte[] dataObj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "question_set_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private QuestionSets questionSetId;
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Rooms roomId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public UserAnswers() {
    }

    public UserAnswers(Integer id) {
        this.id = id;
    }

    public UserAnswers(Integer id, byte[] dataObj, Date createdAt) {
        this.id = id;
        this.dataObj = dataObj;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getDataObj() {
        return dataObj;
    }

    public void setDataObj(byte[] dataObj) {
        this.dataObj = dataObj;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public QuestionSets getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(QuestionSets questionSetId) {
        this.questionSetId = questionSetId;
    }

    public Rooms getRoomId() {
        return roomId;
    }

    public void setRoomId(Rooms roomId) {
        this.roomId = roomId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
        if (!(object instanceof UserAnswers)) {
            return false;
        }
        UserAnswers other = (UserAnswers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.quizolute.model.UserAnswers[ id=" + id + " ]";
    }
    
}
