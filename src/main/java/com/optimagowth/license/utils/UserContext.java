package com.optimagowth.license.utils;

import org.springframework.stereotype.Component;

/**
 * Хранит значения из HTTP заголовков из запроса, отправленного клиентом
 */
@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";

    public static final String AUTH_TOKEN = "Authorization";

    public static final String USER_ID = "tmx-user-id";

    public static final String ORGANIZATION_ID = "tmx-organization-id";

    private static String correlationId = new String();

    private static String authToken = new String();

    private static String userId = new String();

    private static String organizationId = new String();

    public static String getCorrelationId() {
        return correlationId;
    }

    public static void setCorrelationId(String cId) {
        correlationId = cId;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String aToken) {
        authToken = aToken;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String aUser) {
        userId = aUser;
    }

    public static String getOrganizationId() {
        return organizationId;
    }

    public static void setOrganizationId(String organization) {
        organizationId = organization;
    }
}
