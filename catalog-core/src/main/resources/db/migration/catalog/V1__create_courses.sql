-- Catalog domain schema. Owned by catalog-core; applied from its own migration location
-- (classpath:db/migration/catalog) so each domain's schema history lives with its code.
CREATE TABLE courses (
    id         UUID                     NOT NULL,
    title      VARCHAR(255)             NOT NULL,
    city       VARCHAR(255)             NOT NULL,
    capacity   INTEGER                  NOT NULL,
    status     VARCHAR(32)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_courses PRIMARY KEY (id)
);
