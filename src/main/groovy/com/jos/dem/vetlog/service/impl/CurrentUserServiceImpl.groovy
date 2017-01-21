package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service

import com.jos.dem.vetlog.model.Role
import com.jos.dem.vetlog.model.CurrentUser
import com.jos.dem.vetlog.service.CurrentUserService

@Service
class CurrentUserServiceImpl implements CurrentUserService {

  @Override
  boolean canAccessUser(CurrentUser currentUser, Long userId){
    !currentUser && (currentUser.role == Role.ADMIN || currentUser.id.equals(userId))
  }

}
