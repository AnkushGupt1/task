-- Enrollments domain schema. Owned by enrollments-core, applied from its own migration location.
-- Deliberately NO foreign key to catalog's `courses` table: the enrollments domain does not couple
-- to catalog's schema. The course relationship is enforced at the application layer via CoursesApi.
CREATE TABLE enrollments (
    id            UUID                     NOT NULL,
    course_id     UUID                     NOT NULL,
    visitor_name  VARCHAR(255)             NOT NULL,
    visitor_email VARCHAR(320)             NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_enrollments PRIMARY KEY (id)
);

-- Supports both the capacity count and the "list enrollments for a course" query.
CREATE INDEX idx_enrollments_course_id ON enrollments (course_id);
