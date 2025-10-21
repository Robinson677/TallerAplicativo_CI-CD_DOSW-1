package eci.edu.dosw.taller.TallerAplicativo_CI_CD_DOSW_1;

import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;
import eci.edu.dosw.taller.enums.ChefRole;
import eci.edu.dosw.taller.mappers.RecipeMapper;
import eci.edu.dosw.taller.models.Chef;
import eci.edu.dosw.taller.models.Recipe;
import eci.edu.dosw.taller.repositories.RecipeRepository;
import eci.edu.dosw.taller.util.SequenceGeneratorService;
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


    @Test
    void shouldReturnAllRecipes() {
        Recipe recipe1 = Recipe.builder()
                .consecutive(1)
                .title("Ratatouille")
                .chef(new Chef("Remy", ChefRole.CHEF))
                .build();

        Recipe recipe2 = Recipe.builder()
                .consecutive(2)
                .title("Soufflé")
                .chef(new Chef("Colette", ChefRole.CHEF))
                .build();
        RecipeDTO dto1 = new RecipeDTO();
        dto1.setConsecutive(1);
        dto1.setTitle("Ratatouille");
        dto1.setChefName("Remy");
        RecipeDTO dto2 = new RecipeDTO();
        dto2.setConsecutive(2);
        dto2.setTitle("Soufflé");
        dto2.setChefName("Colette");
        when(repository.findAll()).thenReturn(List.of(recipe1, recipe2));
        when(recipeMapper.toDTO(recipe1)).thenReturn(dto1);
        when(recipeMapper.toDTO(recipe2)).thenReturn(dto2);
        List<RecipeDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ratatouille", result.get(0).getTitle());
        assertEquals("Soufflé", result.get(1).getTitle());
    }


    @Test
    void shouldUpdateRecipeSuccessfully() {
        int consecutive = 1;
        Recipe existing = Recipe.builder()
                .id("id-1")
                .consecutive(consecutive)
                .title("Tortilla")
                .ingredients(List.of("huevo", "sal"))
                .steps(List.of("mezclar", "freir"))
                .chef(new Chef("Chef Local", ChefRole.CHEF))
                .season(null)
                .createdAt(Instant.now().minusSeconds(60))
                .updatedAt(Instant.now().minusSeconds(60))
                .build();
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Tortilla Mejorada");
        dto.setIngredients(List.of("huevo", "sal", "papa"));
        dto.setSteps(List.of("pelar", "freir"));
        dto.setChefName("Chef Nuevo");
        dto.setChefRole(ChefRole.CHEF);
        dto.setSeason(null);
        RecipeDTO outDto = new RecipeDTO();
        outDto.setConsecutive(consecutive);
        outDto.setTitle(dto.getTitle());
        outDto.setChefName(dto.getChefName());
        outDto.setChefRole(dto.getChefRole());
        outDto.setIngredients(dto.getIngredients());
        outDto.setSteps(dto.getSteps());
        outDto.setUpdatedAt(Instant.now());
        when(repository.findByConsecutive(consecutive)).thenReturn(Optional.of(existing));
        when(repository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0)); // devuelve la entidad guardada
        when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(outDto);
        RecipeDTO result = service.update(consecutive, dto);

        assertNotNull(result);
        assertEquals(consecutive, result.getConsecutive());
        assertEquals("Tortilla Mejorada", result.getTitle());
        assertEquals("Chef Nuevo", result.getChefName());
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(repository, times(1)).save(captor.capture());
        Recipe saved = captor.getValue();
        assertEquals("Tortilla Mejorada", saved.getTitle());
        assertEquals(dto.getIngredients(), saved.getIngredients());
        assertEquals(dto.getSteps(), saved.getSteps());
        assertNotNull(saved.getUpdatedAt());
        assertEquals("Chef Nuevo", saved.getChef().getName());
        assertEquals(ChefRole.CHEF, saved.getChef().getRole());
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingMissingRecipe() {
        int consecutive = 99;
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("No existe");
        when(repository.findByConsecutive(consecutive)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.update(consecutive, dto));

        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains(String.valueOf(consecutive)) || ex.getMessage().contains(String.valueOf(consecutive)));
    }

    @Test
    void shouldNotFindByChef() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.findByChefRole(null));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void shouldFindByChefRole() {
        Recipe r = Recipe.builder()
                .consecutive(10)
                .title("Plato de prueba")
                .chef(new Chef("Chef Local", ChefRole.CHEF))
                .build();

        RecipeDTO dto = new RecipeDTO();
        dto.setConsecutive(10);
        dto.setTitle("Plato de prueba");
        dto.setChefRole(ChefRole.CHEF);
        dto.setChefName("Chef Local");

        when(repository.findByChefRole(ChefRole.CHEF)).thenReturn(List.of(r));
        when(recipeMapper.toDTOList(anyList())).thenReturn(List.of(dto));

        List<RecipeDTO> res = service.findByChefRole(ChefRole.CHEF);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(ChefRole.CHEF, res.get(0).getChefRole());
        verify(repository, times(1)).findByChefRole(ChefRole.CHEF);
        verify(recipeMapper, times(1)).toDTOList(anyList());
    }

    @Test
    void shouldNotFindBySeason() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.findBySeason(null));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void shouldFindBySeasonDto() {
        Recipe r = Recipe.builder().consecutive(20).season(2).title("Plato temporada 2").build();
        RecipeDTO dto = new RecipeDTO();
        dto.setConsecutive(20);
        dto.setSeason(2);
        dto.setTitle("Plato temporada 2");
        when(repository.findBySeason(2)).thenReturn(List.of(r));
        when(recipeMapper.toDTOList(anyList())).thenReturn(List.of(dto));
        List<RecipeDTO> res = service.findBySeason(2);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(Integer.valueOf(2), res.get(0).getSeason());
    }

    @Test
    void shouldNotFindByIngredient() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.findByIngredient(""));

        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void shouldFindByIngredient() {
        Recipe r = Recipe.builder().consecutive(30).title("Plato con maiz").ingredients(List.of("maiz", "sal")).build();
        RecipeDTO dto = new RecipeDTO();
        dto.setConsecutive(30);
        dto.setTitle("Plato con maiz");
        when(repository.findByIngredients("maiz")).thenReturn(List.of(r));
        when(recipeMapper.toDTOList(anyList())).thenReturn(List.of(dto));
        List<RecipeDTO> res = service.findByIngredient("maiz");

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(30, res.get(0).getConsecutive());

    }

}
