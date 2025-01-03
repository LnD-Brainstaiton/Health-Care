package com.healthcare.notificationservice.service.impl.kafka;


import com.healthcare.kafka.enums.ProcessedEventStatus;
import com.healthcare.notificationservice.domain.entity.ProcessedEvent;
import com.healthcare.notificationservice.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProcessedEventRepositoryService {

    private final ProcessedEventRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProcessedEventStatus(String eventId) {
        ProcessedEvent processedEvent = repository.findByEventId(eventId);
        processedEvent.setStatus(ProcessedEventStatus.COMPLETED);
        repository.save(processedEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedProcessedEventStatus(String eventId) {
        ProcessedEvent processedEvent = new ProcessedEvent(eventId);
        processedEvent.setStatus(ProcessedEventStatus.FAILED);
        repository.save(processedEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateIfExistOrInit(final String eventId) {
        ProcessedEvent processedEvent = repository.findByEventId(eventId);
        if (Objects.isNull(processedEvent))
            processedEvent = new ProcessedEvent(eventId);

        processedEvent.setStatus(ProcessedEventStatus.FAILED);
        repository.save(processedEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRetriedProcessedEventStatus(String eventId) {
        ProcessedEvent processedEvent = new ProcessedEvent(UUID.randomUUID().toString());
        processedEvent.setRetriedEventId(eventId);
        processedEvent.setStatus(ProcessedEventStatus.FAILED);
        repository.save(processedEvent);
    }

}
