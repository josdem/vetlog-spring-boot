package com.jos.dem.vetlog.service

import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command

interface RecoveryService {
  void sendConfirmationAccountToken(String email)
  User confirmAccountForToken(String token)
  void generateRegistrationCodeForEmail(String email)
  Boolean validateToken(String token)
  User changePassword(Command command)
}
