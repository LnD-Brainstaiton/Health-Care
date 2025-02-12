package com.healthcare.notificationservice.domain.entity;

import com.healthcare.kafka.enums.ProcessedEventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PROCESSED_EVENT")
public class ProcessedEvent implements Serializable, Persistable<String> {

    @Id
    @Column(name = "EVENT_ID")
    private String eventId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ProcessedEventStatus status;

    @Column(name = "RETRIED_EVENT_ID")
    private String retriedEventId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    public ProcessedEvent() {
    }

    public ProcessedEvent(final String eventId) {
        this.eventId = eventId;
    }

    @Transient
    @Override
    public String getId() {
        return eventId;
    }

    /**
     * Ensures Hibernate always does an INSERT operation when save() is called.
     */
    @Transient
    @Override
    public boolean isNew() {
        return true;
    }
}
