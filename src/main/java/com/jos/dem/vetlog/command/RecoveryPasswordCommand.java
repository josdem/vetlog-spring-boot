package com.jos.dem.vetlog.command;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RecoveryPasswordCommand implements Command {
    @Email
    @NotNull
    @Size(min=6, max=200)
    private String email;
}
