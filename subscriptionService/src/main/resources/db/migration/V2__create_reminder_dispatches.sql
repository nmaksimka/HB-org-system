CREATE TABLE reminder_dispatches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subscription_id UUID NOT NULL REFERENCES birthday_subscriptions(id) ON DELETE CASCADE,
    birthday_year INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_reminder_dispatch UNIQUE (subscription_id, birthday_year)
);
