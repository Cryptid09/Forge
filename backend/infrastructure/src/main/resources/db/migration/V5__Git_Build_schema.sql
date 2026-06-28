CREATE TABLE git_repositories (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    remote_url  VARCHAR(1024) NOT NULL,
    local_path  VARCHAR(1024) NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE build_artifacts (
    id            VARCHAR(36) PRIMARY KEY,
    repository_id VARCHAR(36) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    type          VARCHAR(50) NOT NULL,
    size_bytes    BIGINT NOT NULL,
    local_path    VARCHAR(1024) NOT NULL,
    build_time    TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_build_repo FOREIGN KEY (repository_id) REFERENCES git_repositories (id) ON DELETE CASCADE
);
