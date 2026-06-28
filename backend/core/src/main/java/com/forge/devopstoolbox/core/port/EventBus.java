package com.forge.devopstoolbox.core.port;

import com.forge.devopstoolbox.core.model.PlatformEvent;
import java.util.function.Consumer;

public interface EventBus {

    void publish(PlatformEvent event);

    void subscribe(Consumer<PlatformEvent> listener);
}
