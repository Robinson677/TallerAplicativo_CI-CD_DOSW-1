package eci.edu.dosw.taller.util;

import java.util.Objects;

/**
 * Clase central que agrupa busquedas y validaciones para lanzar execpciones
 */

public final class AppErrors {

    private AppErrors() {
        throw new UnsupportedOperationException("Utility class");
    }


    public static final class Messages {
        private Messages() {}
        public static final String RECIPE_NOT_FOUND = "Receta con consecutivo %s no encontrada";
        public static final String RECIPE_TITLE_REQUIRED = "El campo title es obligatorio";
        public static final String RECIPE_TITLE_REQUIRED_FOR_ROLE = "El campo title es obligatorio para %s";
        public static final String RECIPE_INGREDIENTS_REQUIRED = "El campo ingredients es obligatorio";
        public static final String RECIPE_STEPS_REQUIRED = "El campo steps es obligatorio";
        public static final String RECIPE_SEASON_REQUIRED_FOR_PARTICIPANT = "season es obligatorio para chefRole PARTICIPANT";
        public static final String INVALID_REQUEST_BODY = "Request body inválido o nulo";
        public static final String ROLE_REQUIRED = "El rol no puede ser nulo";
        public static final String SEASON_REQUIRED = "season es requerido";
        public static final String INGREDIENT_REQUIRED = "ingredient es requerido";
    }


    /**
     * Retorna mensaje contextual para title según el rol del chef
     */
    public static String titleRequiredForRoleOrDefault(Object role) {
        if (role == null) {
            return Messages.RECIPE_TITLE_REQUIRED;
        }
        String roleName = role.toString().toLowerCase();
        return String.format(Messages.RECIPE_TITLE_REQUIRED_FOR_ROLE, roleName);
    }

    /**
     * Clase de excepciones base
     */
    public static class BaseAppException extends RuntimeException {
        public BaseAppException(String message) { super(message); }
        public BaseAppException(String message, Throwable cause) { super(message, cause); }
    }

    /**
     * Clase de excepciones para cosas no encontradas
     */
    public static class NotFound extends BaseAppException {
        public NotFound(String message) { super(message); }
    }

    /**
     * Clase de excepciones para cosas mal implementadas
     */
    public static class BadRequest extends BaseAppException {
        public BadRequest(String message) { super(message); }
    }

    /**
     * Clase de excepciones para objetos no encontrados
     * @param format
     * @param args
     * @return
     */
    public static NotFound notFoundFmt(String format, Object... args) {
        return new NotFound(String.format(Objects.toString(format, ""), args));
    }

    /**
     * Clase de excepciones para objetos mal implementadas
     * @param format
     * @param args
     * @return
     */
    public static BadRequest badRequestFmt(String format, Object... args) {
        return new BadRequest(String.format(Objects.toString(format, ""), args));
    }
}
