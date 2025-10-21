package eci.edu.dosw.taller.services.impl;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;
import eci.edu.dosw.taller.enums.ChefRole;
import eci.edu.dosw.taller.mappers.RecipeMapper;
import eci.edu.dosw.taller.models.ChefFactory;
import eci.edu.dosw.taller.models.Recipe;
import eci.edu.dosw.taller.repositories.RecipeRepository;
import eci.edu.dosw.taller.services.RecipeService;
import eci.edu.dosw.taller.util.SequenceGeneratorService;
import eci.edu.dosw.taller.util.AppErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que se encarga del gestionamiento de las recetas para el programa de master cheff
 */
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final SequenceGeneratorService sequenceGenerator;
    private final RecipeMapper recipeMapper;

    @Override
    public RecipeDTO create(CreateRecipeDTO dto) {
        if (dto == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.INVALID_REQUEST_BODY);
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            String message = AppErrors.titleRequiredForRoleOrDefault(dto.getChefRole());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        ChefRole role = dto.getChefRole();
        if (role == null) {
            dto.setSeason(null);
        } else if (role == ChefRole.PARTICIPANT) {
            if (dto.getSeason() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        AppErrors.Messages.RECIPE_SEASON_REQUIRED_FOR_PARTICIPANT);
            }
        } else {
            dto.setSeason(null);
        }

        int consecutive = sequenceGenerator.getNextSequence("recipes_sequence");
        Instant now = Instant.now();
        Recipe entity = recipeMapper.toEntity(dto);
        entity.setChef(ChefFactory.fromDto(dto));
        entity.setConsecutive(consecutive);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        Recipe saved = repository.save(entity);
        return recipeMapper.toDTO(saved);
    }


    @Override
    public RecipeDTO createChef(CreateRecipeDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.INVALID_REQUEST_BODY);
        }
        dto.setChefRole(ChefRole.CHEF);
        return create(dto);
    }


    @Override
    public RecipeDTO createViewer(CreateRecipeDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.INVALID_REQUEST_BODY);
        }
        dto.setChefRole(ChefRole.VIEWER);
        return create(dto);
    }

    @Override
    public RecipeDTO createParticipant(CreateRecipeDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.INVALID_REQUEST_BODY);
        }
        if (dto.getSeason() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    AppErrors.Messages.RECIPE_SEASON_REQUIRED_FOR_PARTICIPANT);
        }
        dto.setChefRole(ChefRole.PARTICIPANT);
        return create(dto);
    }

    @Override
    public List<RecipeDTO> findAll() {
        return repository.findAll().stream()
                .map(recipeMapper::toDTO)
                .toList();
    }

    @Override
    public RecipeDTO findByConsecutive(Integer consecutive) {
        return repository.findByConsecutive(consecutive)
                .map(recipeMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(AppErrors.Messages.RECIPE_NOT_FOUND, consecutive)));
    }

    @Override
    public void delete(Integer consecutive) {
        var rec = repository.findByConsecutive(consecutive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(AppErrors.Messages.RECIPE_NOT_FOUND, consecutive)));
        repository.delete(rec);
    }


    @Override
    public RecipeDTO update(Integer consecutive, CreateRecipeDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.INVALID_REQUEST_BODY);
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.RECIPE_TITLE_REQUIRED);
        }

        Recipe existing = repository.findByConsecutive(consecutive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(AppErrors.Messages.RECIPE_NOT_FOUND, consecutive)));

        existing.setTitle(dto.getTitle());
        existing.setIngredients(dto.getIngredients());
        existing.setSteps(dto.getSteps());
        existing.setChef(ChefFactory.fromDto(dto));
        existing.setSeason(dto.getSeason());
        existing.setUpdatedAt(Instant.now());
        Recipe saved = repository.save(existing);
        return recipeMapper.toDTO(saved);
    }


    @Override
    public List<RecipeDTO> findByChefRole(ChefRole role) {
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.ROLE_REQUIRED);
        }
        return recipeMapper.toDTOList(repository.findByChefRole(role));
    }

    @Override
    public List<RecipeDTO> findBySeason(Integer season) {
        if (season == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.SEASON_REQUIRED);
        }
        return recipeMapper.toDTOList(repository.findBySeason(season));
    }

    @Override
    public List<RecipeDTO> findByIngredient(String ingredient) {
        if (ingredient == null || ingredient.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppErrors.Messages.INGREDIENT_REQUIRED);
        }
        return recipeMapper.toDTOList(repository.findByIngredients(ingredient));
    }


}
