package eci.edu.dosw.taller.TallerAplicativo_CI_CD_DOSW_1;

import eci.edu.dosw.taller.controllers.RecipeController;
import eci.edu.dosw.taller.dtos.CreateRecipeDTO;
import eci.edu.dosw.taller.dtos.RecipeDTO;
import eci.edu.dosw.taller.enums.ChefRole;
import eci.edu.dosw.taller.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeControllerTest {

    @InjectMocks
    private RecipeController controller;

    @Mock
    private RecipeService service;

    private CreateRecipeDTO createDto;
    private RecipeDTO recipeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createDto = new CreateRecipeDTO();
        createDto.setTitle("Tortilla");
        createDto.setIngredients(List.of("huevo", "sal", "patata"));
        createDto.setSteps(List.of("pelar", "freir", "batir"));
        createDto.setChefName("Robinson Steven Nuñez");
        createDto.setChefRole(ChefRole.VIEWER);
        recipeDto = new RecipeDTO();
        recipeDto.setConsecutive(1);
        recipeDto.setTitle("Tortilla");
        recipeDto.setIngredients(createDto.getIngredients());
        recipeDto.setSteps(createDto.getSteps());
        recipeDto.setChefName(createDto.getChefName());
        recipeDto.setChefRole(createDto.getChefRole());
        recipeDto.setCreatedAt(Instant.now());
        recipeDto.setUpdatedAt(Instant.now());
    }

    @Test
    void shouldCreateChefRecipe() {
        RecipeDTO chefRecipe = new RecipeDTO();
        chefRecipe.setConsecutive(2);
        chefRecipe.setTitle("Ratatouille");
        chefRecipe.setChefName("Chef Linguini");
        chefRecipe.setChefRole(ChefRole.CHEF);
        when(service.createChef(any(CreateRecipeDTO.class))).thenReturn(chefRecipe);
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Ratatouille");
        dto.setIngredients(List.of("arroz", "caldo", "setas"));
        dto.setSteps(List.of("tostar", "añadir caldo", "remover"));
        dto.setChefName("Chef Linguini");
        RecipeDTO result = controller.createChefRecipe(dto);

        assertNotNull(result);
        assertEquals(2, result.getConsecutive());
        assertEquals("Ratatouille", result.getTitle());
        assertEquals(ChefRole.CHEF, result.getChefRole());
    }

    @Test
    void shouldCreateViewerRecipe() {
        RecipeDTO chefRecipe = new RecipeDTO();
        chefRecipe.setConsecutive(7);
        chefRecipe.setTitle("Pasta");
        chefRecipe.setChefName("Televidente Ratatouille");
        chefRecipe.setChefRole(ChefRole.VIEWER);
        when(service.createViewer(any(CreateRecipeDTO.class))).thenReturn(chefRecipe);
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Pasta");
        dto.setIngredients(List.of("arroz", "pasta", "agua", "tomate"));
        dto.setSteps(List.of("calentar agua y echar pasta", "cortoar tomate y chearselo", "revolver", "esperar que este listo"));
        dto.setChefName("Televidente Ratatouille");
        RecipeDTO result = controller.createViewerRecipe(dto);

        assertNotNull(result);
        assertEquals(7, result.getConsecutive());
        assertEquals("Pasta", result.getTitle());
        assertEquals(ChefRole.VIEWER, result.getChefRole());
    }

    @Test
    void shouldCreateParticipantRecipe() {
        RecipeDTO chefRecipe = new RecipeDTO();
        chefRecipe.setConsecutive(7);
        chefRecipe.setTitle("Caldo de Pajarilla");
        chefRecipe.setChefName("Participante RobinHood");
        chefRecipe.setChefRole(ChefRole.PARTICIPANT);
        when(service.createParticipant(any(CreateRecipeDTO.class))).thenReturn(chefRecipe);
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Caldo de Pajarilla");
        dto.setIngredients(List.of("pimenton", "verduras", "agua", "pajarilla", "papas"));
        dto.setSteps(List.of("pelar papas", "hacer el hogao", "limiar viseras", "juntar todo en una olla", "cocinar por 30 min"));
        dto.setChefName("Participante RobinHood");
        RecipeDTO result = controller.createParticipantRecipe(dto);

        assertNotNull(result);
        assertEquals(7, result.getConsecutive());
        assertEquals("Caldo de Pajarilla", result.getTitle());
        assertEquals(ChefRole.PARTICIPANT, result.getChefRole());
    }



    @Test
    void shouldGetAllRecipes() {
        when(service.findAll()).thenReturn(List.of(recipeDto));
        List<RecipeDTO> result = controller.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tortilla", result.get(0).getTitle());
    }

    @Test
    void shouldGetRecipeByConsecutive() {
        when(service.findByConsecutive(1)).thenReturn(recipeDto);
        RecipeDTO result = controller.getByConsecutive(1);

        assertNotNull(result);
        assertEquals(1, result.getConsecutive());
        assertEquals("Tortilla", result.getTitle());
    }

    @Test
    void shouldDeleteRecipe() {
        controller.delete(1);
        verify(service, times(1)).delete(1);
    }

    @Test
    void shouldReturn404WhenRecipeNotFound() {
        when(service.findByConsecutive(999)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Receta con consecutivo 999 no encontrada"));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> controller.getByConsecutive(999));

        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getMessage().contains("Receta") || ex.getReason().contains("999"));
    }

    @Test
    void shouldUpdateRecipe() {
        CreateRecipeDTO dto = new CreateRecipeDTO();
        dto.setTitle("Tortilla Mejorada");
        dto.setIngredients(List.of("huevo", "sal"));
        dto.setSteps(List.of("mezclar", "freir"));
        dto.setChefName("Robinson");
        RecipeDTO updated = new RecipeDTO();
        updated.setConsecutive(1);
        updated.setTitle("Tortilla Mejorada");
        when(service.update(eq(1), any(CreateRecipeDTO.class))).thenReturn(updated);
        RecipeDTO result = controller.update(1, dto);

        assertNotNull(result);
        assertEquals(1, result.getConsecutive());
        assertEquals("Tortilla Mejorada", result.getTitle());
        verify(service, times(1)).update(eq(1), any(CreateRecipeDTO.class));
    }

    @Test
    void shouldGetParticipantsRecipesController() {
        RecipeDTO r = new RecipeDTO();
        r.setConsecutive(10);
        r.setTitle("Plato participante Martin");
        r.setChefRole(ChefRole.PARTICIPANT);
        when(service.findByChefRole(ChefRole.PARTICIPANT)).thenReturn(List.of(r));
        List<RecipeDTO> res = controller.getParticipantsRecipes();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(ChefRole.PARTICIPANT, res.get(0).getChefRole());
    }

    @Test
    void shouldGetViewersRecipesController() {
        RecipeDTO r = new RecipeDTO();
        r.setConsecutive(11);
        r.setTitle("Plato televidente ELIF");
        r.setChefRole(ChefRole.VIEWER);
        when(service.findByChefRole(ChefRole.VIEWER)).thenReturn(List.of(r));
        List<RecipeDTO> res = controller.getViewersRecipes();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(ChefRole.VIEWER, res.get(0).getChefRole());
    }

    @Test
    void shouldGetChefsRecipesController() {
        RecipeDTO r = new RecipeDTO();
        r.setConsecutive(12);
        r.setTitle("Plato chef Linguini");
        r.setChefRole(ChefRole.CHEF);
        when(service.findByChefRole(ChefRole.CHEF)).thenReturn(List.of(r));
        List<RecipeDTO> res = controller.getChefsRecipes();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(ChefRole.CHEF, res.get(0).getChefRole());
    }

    @Test
    void shouldGetBySeasonController() {
        RecipeDTO r = new RecipeDTO();
        r.setConsecutive(20);
        r.setTitle("Plato temporada 3");
        r.setSeason(3);
        when(service.findBySeason(3)).thenReturn(List.of(r));
        List<RecipeDTO> res = controller.getBySeason(3);

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(Integer.valueOf(3), res.get(0).getSeason());
    }

    @Test
    void shouldSearchByIngredientController() {
        RecipeDTO r = new RecipeDTO();
        r.setConsecutive(30);
        r.setTitle("Plato con maiz");
        when(service.findByIngredient("maiz")).thenReturn(List.of(r));
        List<RecipeDTO> res = controller.searchByIngredient("maiz");

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(30, res.get(0).getConsecutive());
    }

}
