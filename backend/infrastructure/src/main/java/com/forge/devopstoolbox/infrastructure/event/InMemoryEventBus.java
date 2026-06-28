package com.forge.devopstoolbox.infrastructure.event;

import com.forge.devopstoolbox.core.model.PlatformEvent;
import com.forge.devopstoolbox.core.port.EventBus;
import com.forge.devopstoolbox.core.port.EventRepository;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class InMemoryEventBus implements EventBus {

    private final EventRepository eventRepository;
    private final List<Consumer<PlatformEvent>> listeners = new CopyOnWriteArrayList<>();

    public InMemoryEventBus(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void publish(PlatformEvent event) {
        PlatformEvent persisted = eventRepository.save(event);
        for (Consumer<PlatformEvent> listener : listeners) {
            listener.accept(persisted);
        }
    }

    @Override
    public void subscribe(Consumer<PlatformEvent> listener) {
        listeners.add(listener);
    }
}
