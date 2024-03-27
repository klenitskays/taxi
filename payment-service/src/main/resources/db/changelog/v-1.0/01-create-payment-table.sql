CREATE TABLE payment
(
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    description VARCHAR(255),
    amount INTEGER,
    currency VARCHAR(3),
    status VARCHAR(255)
);

