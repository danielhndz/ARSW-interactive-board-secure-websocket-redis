package edu.escuelaing.arsw.labs.securewsredis.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.escuelaing.arsw.labs.securewsredis.repo.TicketRepository;

@RestController
public class DrawingServiceController {

    @Inject
    public TicketRepository ticketRepository;

    @GetMapping("/status")
    public String status() {
        return "{\"status\":\"Greetings from Spring Boot. " +
                java.time.LocalDate.now() + ", " +
                java.time.LocalTime.now() +
                ". " + "The server is Runnig!\"}";
    }

    @GetMapping("/getTicket")
    public String getTicket() {
        return "{\"ticket\":\"" +
                ticketRepository.getTicket() + "\"}";
    }
}
