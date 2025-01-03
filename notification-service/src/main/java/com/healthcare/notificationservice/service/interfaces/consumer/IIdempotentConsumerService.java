package com.healthcare.notificationservice.service.interfaces.consumer;


import com.healthcare.kafka.domain.EventWrapper;
import com.healthcare.notificationservice.presenter.rest.event.NotificationEvent;


public interface IIdempotentConsumerService {

    void processIdempotentAndOutboxForNotification(EventWrapper<NotificationEvent> eventWrapper);


}
