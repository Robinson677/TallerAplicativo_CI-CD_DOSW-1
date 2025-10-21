package eci.edu.dosw.taller.mappers;


import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;
import eci.edu.dosw.taller.models.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

/***
 * Interfaz que mapea el dto y entity para la receta
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Mapping(source = "chef.name", target = "chefName")
    @Mapping(source = "chef.role", target = "chefRole")
    RecipeDTO toDTO(Recipe recipe);
    List<RecipeDTO> toDTOList(List<Recipe> recipes);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consecutive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "chefName", target = "chef.name")
    @Mapping(source = "chefRole", target = "chef.role")
    Recipe toEntity(CreateRecipeDTO dto);
}