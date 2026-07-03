CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE birthday_subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subscriber_id UUID NOT NULL,
    subscription_type VARCHAR(20) NOT NULL,
    target_user_id UUID,
    target_group_id UUID,
    days_before INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_subscription_type CHECK (subscription_type IN ('USER', 'GROUP')),
    CONSTRAINT chk_days_before CHECK (days_before BETWEEN 0 AND 365),
    CONSTRAINT chk_subscription_target CHECK (
        (subscription_type = 'USER' AND target_user_id IS NOT NULL AND target_group_id IS NULL)
        OR
        (subscription_type = 'GROUP' AND target_group_id IS NOT NULL AND target_user_id IS NULL)
    ),
    CONSTRAINT chk_subscription_not_self CHECK (
        target_user_id IS NULL OR subscriber_id <> target_user_id
    )
);

CREATE UNIQUE INDEX uq_active_user_subscription
    ON birthday_subscriptions (subscriber_id, target_user_id)
    WHERE subscription_type = 'USER' AND is_active;
CREATE UNIQUE INDEX uq_active_group_subscription
    ON birthday_subscriptions (subscriber_id, target_group_id)
    WHERE subscription_type = 'GROUP' AND is_active;
CREATE INDEX idx_subscriptions_subscriber
    ON birthday_subscriptions (subscriber_id, is_active);

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
    CONSTRAINT chk_subscription_outbox_status CHECK (status IN ('NEW', 'SENT'))
);

CREATE INDEX idx_subscription_outbox_pending ON outbox_events (status, created_at);
