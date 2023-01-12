/*
Copyright 2023 Jose Morales joseluis.delacruz@gmail.com

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

import com.jos.dem.vetlog.enums.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@ToString
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true)
  private String firstname;

  @Column(nullable = true)
  private String lastname;

  @Column(nullable = true)
  private String email;

  @Column(nullable = true)
  private String mobile;

  @Column(nullable = false)
  @Enumerated(STRING)
  private Role role;

  @Column(nullable = false)
  private Boolean enabled = true;

  @Column(nullable = false)
  private Boolean accountNonExpired = true;

  @Column(nullable = false)
  private Boolean credentialsNonExpired = true;

  @Column(nullable = false)
  private Boolean accountNonLocked = true;

  @Column(nullable = false)
  private Date dateCreated = new Date();
}
