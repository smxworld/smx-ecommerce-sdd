-- Tabella richiesta da Spring Modulith (spring-modulith-starter-jpa)
-- per il tracking degli eventi di dominio pubblicati in modo asincrono.
CREATE TABLE IF NOT EXISTS event_publication (
    id               UUID                     NOT NULL,
    listener_id      TEXT                     NOT NULL,
    event_type       TEXT                     NOT NULL,
    serialized_event TEXT                     NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date  TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS event_publication_completion_date_idx
    ON event_publication (completion_date);
