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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")
    , @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username")
    , @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName")
    , @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName = :lastName")
    , @NamedQuery(name = "Users.findByType", query = "SELECT u FROM Users u WHERE u.type = :type")
    , @NamedQuery(name = "Users.findByActive", query = "SELECT u FROM Users u WHERE u.active = :active")
    , @NamedQuery(name = "Users.findByDisabled", query = "SELECT u FROM Users u WHERE u.disabled = :disabled")})
public class Users implements Serializable {

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(name = "disabled")
    private boolean disabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerId")
    private List<Rooms> roomsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<UserInRoom> userInRoomList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<UserAnswers> userAnswersList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerId")
    private List<QuestionSets> questionSetsList;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(Integer id, String username, String firstName, String lastName, String type, boolean active, boolean disabled) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.active = active;
        this.disabled = disabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @XmlTransient
    public List<Rooms> getRoomsList() {
        return roomsList;
    }

    public void setRoomsList(List<Rooms> roomsList) {
        this.roomsList = roomsList;
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

    @XmlTransient
    public List<QuestionSets> getQuestionSetsList() {
        return questionSetsList;
    }

    public void setQuestionSetsList(List<QuestionSets> questionSetsList) {
        this.questionSetsList = questionSetsList;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.quizolute.model.Users[ id=" + id + " ]";
    }
    
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
