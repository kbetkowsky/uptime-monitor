CREATE TABLE check_results(
    id UUID PRIMARY KEY,
    site_id UUID NOT NULL,
    checked_at TIMESTAMPTZ NOT NULL,
    http_status INT,
    response_time_ms BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_check_results_site FOREIGN KEY (site_id) REFERENCES monitored_sites(id)
);

CREATE INDEX idx_check_results_site_checked ON check_results(site_id, checked_at DESC);