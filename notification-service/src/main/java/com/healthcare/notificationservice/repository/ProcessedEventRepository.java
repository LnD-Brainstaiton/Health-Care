package com.healthcare.notificationservice.repository;



import com.healthcare.notificationservice.domain.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {

    ProcessedEvent findByEventId(String eventId);

}
