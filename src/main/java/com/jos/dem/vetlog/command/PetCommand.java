package com.jos.dem.vetlog.command;

import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.PetImage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PetCommand implements Command {

    private Long id;

    @NotNull
    @Size(min=1, max=50)
    private String name;

    @NotNull
    @Size(min=10, max=10)
    private String birthDate;

    @NotNull
    private Boolean dewormed = false;

    @NotNull
    private Boolean sterilized = false;

    @NotNull
    private Boolean vaccinated = false;

    @NotNull
    @Min(1L)
    private Long breed;

    @Min(1L)
    private Long user;

    @Min(1L)
    private Long adopter;

    @NotNull
    private PetType type;

    private String uuid;

    private PetStatus status;

    private MultipartFile image;

    private List<PetImage> images;
}
