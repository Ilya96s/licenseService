package com.optimagowth.license.utils;

import org.springframework.util.Assert;

/**
 * Служит для сохранения UserContext в переменной ThreadLocal, доступной любым методам, вызываемым в потоке, который
 * обрабатывает запрос пользователя
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    public static final UserContext getContext() {
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public static final void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    public static final UserContext createEmptyContext() {
        return new UserContext();
    }
}
