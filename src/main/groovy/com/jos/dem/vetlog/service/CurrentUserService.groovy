package com.jos.dem.vetlog.service

import com.jos.dem.vetlog.model.CurrentUser

interface CurrentUserService {
   boolean canAccessUser(CurrentUser currentUser, Long userId)
}
