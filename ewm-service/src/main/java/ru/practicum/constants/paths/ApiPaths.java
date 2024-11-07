package ru.practicum.constants.paths;

public final class ApiPaths {
    public static final String ADMIN_CATEGORIES_BASE_PATH = "/admin/categories";
    public static final String PUBLIC_CATEGORIES_BASE_PATH = "/categories";
    public static final String CATEGORY_ID_PATH = "/{category-id}";

    public static final String ADMIN_COMPILATIONS_BASE_PATH = "/admin/compilations";
    public static final String PUBLIC_COMPILATIONS_BASE_PATH = "/compilations";
    public static final String COMPILATION_ID_PATH = "/{comp-id}";

    public static final String ADMIN_EVENTS_BASE_PATH = "/admin/events";
    public static final String EVENT_ID_PATH = "/{event-id}";

    public static final String USER_EVENTS_BASE_PATH = "/users/{user-id}/events";
    public static final String EVENT_REQUESTS_PATH = "/{event-id}/requests";

    public static final String PUBLIC_EVENTS_BASE_PATH = "/events";

    public static final String USER_REQUESTS_BASE_PATH = "/users/{user-id}/requests";
    public static final String REQUEST_ID_PATH = "/{request-id}/cancel";

    public static final String ADMIN_USERS_BASE_PATH = "/admin/users";
    public static final String USER_ID_PATH = "/{user-id}";

    private ApiPaths() {
    }

}
