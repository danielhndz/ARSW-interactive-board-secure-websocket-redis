package edu.escuelaing.arsw.labs.securewsredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class BBConfigurator {

    @Bean
    ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
