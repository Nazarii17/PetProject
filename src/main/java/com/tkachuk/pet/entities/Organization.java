package com.tkachuk.pet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
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
    @ElementCollection(targetClass = OrganizationType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "organization_type", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<OrganizationType> organizationTypes;
    @OneToMany()
    @JoinColumn(name = "organization_id")
    private Set<OrganizationPhoto> organizationPhotos;

    public Organization(Long id, @NotNull @NotBlank(message = "Please fill the name!") String name, @NotNull @NotBlank(message = "Please fill the website!") String website, User author, String address, String phoneNumber, double rating, @NotNull @NotBlank(message = "Please fill the Description!") @Length(max = 2048, message = "Description is to long!") String description, String logo, Set<OrganizationType> organizationTypes) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.author = author;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.description = description;
        this.logo = logo;
        this.organizationTypes = organizationTypes;
    }
}
