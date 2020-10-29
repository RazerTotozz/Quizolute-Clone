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
@Table(name = "rooms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rooms.findAll", query = "SELECT r FROM Rooms r")
    , @NamedQuery(name = "Rooms.findById", query = "SELECT r FROM Rooms r WHERE r.id = :id")
    , @NamedQuery(name = "Rooms.findByName", query = "SELECT r FROM Rooms r WHERE r.name = :name")
    , @NamedQuery(name = "Rooms.findByInvitationCode", query = "SELECT r FROM Rooms r WHERE r.invitationCode = :invitationCode")
    , @NamedQuery(name = "Rooms.findByIsActive", query = "SELECT r FROM Rooms r WHERE r.isActive = :isActive")
    , @NamedQuery(name = "Rooms.findByCreatedAt", query = "SELECT r FROM Rooms r WHERE r.createdAt = :createdAt")})
public class Rooms implements Serializable {

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
    @Size(min = 1, max = 255)
    @Column(name = "invitation_code")
    private String invitationCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "question_set_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private QuestionSets questionSetId;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users ownerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private List<UserInRoom> userInRoomList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private List<UserAnswers> userAnswersList;

    public Rooms() {
    }

    public Rooms(Integer id) {
        this.id = id;
    }

    public Rooms(Integer id, String name, String invitationCode, boolean isActive, Date createdAt) {
        this.id = id;
        this.name = name;
        this.invitationCode = invitationCode;
        this.isActive = isActive;
        this.createdAt = createdAt;
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

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public Users getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Users ownerId) {
        this.ownerId = ownerId;
    }

    @XmlTransient
    public List<UserInRoom> getUserInRoomList() {
        return userInRoomList;
    }

    public void setUserInRoomList(List<UserInRoom> userInRoomList) {
        this.userInRoomList = userInRoomList;
    }

    @XmlTransient
    public List<UserAnswers> getUserAnswersList() {
        return userAnswersList;
    }

    public void setUserAnswersList(List<UserAnswers> userAnswersList) {
        this.userAnswersList = userAnswersList;
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
        if (!(object instanceof Rooms)) {
            return false;
        }
        Rooms other = (Rooms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.quizolute.model.Rooms[ id=" + id +" name : " +name +" invit : " +  invitationCode + " ]";
    }
    
}
