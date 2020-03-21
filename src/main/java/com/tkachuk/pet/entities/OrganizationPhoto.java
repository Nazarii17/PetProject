package com.tkachuk.pet.entities;

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
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "organization_id")
//    private Organization organization;

}
