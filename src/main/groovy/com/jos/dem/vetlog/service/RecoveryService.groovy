package com.jos.dem.vetlog.service

import com.jos.dem.vetlog.model.User

interface RecoveryService {
  void sendConfirmationAccountToken(String email)
  User confirmAccountForToken(String token)
  void generateRegistrationCodeForEmail(String email)
}
