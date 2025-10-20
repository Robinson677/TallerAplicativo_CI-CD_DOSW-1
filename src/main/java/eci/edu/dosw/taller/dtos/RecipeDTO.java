package eci.edu.dosw.taller.dtos;

import eci.edu.dosw.taller.enums.ChefRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Clase dto que maneja la informacion de la receta
 */
@Data
public class RecipeDTO {

    @Schema(description = "Número consecutivo público")
    private Integer consecutive;

    private String title;
    private List<String> ingredients;
    private List<String> steps;
    private String chefName;
    private ChefRole chefRole;
    private Integer season;
    private Instant createdAt;
    private Instant updatedAt;
}
