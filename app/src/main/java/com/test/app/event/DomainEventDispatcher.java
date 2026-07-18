package com.test.app.event;

import com.test.common.event.DomainEvent;
import com.test.common.event.DomainEventHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The single bridge between the transport (today: Spring in-process events) and the domains'
 * transport-agnostic {@link DomainEventHandler} beans.
 *
 * <p>This is deliberately the ONLY place in the codebase that mentions Spring's {@code @EventListener}.
 * Every producer publishes through {@link com.test.common.event.DomainEventPublisher} and
 * every consumer implements {@link DomainEventHandler}; neither knows how events travel. Swap the
 * transport for a broker and only this class (plus {@link SpringDomainEventPublisher}) is rewritten.
 */
@Component
class DomainEventDispatcher {

    private final List<DomainEventHandler<?>> handlers;

    DomainEventDispatcher(List<DomainEventHandler<?>> handlers) {
        this.handlers = handlers;
    }

    @EventListener
    void onDomainEvent(DomainEvent event) {
        for (DomainEventHandler<?> handler : handlers) {
            if (handler.eventType().isInstance(event)) {
                dispatch(handler, event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void dispatch(DomainEventHandler<?> handler, DomainEvent event) {
        ((DomainEventHandler<DomainEvent>) handler).handle(event);
    }
}
