package com.trainingplatform.app.event;

import com.trainingplatform.common.event.DomainEvent;
import com.trainingplatform.common.event.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * The one and only implementation of {@link DomainEventPublisher} shipped today: it forwards
 * domain events onto Spring's in-process {@link ApplicationEventPublisher}.
 *
 * <p>This class is the entire "transport" for cross-domain notifications. Replacing in-process
 * events with a message broker means writing a different {@code DomainEventPublisher} (serialize +
 * send) and a matching consumer bridge — <b>producers and consumers do not change</b>, because they
 * only ever touch the {@code common} interfaces.
 */
@Component
class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher delegate;

    SpringDomainEventPublisher(ApplicationEventPublisher delegate) {
        this.delegate = delegate;
    }

    @Override
    public void publish(DomainEvent event) {
        delegate.publishEvent(event);
    }
}
