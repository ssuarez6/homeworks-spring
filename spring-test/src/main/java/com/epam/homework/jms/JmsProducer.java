package com.epam.homework.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class JmsProducer {

    private final static Logger logger = LoggerFactory.getLogger(JmsProducer.class);

    @Value("${spring.tickets.queue}")
    private String ticketsQueueName;

    private final JmsTemplate template;

    public JmsProducer(JmsTemplate template) {
        this.template = template;
    }

    public void produceMessage(TicketMessage message) {
        try {
            template.convertAndSend(message);
        } catch (Exception ex) {
            logger.error("There was an error trying to produce the message", ex);
        }
    }
}
