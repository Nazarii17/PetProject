package com.tkachuk.pet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotNull
    @NotBlank(message = "Please fill the name!")
    private String name;
    @Column(unique = true)
    @NotNull
    @NotBlank(message = "Please fill the website!")
    private String website;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    @Column
    private String address;
    @Column(unique = true, name = "phone_number")
    private String phoneNumber;
    @Column
    private double rating;
    @Column(length = 2048)
    @NotNull
    @NotBlank(message = "Please fill the Description!")
    @Size(max = 2048, message = "Description is to long!")
    private String description;
    @Column
    private String logo;
    @ElementCollection(targetClass = OrganizationType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "organization_type", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<OrganizationType> organizationTypes;
    @OneToMany
    @JoinColumn(name = "organization_id")
    private Set<OrganizationPhoto> organizationPhotos;

    public Organization(Long id,
                        @NotNull @NotBlank(message = "Please fill the name!") String name,
                        @NotNull @NotBlank(message = "Please fill the website!") String website,
                        User author,
                        String address,
                        String phoneNumber,
                        double rating,
                        @NotNull @NotBlank(message = "Please fill the Description!")
                        @Length(max = 2048, message = "Description is to long!") String description,
                        String logo,
                        Set<OrganizationType> organizationTypes
    ) {
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
