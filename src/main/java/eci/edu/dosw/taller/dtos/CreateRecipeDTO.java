package eci.edu.dosw.taller.dtos;

import eci.edu.dosw.taller.enums.ChefRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
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
    private ChefRole chefRole;
    @Schema
    private Integer season;
    @AssertTrue
    public boolean isSeasonValid() {
        return chefRole != null && chefRole != eci.edu.dosw.taller.enums.ChefRole.PARTICIPANT || season != null;
    }
}
