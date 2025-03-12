package com.ungman.sc.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "class")
public class ClassId {
   
   @Id
   @Column(name = "CLASS_ID")
   private Integer classId;
   
   @Column(name = "CLASS_PW")
   private String classPw;
   
   @Column(name = "CLASS_CALENDAR")
   private String classCalendarId;
   
}
