package com.trainingplatform.common.event;

/**
 * The seam a producer publishes through.
 *
 * <p>Producers ({@code catalog-core}, {@code enrollments-core}) depend only on this interface.
 * The only implementation shipped today is in-process (Spring {@code ApplicationEventPublisher},
 * wired in the {@code app} module). Swapping to a message broker means providing a different
 * implementation of this one method — <b>no producer code changes</b>.
 */
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
