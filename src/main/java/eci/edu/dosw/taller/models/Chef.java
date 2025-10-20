package eci.edu.dosw.taller.models;

import eci.edu.dosw.taller.enums.ChefRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que modela al chef por su tipo y nombre
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chef {
    private String name;
    private ChefRole role;
}
