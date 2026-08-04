CREATE TABLE addresses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    address VARCHAR(255),
    mail_index VARCHAR(20) DEFAULT NULL,
    country VARCHAR(100),
    city VARCHAR(100)
);
