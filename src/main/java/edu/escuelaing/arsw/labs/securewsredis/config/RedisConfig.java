package edu.escuelaing.arsw.labs.securewsredis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@PropertySource("application.properties")
public class RedisConfig {

    @Value("${redis.cache.hostname}")
    private String redisHostName;

    @Value("${redis.cache.port}")
    private int redisPort;

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(redisHostName, redisPort));
        return connectionFactory;
    }

}
