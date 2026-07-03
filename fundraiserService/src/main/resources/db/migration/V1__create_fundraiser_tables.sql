CREATE TABLE fundraisers (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    beneficiary_id UUID NOT NULL,
    gift_id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    target_amount NUMERIC(19,2) NOT NULL CHECK (target_amount > 0),
    collected_amount NUMERIC(19,2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_url VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_fundraisers_owner ON fundraisers(owner_id);
CREATE INDEX idx_fundraisers_beneficiary ON fundraisers(beneficiary_id);

CREATE TABLE fundraiser_participants (
    id UUID PRIMARY KEY,
    fundraiser_id UUID NOT NULL REFERENCES fundraisers(id),
    user_id UUID NOT NULL,
    contributed_amount NUMERIC(19,2) NOT NULL DEFAULT 0,
    joined_at TIMESTAMPTZ NOT NULL,
    UNIQUE(fundraiser_id, user_id)
);

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    event_type VARCHAR(150) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL
);

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

CREATE INDEX idx_fundraiser_outbox_status_created ON outbox_events(status, created_at);
