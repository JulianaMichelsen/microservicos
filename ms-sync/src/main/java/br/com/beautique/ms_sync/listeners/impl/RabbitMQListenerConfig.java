package br.com.beautique.ms_sync.listeners.impl;

import br.com.beautique.ms_sync.dtos.appointments.FullAppointmentDTO;
import br.com.beautique.ms_sync.dtos.beautyprocedures.BeautyProcedureDTO;
import br.com.beautique.ms_sync.dtos.customers.CustomerDTO;
import br.com.beautique.ms_sync.listeners.ListenerConfig;
import br.com.beautique.ms_sync.utils.SyncLogger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableRabbit
public class RabbitMQListenerConfig implements ListenerConfig {

    private final ObjectMapper objectMapper;

    public RabbitMQListenerConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "customerQueue")
    @Override
    public void listenToCustomerQueue(String message) {
        try {
            CustomerDTO customer = objectMapper.readValue(message, CustomerDTO.class);
            //Sync data here...
            SyncLogger.info("Message received from queue customerQueue: " + customer.toString());
        } catch (Exception e){
            SyncLogger.error("Error listen customer queue: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "appointmentQueue")
    @Override
    public void listenToAppointmentQueue(String message) {
        try {
            FullAppointmentDTO appointments = objectMapper.readValue(message, FullAppointmentDTO.class);
            //Sync data here...
            SyncLogger.info("Message received from queue appointmentQueue: " + appointments.toString());
        } catch (Exception e){
            SyncLogger.error("Error listen appointment queue: " + e.getMessage());
        }

    }

    @RabbitListener(queues = "beautyProcedureQueue")
    @Override
    public void listenToBeautyProcedure(String message) {
        try {
            BeautyProcedureDTO beautyProcedures = objectMapper.readValue(message, BeautyProcedureDTO.class);
            //Sync data here...
            SyncLogger.info(("Message received from queue beautyProcedureQueue: " + beautyProcedures.toString()));
        } catch (Exception e){
            SyncLogger.error("Error listen beautyprocedure queue: " + e.getMessage());
        }

    }
}
