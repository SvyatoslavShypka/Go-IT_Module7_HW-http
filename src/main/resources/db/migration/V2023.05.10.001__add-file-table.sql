-- Create the file table
CREATE TABLE app_file (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255),
    extension VARCHAR(50),
    creation_date BIGINT
);