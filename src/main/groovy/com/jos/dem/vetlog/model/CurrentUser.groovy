package com.jos.dem.vetlog.model

import org.springframework.security.core.authority.AuthorityUtils

class CurrentUser extends org.springframework.security.core.userdetails.User {

  private User user

  CurrentUser(User user) {
    super(user.username, user.password, AuthorityUtils.createAuthorityList(user.role.toString()))
    this.user = user
  }

}
