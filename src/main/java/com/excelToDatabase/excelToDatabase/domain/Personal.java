package com.excelToDatabase.excelToDatabase.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "personal")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Personal {
    @Id
    private Integer personalId;
    private String firstName;
    private String lastName;
    private Integer phoneNumber;
    private String email;
    private String address;

}
