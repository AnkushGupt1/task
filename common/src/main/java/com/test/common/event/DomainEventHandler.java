package com.test.common.event;

/**
 * The seam a consumer handles through.
 *
 * <p>A consumer implements this as a plain Spring {@code @Component}; it never references Spring's
 * {@code @EventListener} or any broker client. The {@code app} module owns the single adapter that
 * bridges the transport (today: in-process Spring events) to these handlers, so moving to a broker
 * changes only that adapter — <b>no consumer code changes</b>.
 *
 * @param <E> the concrete event type this handler consumes
 */
public interface DomainEventHandler<E extends DomainEvent> {

    void handle(E event);

    /**
     * The event type this handler subscribes to. Used by the transport adapter to route events
     * without the handler needing any transport-specific annotation.
     */
    Class<E> eventType();
}
