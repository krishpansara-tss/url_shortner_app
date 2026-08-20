package com.tssconsultancy.url_sortner_app.services;

import com.tssconsultancy.url_sortner_app.services.implementation.NotificationProcessor;
import com.tssconsultancy.url_sortner_app.services.interfaces.INotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotificationProcessorTest {

    @Mock
    private INotificationService emailService;

    @Mock
    private INotificationService smsService;

    private NotificationProcessor notificationProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, INotificationService> processors = new HashMap<>();
        processors.put("email", emailService);
        processors.put("sms", smsService);
        notificationProcessor = new NotificationProcessor(processors);
    }

    @Test
    void testGetProcessorWithEmail() {
        INotificationService service = notificationProcessor.getProcessor("email");
        assertNotNull(service);
        assertEquals(emailService, service);
    }

    @Test
    void testGetProcessorWithSms() {
        INotificationService service = notificationProcessor.getProcessor("sms");
        assertNotNull(service);
        assertEquals(smsService, service);
    }

    @Test
    void testGetProcessorWithNullDefaultsToSmsOrEmail() {
        INotificationService service = notificationProcessor.getProcessor(null);
        assertNotNull(service);
        assertEquals(smsService, service);
    }

    @Test
    void testGetProcessorInvalidTypeThrowsException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            notificationProcessor.getProcessor("push");
        });
        assertEquals("Method not found", exception.getMessage());
    }
}
