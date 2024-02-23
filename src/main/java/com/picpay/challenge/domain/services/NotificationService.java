package com.picpay.challenge.domain.services;

import com.picpay.challenge.domain.models.User;
import com.picpay.challenge.dtos.NotificationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class NotificationService {

    @Value("${picpay.api.url.notification}")
    private String NOTIFICATION_URL;

    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendNotification(final User user, final String message) {
        final String email = user.getEmail();

        final NotificationDTO notificationRequest = new NotificationDTO(email, message);

        final ResponseEntity<String> notificationResponse = this.restTemplate
                .postForEntity(this.NOTIFICATION_URL, notificationRequest, String.class);

        if (!notificationResponse.getStatusCode().equals(HttpStatus.OK)) {
            log.error("Erro ao enviar notificação");

            throw new RuntimeException("O serviço de notificação está fora do ar.");
        }
    }

}
