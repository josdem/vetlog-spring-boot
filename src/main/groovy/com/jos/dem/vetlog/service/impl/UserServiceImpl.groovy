/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.core.context.SecurityContextHolder

import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.UserBinder
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.repository.UserRepository

@Service
class UserServiceImpl implements UserService {

  @Autowired
  UserBinder userBinder
  @Autowired
  UserRepository userRepository
  @Autowired
  RecoveryService recoveryService

  User getByUsername(String username){
    userRepository.findByUsername(username)
  }

  User getByEmail(String email){
    userRepository.findByEmail(email)
  }

  @Transactional
  User save(Command command){
    User user = userBinder.bindUser(command)
    userRepository.save(user)
    recoveryService.sendConfirmationAccountToken(user.email)
    user
  }

  User getCurrentUser(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication()
    String username = auth.getName()
    userRepository.findByUsername(username)
  }

}
