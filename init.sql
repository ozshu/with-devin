CREATE DATABASE IF NOT EXISTS testdb;
USE testdb;

CREATE TABLE IF NOT EXISTS messages (
    id INT PRIMARY KEY,
    text VARCHAR(255) NOT NULL
);

INSERT INTO messages (id, text) VALUES (1, 'Hello');
