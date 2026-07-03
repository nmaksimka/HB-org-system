CREATE TABLE mock_collections (
    id UUID PRIMARY KEY,
    fundraiser_id UUID NOT NULL UNIQUE,
    owner_id UUID NOT NULL,
    target_amount NUMERIC(19,2) NOT NULL CHECK (target_amount > 0),
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE mock_payments (
    id UUID PRIMARY KEY,
    collection_id UUID NOT NULL REFERENCES mock_collections(id),
    payer_id UUID NOT NULL,
    amount NUMERIC(19,2) NOT NULL CHECK (amount > 0),
    status VARCHAR(20) NOT NULL,
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_mock_payments_collection ON mock_payments(collection_id);

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

CREATE INDEX idx_mock_bank_outbox_status_created ON outbox_events(status, created_at);
