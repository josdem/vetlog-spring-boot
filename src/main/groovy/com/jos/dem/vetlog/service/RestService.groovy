package com.jos.dem.vetlog.service

import com.jos.dem.vetlog.command.Command

interface RestService {
  void sendCommand(Command message, String template)
}
