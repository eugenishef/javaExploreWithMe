CREATE TABLE statistics (
    id BIGSERIAL PRIMARY KEY,
    app VARCHAR(255),
    endpoint VARCHAR(255) NOT NULL,
    ip VARCHAR(50),
    request_time TIMESTAMP NOT NULL
);

CREATE INDEX idx_statistics_endpoint ON statistics(endpoint);
CREATE INDEX idx_statistics_request_time ON statistics(request_time);
