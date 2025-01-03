package com.healthcare.notificationservice.service.impl.consumer;


import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.kafka.exception.DuplicateEventException;
import com.healthcare.notificationservice.domain.entity.ProcessedEvent;
import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;
import com.healthcare.notificationservice.repository.ProcessedEventRepository;
import com.healthcare.notificationservice.service.interfaces.INotificationService;
import com.healthcare.notificationservice.service.interfaces.consumer.IIdempotentConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class IIdempotentConsumerServiceImpl  implements IIdempotentConsumerService {
    private final ProcessedEventRepository processedEventRepository;

    private final INotificationService notificationService;


    public void processIdempotentAndOutboxForNotification(EventWrapper<NotificationEvent> eventWrapper) {
        deduplicate(eventWrapper.getEventId());

        try {
            notificationService.processDynamicNotification(eventWrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void deduplicate(String eventId) throws DuplicateEventException {

        try {
            processedEventRepository.saveAndFlush(new ProcessedEvent(eventId));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEventException(eventId);
        }
    }

}
