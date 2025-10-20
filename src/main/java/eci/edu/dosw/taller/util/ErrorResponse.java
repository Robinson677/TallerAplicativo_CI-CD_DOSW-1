package eci.edu.dosw.taller.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Muestra los errores cuando se este probando en swagger
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private final OffsetDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final List<String> details;
}
