package edu.escuelaing.arsw.labs.securewsredis.endpoint;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.escuelaing.arsw.labs.securewsredis.context.BBAppContextAware;
import edu.escuelaing.arsw.labs.securewsredis.repo.TicketRepository;

@Component
@ServerEndpoint("/bbService")
public class BBEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BBEndPoint.class);
    // Queue for all open WebSocket sessions
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    Session ownSession = null;

    private boolean accepted = false;

    // This code allows to include a bean directly from the application context
    TicketRepository ticketRepository = (TicketRepository) BBAppContextAware
            .getApplicationContext()
            .getBean("ticketRepository");

    // Call this method to send a message to all clients
    public void send(String msg) {
        try {
            /* Send updates to all open WebSocket sessions */
            for (Session session : queue) {
                if (!session.equals(this.ownSession)) {
                    session.getBasicRemote().sendText(msg);
                }
                LOGGER.info("Sent: {}", msg);
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @OnMessage
    public void processPoint(String message, Session session) {
        LOGGER.info("Point received: {}. From session: {}", message, session);
        if (accepted) {
            this.send(message);
        } else {
            if (ticketRepository.checkTicket(message)) {
                accepted = true;
            } else {
                try {
                    ownSession.close();
                } catch (IOException e) {
                    LOGGER.debug(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @OnOpen
    public void openConnection(Session session) {
        // Register this connection in the queue
        queue.add(session);
        ownSession = session;
        LOGGER.info("Connection opened.");
        try {
            session.getBasicRemote().sendText("Connection established.");
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        // Remove this connection from the queue
        queue.remove(session);
        LOGGER.info("Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        LOGGER.info(t.getMessage());
        LOGGER.info("Connection error.");
    }

}
