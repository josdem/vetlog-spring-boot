/*
Copyright 2022 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.model;

import com.jos.dem.vetlog.enums.PetStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@ToString
public class Pet {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime birthDate;

    @Column(nullable = false)
    private Boolean dewormed = false;

    @Column(nullable = false)
    private Boolean sterilized = false;

    @Column(nullable = false)
    private Boolean vaccinated = false;

    @Column(nullable = false)
    private Date dateCreated = new Date();

    @Column(nullable = false)
    @Enumerated(STRING)
    private PetStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_adoption_id")
    private PetAdoption adoption;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private User adopter;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_image_id")
    private List<PetBucket> images;

}
