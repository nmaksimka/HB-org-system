CREATE TABLE calendar_integrations (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    provider VARCHAR(20) NOT NULL,
    encrypted_access_token TEXT NOT NULL,
    calendar_id VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    UNIQUE(user_id, provider)
);
CREATE TABLE calendar_events (
    id UUID PRIMARY KEY,
    integration_id UUID NOT NULL REFERENCES calendar_integrations(id),
    user_id UUID NOT NULL,
    birthday_user_id UUID NOT NULL,
    external_event_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_calendar_events_user ON calendar_events(user_id);
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    topic VARCHAR(150) NOT NULL,
    event_type VARCHAR(150) NOT NULL,
    aggregate_id UUID NOT NULL,
    correlation_id VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    attempts INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    sent_at TIMESTAMPTZ
);
CREATE INDEX idx_calendar_outbox_status_created ON outbox_events(status, created_at);
