package com.tssconsultancy.url_sortner_app.services;

import com.tssconsultancy.url_sortner_app.services.implementation.NotificationProcessor;
import com.tssconsultancy.url_sortner_app.services.interfaces.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotificationProcessorTest {

    @Mock
    private NotificationService emailService;

    @Mock
    private NotificationService smsService;

    private NotificationProcessor notificationProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, NotificationService> processors = new HashMap<>();
        processors.put("email", emailService);
        processors.put("sms", smsService);
        notificationProcessor = new NotificationProcessor(processors);
    }

    @Test
    void testGetProcessorWithEmail() {
        NotificationService service = notificationProcessor.getProcessor("email");
        assertNotNull(service);
        assertEquals(emailService, service);
    }

    @Test
    void testGetProcessorWithSms() {
        NotificationService service = notificationProcessor.getProcessor("sms");
        assertNotNull(service);
        assertEquals(smsService, service);
    }

    @Test
    void testGetProcessorWithNullDefaultsToSmsOrEmail() {
        NotificationService service = notificationProcessor.getProcessor(null);
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
