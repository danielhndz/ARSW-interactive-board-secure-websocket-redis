package edu.escuelaing.arsw.labs.securewsredis.repo;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketRepository {

    private Logger LOGGER = LoggerFactory.getLogger(TicketRepository.class);

    @Inject
    private StringRedisTemplate template;

    // Inject the template as ListOperations
    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> listTickets;

    private int ticketNumber;

    public synchronized Integer getTicket() {
        Integer ticket = ticketNumber++;
        listTickets.leftPush("ticketStore", ticket.toString());
        LOGGER.info("New ticket generated : {}", ticket);
        return ticket;
    }

    public boolean checkTicket(String ticket) {
        Long isValid = listTickets.getOperations().boundListOps("ticketStore").remove(0, ticket);
        return isValid > 0l;
    }
}
