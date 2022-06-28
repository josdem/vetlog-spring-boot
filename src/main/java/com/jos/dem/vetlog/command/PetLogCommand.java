package com.jos.dem.vetlog.command;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PetLogCommand implements Command {
    @Size(max = 200)
    private String vetName;

    @NotNull
    @Size(min = 1, max = 1000)
    private String symptoms;

    @NotNull
    @Size(min = 1, max = 1000)
    private String diagnosis;

    @Size(min = 1, max = 500)
    private String medicine;

    @NotNull
    @Min(1L)
    private Long pet;
}
