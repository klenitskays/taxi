CREATE TABLE rides (
                       id             INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                       passenger_id           INTEGER,
                       driver_id              INTEGER,
                       start_latitude        FLOAT,
                       start_longitude       FLOAT,
                       destination_latitude  FLOAT,
                       destination_longitude FLOAT,
                       status                 VARCHAR(255),
                       start_time             TIMESTAMP,
                       end_time                 TIMESTAMP
);