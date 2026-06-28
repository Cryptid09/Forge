package com.forge.infrastructure.event;

import com.forge.core.event.Event;
import com.forge.core.event.EventBus;
import com.forge.infrastructure.persistence.entity.EventEntity;
import com.forge.infrastructure.persistence.repository.JpaEventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class InMemoryEventBus implements EventBus {

    private final JpaEventRepository jpaEventRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemoryEventBus(JpaEventRepository jpaEventRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.jpaEventRepository = jpaEventRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(Event event) {
        // Persist event to DB
        EventEntity entity = new EventEntity(event);
        jpaEventRepository.save(entity);

        // Publish to in-memory listeners (like WebSockets)
        applicationEventPublisher.publishEvent(event);
    }
}
