package eci.edu.dosw.taller.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;

/**
 * Clase para modelar las recetas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "recipes")
public class Recipe {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer consecutive;
    private String title;
    @Indexed
    private List<String> ingredients;
    private List<String> steps;
    private Chef chef;
    private Integer season;
    private Instant createdAt;
    private Instant updatedAt;
}
