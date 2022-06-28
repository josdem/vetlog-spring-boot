package com.jos.dem.vetlog.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdoptionCommand implements Command {

    @NotNull
    private String uuid;

    @NotNull
    @Size(min=1, max=1000)
    private String description;
}
