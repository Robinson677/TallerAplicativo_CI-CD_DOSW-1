package eci.edu.dosw.taller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import eci.edu.dosw.taller.enums.ChefRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/***
 * Clase dto que maneja la informacion de la creacion de recetas
 */
@Data
public class CreateRecipeDTO {

    @NotBlank
    private String title;
    @NotEmpty
    private List<String> ingredients;
    @NotEmpty
    private List<String> steps;
    @NotBlank
    private String chefName;
    @Schema
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ChefRole chefRole;
    @Schema
    private Integer season;
}
