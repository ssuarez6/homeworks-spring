CREATE TABLE recipe (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    difficulty VARCHAR(255) NOT NULL
);