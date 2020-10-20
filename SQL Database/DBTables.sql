CREATE TABLE containers (
    c_ID INT,
    c_batchID INT,
    c_dateTime DATETIME,
    c_row INT,
    c_section INT,
    c_notes VARCHAR(255)
)

CREATE TABLE batch (
    b_ID INT,
    c_latitude DOUBLE(20, 15),
    c_longitude DOUBLE(20, 15)
);