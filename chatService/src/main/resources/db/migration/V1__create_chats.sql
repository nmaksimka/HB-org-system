CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE chat_rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(30) NOT NULL,
    title VARCHAR(255) NOT NULL,
    target_user_id UUID,
    target_gift_id UUID,
    group_id UUID,
    created_by_user_id UUID NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT chk_chat_type CHECK (type IN ('BIRTHDAY_DISCUSSION','GIFT_DISCUSSION','GROUP_CHAT'))
);

CREATE TABLE chat_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat_room_id UUID NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
    user_id UUID NOT NULL,
    joined_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    left_at TIMESTAMPTZ,
    CONSTRAINT uq_chat_participant UNIQUE (chat_room_id, user_id)
);

CREATE TABLE chat_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat_room_id UUID NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'TEXT',
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT chk_message_type CHECK (type IN ('TEXT','SYSTEM'))
);

CREATE INDEX idx_chat_participants_user ON chat_participants (user_id, left_at);
CREATE INDEX idx_chat_messages_room_created ON chat_messages (chat_room_id, created_at);
