CREATE TABLE driver
(
    id           INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    contact_info VARCHAR(255),
    latitude     FLOAT,
    longitude    FLOAT,
    is_available BOOLEAN,
    PRIMARY KEY (id)
)
