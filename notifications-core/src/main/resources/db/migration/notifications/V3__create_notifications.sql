-- Notifications domain schema. Owned by notifications-core, applied from its own migration location.
-- No foreign keys to catalog or enrollments tables: cross-domain identifiers are stored as plain
-- UUIDs, consistent with the other domains keeping their schemas independent.
CREATE TABLE notifications (
    id              UUID                     NOT NULL,
    enrollment_id   UUID                     NOT NULL,
    course_id       UUID                     NOT NULL,
    recipient_email VARCHAR(320)             NOT NULL,
    channel         VARCHAR(32)              NOT NULL,
    body            VARCHAR(1000)            NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE INDEX idx_notifications_recipient_email ON notifications (recipient_email);
