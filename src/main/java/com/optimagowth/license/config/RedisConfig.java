package com.optimagowth.license.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ServiceConfig serviceConfig;

    /**
     * Создание фабрики подключений к серверу Redis
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        String hostName = serviceConfig.getRedisServer();
        int port = Integer.parseInt(serviceConfig.getRedisPort());
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(hostName, port);
        return new JedisConnectionFactory(configuration);
    }

    /**
     * Создание RedisTemplate для взаимодействия с сервером Redis
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
}
