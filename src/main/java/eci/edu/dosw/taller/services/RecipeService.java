package eci.edu.dosw.taller.services;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;

import java.util.List;

/**
 * Clase servicio que define los metodos de crud para las recetas
 */
public interface RecipeService {
    RecipeDTO create(CreateRecipeDTO dto);
    RecipeDTO createChef(CreateRecipeDTO dto);
    RecipeDTO createViewer(CreateRecipeDTO dto);
    RecipeDTO createParticipant(CreateRecipeDTO dto);
    List<RecipeDTO> findAll();
    RecipeDTO findByConsecutive(Integer consecutive);
    void delete(Integer consecutive);
    RecipeDTO update(Integer consecutive, CreateRecipeDTO dto);
}
