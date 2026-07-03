CREATE TABLE admin_imports (
    id UUID PRIMARY KEY,
    admin_id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_rows INTEGER NOT NULL,
    successful_rows INTEGER NOT NULL,
    failed_rows INTEGER NOT NULL,
    error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    finished_at TIMESTAMPTZ
);
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    event_id UUID UNIQUE,
    event_type VARCHAR(150) NOT NULL,
    source_service VARCHAR(100) NOT NULL,
    actor_id UUID,
    aggregate_id UUID,
    correlation_id VARCHAR(100),
    payload TEXT NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_audit_logs_occurred ON audit_logs(occurred_at DESC);
