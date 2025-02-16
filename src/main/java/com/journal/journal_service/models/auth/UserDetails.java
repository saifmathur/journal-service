package com.journal.journal_service.models.auth;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getAge() {
        return age;
    }


    private String email;
    private String DOB;

    public void setAge(String age) {
        this.age = age;
    }

    private String firstName;
    private String lastName;


    private String age;


    private String addressLine1;
    private String addressLine2;

    private String phone;

    @CreationTimestamp
    private LocalDateTime eventTime;


    @OneToOne(mappedBy = "userDetails")
    private User user;

//    @OneToOne(mappedBy = "user_id")
//    private User user;


    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDOB() {
        return DOB;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }



    public String getPhone() {
        return phone;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public User getUser() {
        return user;
    }
}
