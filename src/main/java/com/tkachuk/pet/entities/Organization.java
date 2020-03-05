package com.tkachuk.pet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(unique = true, name = "name")
    @NotNull
    @NotBlank(message = "Please fill the name!")
    private String name;
    @Column(unique = true, name = "website")
    @NotNull
    @NotBlank(message = "Please fill the website!")
    private String website;
    @Column(name = "address")
    private String address;
    @Column(unique = true, name = "phone_number")
    private String phoneNumber;
    @Column(name = "rating")
    private double rating;
    @Column(name = "description")
    @NotNull
    @NotBlank(message = "Please fill the Description!")
    @Length(max = 2048, message = "Description is to long!")
    private String description;
    @Column(name = "logo")
    private String logo;
//    @Column()
//    private Set<OrganizationType> organizationTypes


}
