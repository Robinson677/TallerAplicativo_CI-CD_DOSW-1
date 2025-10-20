package eci.edu.dosw.taller.controllers;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;
import eci.edu.dosw.taller.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase controlador para gentionar el CRUD de las recetas y sus participantes
 */
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService service;

    @Operation(summary = "Registrar una receta de un chef")
    @PostMapping("/chefs")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDTO createChefRecipe(@RequestBody @Valid CreateRecipeDTO dto) {
        return service.createChef(dto);
    }

    @Operation(summary = "Registrar una receta de un televidente")
    @PostMapping("/viewers")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDTO createViewerRecipe(@RequestBody @Valid CreateRecipeDTO dto) {
        return service.createViewer(dto);
    }

    @Operation(summary = "Registrar una receta de un participante")
    @PostMapping("/participants")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDTO createParticipantRecipe(@RequestBody @Valid CreateRecipeDTO dto) {
        return service.createParticipant(dto);
    }

    @Operation(summary = "Devolver todas las recetas guardadas ")
    @GetMapping
    public List<RecipeDTO> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Devolver cada receta por su Numero de consecutivo")
    @GetMapping("/{consecutive}")
    public RecipeDTO getByConsecutive(@PathVariable Integer consecutive) {
        return service.findByConsecutive(consecutive);
    }

    @Operation(summary = "Eliminar una receta")
    @DeleteMapping("/{consecutive}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer consecutive) {
        service.delete(consecutive);
    }

    @Operation(summary = "Actualizar una receta")
    @PutMapping("/{consecutive}")
    public RecipeDTO update(@PathVariable Integer consecutive, @RequestBody @Valid CreateRecipeDTO dto) {
        return service.update(consecutive, dto);
    }
}
