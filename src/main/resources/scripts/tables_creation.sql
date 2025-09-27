-- ============================
-- USERS
-- ============================
CREATE TABLE users
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    mobile     VARCHAR(20) UNIQUE,
    avatar     VARCHAR(255),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_users_name ON users (name);

-- ============================
-- CHEFS
-- ============================
CREATE TABLE chefs
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    bio        VARCHAR(255),
    avatar     VARCHAR(255),
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_chefs_name ON chefs (name);

-- ============================
-- CATEGORIES
-- ============================
CREATE TABLE categories
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    image_path  VARCHAR(255),
    created_at  TIMESTAMP DEFAULT now()
);

CREATE UNIQUE INDEX idx_categories_title ON categories (title);

-- ============================
-- INGREDIENTS
-- ============================
CREATE TABLE ingredients
(
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(100) UNIQUE NOT NULL,
    image_path VARCHAR(255)
);

CREATE UNIQUE INDEX idx_ingredients_title ON ingredients (title);

-- ============================
-- MEALS
-- ============================
CREATE TABLE meals
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(150) NOT NULL,
    description  TEXT,
    image_path   VARCHAR(255),
    video_url    VARCHAR(255),
    rating_count INT           DEFAULT 0,
    rating_avg   NUMERIC(3, 2) DEFAULT 0,
    added_at     TIMESTAMP     DEFAULT now(),
    category_id  BIGINT       REFERENCES categories (id) ON DELETE SET NULL,
    chef_id      BIGINT       REFERENCES chefs (id) ON DELETE SET NULL
);

CREATE INDEX idx_meals_title ON meals (title);
CREATE INDEX idx_meals_category ON meals (category_id);
CREATE INDEX idx_meals_chef ON meals (chef_id);

-- ============================
-- MEAL_INGREDIENT (junction)
-- ============================
CREATE TABLE meal_ingredients
(
    meal_id       BIGINT REFERENCES meals (id) ON DELETE CASCADE,
    ingredient_id BIGINT REFERENCES ingredients (id) ON DELETE CASCADE,
    measure       VARCHAR(50),
    PRIMARY KEY (meal_id, ingredient_id)
);

CREATE INDEX idx_meal_ingredients_meal ON meal_ingredients (meal_id);
CREATE INDEX idx_meal_ingredients_ingredient ON meal_ingredients (ingredient_id);

-- ============================
-- COMMENTS
-- ============================
CREATE TABLE comments
(
    id         BIGSERIAL PRIMARY KEY,
    body       TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    meal_id    BIGINT REFERENCES meals (id) ON DELETE CASCADE,
    user_id    UUID REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_comments_meal ON comments (meal_id);
CREATE INDEX idx_comments_user ON comments (user_id);

-- ============================
-- USER_FAVORITES (junction)
-- ============================
CREATE TABLE user_favorites
(
    user_id    UUID REFERENCES users (id) ON DELETE CASCADE,
    meal_id    BIGINT REFERENCES meals (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (user_id, meal_id)
);

CREATE INDEX idx_user_favorites_user ON user_favorites (user_id);
CREATE INDEX idx_user_favorites_meal ON user_favorites (meal_id);
