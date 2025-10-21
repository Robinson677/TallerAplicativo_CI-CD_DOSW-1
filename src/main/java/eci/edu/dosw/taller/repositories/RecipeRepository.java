package eci.edu.dosw.taller.repositories;

import eci.edu.dosw.taller.models.Recipe;
import eci.edu.dosw.taller.enums.ChefRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Intefaz para el repositorio de resetas
 */
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Optional<Recipe> findByConsecutive(Integer consecutive);
    @Query("{ 'chef.role': ?0 }")
    List<Recipe> findByChefRole(ChefRole role);
    List<Recipe> findBySeason(Integer season);
    @Query("{ 'ingredients': { $regex: ?0, $options: 'i' } }")
    List<Recipe> findByIngredients(String ingredient);
}
