CREATE TABLE driver (
                        id INT NOT NULL AUTO_INCREMENT,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        contact_info VARCHAR(255),
                        latitude DOUBLE,
                        longitude DOUBLE,
                        isAvailable BOOLEAN,
                        PRIMARY KEY (id)
) ENGINE=InnoDB;
/*диалект postgresql не работает, сработал mysql*/