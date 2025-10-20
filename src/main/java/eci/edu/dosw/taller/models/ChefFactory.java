package eci.edu.dosw.taller.models;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.enums.ChefRole;
import lombok.NoArgsConstructor;

/**
 * Fabrica los tipos de chef
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

    /**
     * Crea un Chef a partir de nombre y rol
     */
    public static Chef fromParts(String name, ChefRole role) {
        return new Chef(name, role);
    }
}
