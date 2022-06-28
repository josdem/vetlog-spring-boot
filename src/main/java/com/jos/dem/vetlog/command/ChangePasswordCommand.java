package com.jos.dem.vetlog.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordCommand implements Command {

    @NotNull
    @Size(min=32, max=32)
    private String token;

    @NotNull
    @Size(min=8, max=50)
    private String password;

    @NotNull
    @Size(min=8, max=50)
    private String passwordConfirmation;
}
