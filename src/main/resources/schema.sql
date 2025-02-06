CREATE TABLE plans (
                       id BIGINT PRIMARY KEY NOT NULL,
                       plan_name VARCHAR(255) NOT NULL UNIQUE,
                       description TEXT,
                       max_links NUMERIC,
                       price DECIMAL(10, 2) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
                       id BIGINT PRIMARY KEY NOT NULL,
                       role_name VARCHAR(255) NOT NULL UNIQUE,
                       description TEXT,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       image VARCHAR(255),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       is_verified BOOLEAN DEFAULT TRUE,
                       is_active BOOLEAN DEFAULT TRUE,
                       plan_id BIGINT,
                       role_id BIGINT,
                       CONSTRAINT fk_users_plan FOREIGN KEY (plan_id) REFERENCES plans(id) ON DELETE SET NULL,
                       CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
);

CREATE TABLE short_urls (
                            id BIGINT PRIMARY KEY NOT NULL,
                            original_url TEXT NOT NULL,
                            short_url VARCHAR(255) NOT NULL,
                            custom_alias VARCHAR(255) UNIQUE,
                            user_id BIGINT NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            expires_at TIMESTAMP,
                            qr_code TEXT,
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE analytics_urls (
                                id BIGINT PRIMARY KEY NOT NULL,
                                clicks BIGINT NOT NULL DEFAULT 0,
                                last_accessed_at TIMESTAMP NULL,
                                short_url_id BIGINT NOT NULL UNIQUE,
                                CONSTRAINT fk_short_url FOREIGN KEY (short_url_id) REFERENCES short_urls(id) ON DELETE CASCADE
);
