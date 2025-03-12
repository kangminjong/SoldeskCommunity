package com.ungman.sc.user;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User { 

   @Id
   @Column(name = "EMAIL")
   private String email;

   @Column(name = "PASSWORD")
   private String pw;

   @Column(name = "NAME")   
   private String name;

   @Column(name = "TEL")
   private String tel;

   @Column(name = "PROFILE_IMAGE")
   private String photo;

   @Column(name = "BIRTH_DATE")
   private Date birthday;

   @Column(name = "ADDRESS")
   private String addr;

   @Column(name = "CLASS_ID")
   private Integer classId;  // classId

   @Column(name = "ROLE")
   private String role;

   @Column(name = "IS_DELETED")
   private String isDeleted;  // 삭제 여부
}
