package com.example.listener;

import com.example.dto.NotificationDto;
import com.example.service.ClientService;
import com.example.service.ManagerService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private MessageHelper messageHelper;
    private ClientService clientService;
    private ManagerService managerService;

    public NotificationListener(MessageHelper messageHelper, ClientService clientService, ManagerService managerService) {
        this.messageHelper = messageHelper;
        this.clientService = clientService;
        this.managerService = managerService;
    }

    @JmsListener(destination = "${destination.incrementReservationCount}", concurrency = "5-10")
    public void notification(Message message) throws JMSException {
        NotificationDto notificationDto = messageHelper.getMessage(message, NotificationDto.class);
        System.out.println(notificationDto);
        //userService.incrementReservationCount(incrementReservationCountDto);
    }
}
