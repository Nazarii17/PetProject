package com.tkachuk.pet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_photo")
public class OrganizationPhoto {

    @Id
    @GeneratedValue
    @Column
    private Long id;
    @Column
    private String name;

}
