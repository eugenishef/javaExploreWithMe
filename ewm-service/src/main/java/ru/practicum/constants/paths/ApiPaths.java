package ru.practicum.constants.paths;

public final class ApiPaths {
    public static final String ADMIN_CATEGORIES_BASE_PATH = "/admin/categories";
    public static final String PUBLIC_CATEGORIES_BASE_PATH = "/categories";
    public static final String CATEGORY_ID_PATH = "/{categoryId}";

    public static final String ADMIN_COMPILATIONS_BASE_PATH = "/admin/compilations";
    public static final String PUBLIC_COMPILATIONS_BASE_PATH = "/compilations";
    public static final String COMPILATION_ID_PATH = "/{compId}";

    public static final String ADMIN_EVENTS_BASE_PATH = "/admin/events";
    public static final String EVENT_ID_PATH = "/{eventId}";

    public static final String USER_EVENTS_BASE_PATH = "/users/{userId}/events";
    public static final String EVENT_REQUESTS_PATH = "/{eventId}/requests";

    public static final String PUBLIC_EVENTS_BASE_PATH = "/events";

    public static final String USER_REQUESTS_BASE_PATH = "/users/{userId}/requests";
    public static final String REQUEST_ID_PATH = "/{requestId}/cancel";

    public static final String ADMIN_USERS_BASE_PATH = "/admin/users";
    public static final String USER_ID_PATH = "/{userId}";

    private ApiPaths() {

    }
}
