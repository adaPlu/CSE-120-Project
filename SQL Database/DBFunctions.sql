/*insert, delete, sort, search*/
CREATE FUNCTION InsertIntoDB(
    ID AS INT,
    dateAndTime AS DATETIME,
    latitude AS DOUBLE(20, 15),
    longitude AS DOUBLE(20, 15),
    currentRow AS INT,
    currentSection AS INT,
    notes AS VARCHAR(255))
RETURNS NULL
INSERT INTO container_DB.containers(
    c_ID,
    c_dateTime,
    c_latitude,
    c_longitude,
    c_row,
    c_section,
    c_notes)
VALUES(
    ID,
    dateAndTime,
    latitude,
    longitude,
    currentRow,
    currentSection,
    notes
)
END