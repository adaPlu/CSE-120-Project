CREATE TABLE containers (
    c_ID INT,
    c_batchID INT,
    c_dateTime DATETIME,
    c_row INT,
    c_section INT,
    c_notes VARCHAR(255)
)

CREATE TABLE batch (
    b_batchID INT,
    b_latitude decimal(20, 15),
    b_longitude decimal(20, 15) 
);