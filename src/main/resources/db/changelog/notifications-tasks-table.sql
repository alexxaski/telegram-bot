--liquibase formatted sql

-- changeset 1:create_notification_tasks_table
CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    notification_text TEXT NOT NULL,
    scheduled_time TIMESTAMP NOT NULL
);