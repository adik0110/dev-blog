CREATE TABLE users
(
    id               BIGSERIAL PRIMARY KEY,
    username         VARCHAR(50) UNIQUE  NOT NULL,
    password         VARCHAR(100)        NOT NULL,
    email            VARCHAR(100) UNIQUE NOT NULL,
    reputation_score INT DEFAULT 0
);

CREATE TABLE tags
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE posts
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    content   TEXT         NOT NULL,
    author_id BIGINT       NOT NULL REFERENCES users (id),
    rating    INT DEFAULT 0
);

CREATE TABLE post_tags
(
    post_id BIGINT NOT NULL REFERENCES posts (id),
    tag_id  BIGINT NOT NULL REFERENCES tags (id),
    PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE comments
(
    id        BIGSERIAL PRIMARY KEY,
    text      TEXT   NOT NULL,
    author_id BIGINT NOT NULL REFERENCES users (id),
    post_id   BIGINT NOT NULL REFERENCES posts (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rating_votes
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id),
    post_id BIGINT NOT NULL REFERENCES posts (id),
    value   INT    NOT NULL CHECK (value IN (-1, 1)),
    UNIQUE (user_id, post_id)
);

-- Добавляем индексы для ускорения запросов
CREATE INDEX idx_posts_author_id ON posts(author_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);
CREATE INDEX idx_rating_votes_post_id ON rating_votes(post_id);