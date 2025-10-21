package eci.edu.dosw.taller.models;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;

/**
 * Fabrica los tipos de chefs
 */
public final class ChefFactory {

    /**
     * Constructor privado para no instaciar y cumplir con factory
     */
    private ChefFactory() {}

    /**
     * Crea un Chef a partir de un dto
     */
    public static Chef fromDto(CreateRecipeDTO dto) {
        if (dto == null) return null;
        return new Chef(dto.getChefName(), dto.getChefRole());
    }
}
