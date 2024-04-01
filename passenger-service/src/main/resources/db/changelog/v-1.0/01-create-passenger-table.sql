CREATE TABLE passenger
(
    id             INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    first_name     VARCHAR(255),
    last_name      VARCHAR(255),
    contact_info   VARCHAR(255),
    start_latitude FLOAT,
    start_longitude FLOAT,
    destination_latitude FLOAT,
    destination_longitude FLOAT,
    available      BOOLEAN,
    rating         INTEGER,
    PRIMARY KEY (id)
);