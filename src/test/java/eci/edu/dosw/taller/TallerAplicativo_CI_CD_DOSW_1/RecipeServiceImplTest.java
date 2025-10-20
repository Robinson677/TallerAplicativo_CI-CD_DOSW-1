package eci.edu.dosw.taller.TallerAplicativo_CI_CD_DOSW_1;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;
import eci.edu.dosw.taller.enums.ChefRole;
import eci.edu.dosw.taller.mappers.RecipeMapper;
import eci.edu.dosw.taller.models.Chef;
import eci.edu.dosw.taller.models.Recipe;
import eci.edu.dosw.taller.repositories.RecipeRepository;
import eci.edu.dosw.taller.services.SequenceGeneratorService;
import eci.edu.dosw.taller.services.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository repository;

    @Mock
    private SequenceGeneratorService sequenceGenerator;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeServiceImpl service;

    @Test
    void shouldCreateRecipe() {
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Tortilla de patatas");
        dto.setIngredients(List.of("patata", "huevo", "sal"));
        dto.setSteps(List.of("pelar patatas", "freir", "mezclar con huevo"));
        dto.setChefName("Juan Perez");
        dto.setChefRole(ChefRole.VIEWER);

        Recipe mapped = Recipe.builder()
                .title(dto.getTitle())
                .ingredients(dto.getIngredients())
                .steps(dto.getSteps())
                .chef(new Chef(dto.getChefName(), dto.getChefRole()))
                .build();

        when(recipeMapper.toEntity(any(CreateRecipeDTO.class))).thenReturn(mapped);
        when(sequenceGenerator.getNextSequence("recipes_sequence")).thenReturn(42);
        when(repository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe r = invocation.getArgument(0);
            r.setId("mongo-id-123");
            return r;
        });

        RecipeDTO expectedDto = new RecipeDTO();
        expectedDto.setConsecutive(42);
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(expectedDto);
        RecipeDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(42, result.getConsecutive());
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(repository, times(1)).save(captor.capture());
        Recipe saved = captor.getValue();

        assertEquals(42, saved.getConsecutive());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(saved.getCreatedAt(), saved.getUpdatedAt());
        assertEquals(dto.getTitle(), saved.getTitle());
        assertEquals(dto.getIngredients(), saved.getIngredients());
    }

    @Test
    void shouldCreateChefRecipe() {
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Beef Wellington");
        dto.setIngredients(List.of("beef", "mushrooms", "puff pastry"));
        dto.setSteps(List.of("season", "sear", "bake"));
        dto.setChefName("Gordon");
        when(recipeMapper.toEntity(any(CreateRecipeDTO.class))).thenAnswer(invocation -> {
            CreateRecipeDTO arg = invocation.getArgument(0);
            return Recipe.builder()
                    .title(arg.getTitle())
                    .ingredients(arg.getIngredients())
                    .steps(arg.getSteps())
                    .chef(new Chef(arg.getChefName(), arg.getChefRole()))
                    .build();
        });
        when(sequenceGenerator.getNextSequence("recipes_sequence")).thenReturn(100);
        when(repository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe r = invocation.getArgument(0);
            r.setId("mongo-chef-100");
            return r;
        });
        RecipeDTO outDto = new RecipeDTO();
        outDto.setConsecutive(100);
        outDto.setTitle("Beef Wellington");
        outDto.setChefName("Gordon");
        outDto.setChefRole(ChefRole.CHEF);
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(outDto);
        RecipeDTO result = service.createChef(dto);

        assertNotNull(result);
        assertEquals(100, result.getConsecutive());
        assertEquals("Beef Wellington", result.getTitle());
        assertEquals(ChefRole.CHEF, result.getChefRole());
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(repository, times(1)).save(captor.capture());
        Recipe saved = captor.getValue();
        assertEquals(100, saved.getConsecutive());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertNotNull(saved.getChef());
        assertEquals(ChefRole.CHEF, saved.getChef().getRole());
    }

    @Test
    void shouldCreateViewerRecipe() {
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Confit Byaldi");
        dto.setIngredients(List.of("eggplant", "zucchini", "tomato", "bell pepper"));
        dto.setSteps(List.of("slice vegetables", "layer", "bake"));
        dto.setChefName("Anton Ego"); // treated here as a viewer posting a recipe
        when(recipeMapper.toEntity(any(CreateRecipeDTO.class))).thenAnswer(invocation -> {
            CreateRecipeDTO arg = invocation.getArgument(0);
            return Recipe.builder()
                    .title(arg.getTitle())
                    .ingredients(arg.getIngredients())
                    .steps(arg.getSteps())
                    .chef(new Chef(arg.getChefName(), arg.getChefRole()))
                    .build();
        });
        when(sequenceGenerator.getNextSequence("recipes_sequence")).thenReturn(200);
        when(repository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe r = invocation.getArgument(0);
            r.setId("mongo-viewer-200");
            return r;
        });
        RecipeDTO outDto = new RecipeDTO();
        outDto.setConsecutive(200);
        outDto.setTitle("Confit Byaldi");
        outDto.setChefName("Anton Ego");
        outDto.setChefRole(ChefRole.VIEWER);
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(outDto);
        RecipeDTO result = service.createViewer(dto);

        assertNotNull(result);
        assertEquals(200, result.getConsecutive());
        assertEquals("Confit Byaldi", result.getTitle());
        assertEquals(ChefRole.VIEWER, result.getChefRole());
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(repository, times(1)).save(captor.capture());
        Recipe saved = captor.getValue();
        assertEquals(200, saved.getConsecutive());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertNotNull(saved.getChef());
        assertEquals(ChefRole.VIEWER, saved.getChef().getRole());
    }

    @Test
    void shouldCreateParticipantRecipe() {
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Ratatouille Classic");
        dto.setIngredients(List.of("tomato", "eggplant", "zucchini", "onion"));
        dto.setSteps(List.of("prepare sauce", "arrange vegetables", "bake"));
        dto.setChefName("Alfredo Linguini");
        dto.setSeason(1);
        when(recipeMapper.toEntity(any(CreateRecipeDTO.class))).thenAnswer(invocation -> {
            CreateRecipeDTO arg = invocation.getArgument(0);
            return Recipe.builder()
                    .title(arg.getTitle())
                    .ingredients(arg.getIngredients())
                    .steps(arg.getSteps())
                    .chef(new Chef(arg.getChefName(), arg.getChefRole()))
                    .season(arg.getSeason())
                    .build();
        });
        when(sequenceGenerator.getNextSequence("recipes_sequence")).thenReturn(300);
        when(repository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe r = invocation.getArgument(0);
            r.setId("mongo-participant-300");
            return r;
        });
        RecipeDTO outDto = new RecipeDTO();
        outDto.setConsecutive(300);
        outDto.setTitle("Ratatouille Classic");
        outDto.setChefName("Alfredo Linguini");
        outDto.setChefRole(ChefRole.PARTICIPANT);
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(outDto);
        RecipeDTO result = service.createParticipant(dto);

        assertNotNull(result);
        assertEquals(300, result.getConsecutive());
        assertEquals("Ratatouille Classic", result.getTitle());
        assertEquals(ChefRole.PARTICIPANT, result.getChefRole());
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(repository, times(1)).save(captor.capture());
        Recipe saved = captor.getValue();
        assertEquals(Integer.valueOf(1), saved.getSeason());
        assertNotNull(saved.getChef());
        assertEquals(ChefRole.PARTICIPANT, saved.getChef().getRole());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void ShouldNotCreateNullDto() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(null));

        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void shouldFindByConsecutive() {
        int consecutive = 7;
        Recipe recipe = Recipe.builder()
                .id("id-7")
                .consecutive(consecutive)
                .title("Sopa")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        when(repository.findByConsecutive(consecutive)).thenReturn(Optional.of(recipe));
        RecipeDTO dto = new RecipeDTO();
        dto.setConsecutive(consecutive);
        when(recipeMapper.toDTO(recipe)).thenReturn(dto);
        RecipeDTO result = service.findByConsecutive(consecutive);

        assertNotNull(result);
        assertEquals(consecutive, result.getConsecutive());
    }

    @Test
    void shouldNotFindByConsecutive() {
        int consecutive = 999;
        when(repository.findByConsecutive(consecutive)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.findByConsecutive(consecutive));

        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains(String.valueOf(consecutive)) || ex.getMessage().contains(String.valueOf(consecutive)));
    }

    @Test
    void shouldDeleteExistingConsecutiveRecipe() {
        int consecutive = 3;
        Recipe recipe = Recipe.builder().id("r-3").consecutive(consecutive).build();
        when(repository.findByConsecutive(consecutive)).thenReturn(Optional.of(recipe));
        service.delete(consecutive);

        verify(repository, times(1)).delete(recipe);
    }

    @Test
    void shouldNotDeleteNotFoundConsecutiveRecipe() {
        int consecutive = 404;
        when(repository.findByConsecutive(consecutive)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.delete(consecutive));

        assertEquals(404, ex.getStatusCode().value());
    }
}
