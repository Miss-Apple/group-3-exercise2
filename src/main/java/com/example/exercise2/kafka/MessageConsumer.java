package com.example.exercise2.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.exercise2.cargo.model.Container;
import com.example.exercise2.cargo.model.ContainerDto;
import com.example.exercise2.cargo.service.ContainerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageConsumer {
    @Autowired
    private ContainerService containerService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // For parsing JSON strings

    @KafkaListener(topics = "cargo-events", groupId = "tracking-service-group")
    public void listen(String message) {
        try {

            ContainerDto dto = objectMapper.readValue(message, ContainerDto.class);
            System.out.println("DTO: " + dto);

            Container container = new Container();

            container.setContainerNumber(dto.getContainerNumber());
            container.setDestination(dto.getDestination());
            container.setOrigin(dto.getOrigin());
            container.setWeight(dto.getWeight());

            containerService.addContainer(container);

            System.out.println("Inserted new container : " + container);

        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }

    }

}