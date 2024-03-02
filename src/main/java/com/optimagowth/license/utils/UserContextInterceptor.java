package com.optimagowth.license.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Добавляет идентификатор корреляции в любые исходящие HTTP запросы к нижестоящим службам, которые
 * отправляются экземпляром RestTemplate
 * Это необходимо для того, чтобы дать возможность увидеть связь между вызовами служб
 *
 * Чтобы задействовать UserContextInterceptor нужно определить bean-компонент RestTemplate
 * и добавить в него UserContextInterceptor. Для этого определил свой bean компонент RestTemplate в классе LicenseServiceApplication
 */
@Slf4j
public class UserContextInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        return execution.execute(request, body);
    }
}
