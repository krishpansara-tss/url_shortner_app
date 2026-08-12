package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationProcessor {
    private final Map<String, NotificationService> notificationProcessors;

    public NotificationService getProcessor(String type) {
        NotificationService processor;
        if (type == null) {
            processor = notificationProcessors.get("sms");
            if (processor == null) {
                processor = notificationProcessors.get("email");
            }
        } else {
            processor = notificationProcessors.get(type);
        }

        if (processor == null) {
            throw new RuntimeException("Method not found");
        }
        return processor;
    }
}
