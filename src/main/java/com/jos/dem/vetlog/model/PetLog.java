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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
public class PetLog {

  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;

  @Column private String vetName;

  @Column(nullable = false)
  private String symptoms;

  @Column(nullable = false)
  private String diagnosis;

  private String medicine;

  @Column(nullable = false)
  private Date dateCreated = new Date();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pet_id")
  private Pet pet;
}
