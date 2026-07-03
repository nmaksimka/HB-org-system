CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE gift_wishes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    link TEXT,
    image_url TEXT,
    estimated_price NUMERIC(12, 2),
    currency VARCHAR(3) NOT NULL DEFAULT 'RUB',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT chk_gift_price CHECK (estimated_price IS NULL OR estimated_price >= 0),
    CONSTRAINT chk_gift_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT chk_gift_visibility CHECK (visibility IN ('PUBLIC', 'GROUP_ONLY', 'PRIVATE')),
    CONSTRAINT chk_gift_status CHECK (status IN ('ACTIVE', 'RESERVED', 'PURCHASED', 'ARCHIVED'))
);

CREATE INDEX idx_gift_wishes_user ON gift_wishes (user_id);
CREATE INDEX idx_gift_wishes_status ON gift_wishes (status);

CREATE TABLE gift_reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    gift_wish_id UUID NOT NULL REFERENCES gift_wishes(id) ON DELETE CASCADE,
    reserved_by_user_id UUID NOT NULL,
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_gift_reservation UNIQUE (gift_wish_id)
);

CREATE INDEX idx_gift_reservations_user ON gift_reservations (reserved_by_user_id);

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    topic VARCHAR(150) NOT NULL,
    event_type VARCHAR(150) NOT NULL,
    aggregate_id UUID NOT NULL,
    correlation_id VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    attempts INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    sent_at TIMESTAMPTZ,
    CONSTRAINT chk_outbox_status CHECK (status IN ('NEW', 'SENT'))
);

CREATE INDEX idx_outbox_pending ON outbox_events (status, created_at);
