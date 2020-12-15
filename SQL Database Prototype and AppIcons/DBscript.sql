--Insert some sample data?
--containers
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (8, 1, '1996-01-15', 8, 1, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (1, 1, '1996-05-01', 4, 10, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (2, 1, '1996-03-15', 3, 2, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (3, 1, '1996-02-01', 8, 4, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (4, 2, '1996-01-15', 7, 10, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (5, 2, '1996-05-01', 4, 10, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (6, 3, '1996-03-15', 8, 2, "Some notes");
INSERT INTO containers (c_ID,c_batchID,c_dateTime,c_row,c_section,c_notes)
VALUES  (7, 4, '1996-02-01', 8, 4, "Some notes");
--batches
INSERT INTO batch (b_batchID,b_latitude,b_longitude)
VALUES  (1, 6.021, 5.312);
INSERT INTO batch (b_batchID,b_latitude,b_longitude)
VALUES  (2, 8.021, 3.312);
INSERT INTO batch (b_batchID,b_latitude,b_longitude)
VALUES  (3, 4.021, 1.312);
INSERT INTO batch (b_batchID,b_latitude,b_longitude)
VALUES  (4, 3.021, 2.312);

--Queries
/*insert, delete, sort, search*/
 SELECT c_ID
 FROM containers, batch
 WHERE b_batchID = c_batchID;
 

 SELECT b_latitude,b_longitude
 FROM containers, batch
 WHERE b_batchID = c_batchID
 GROUP BY b_batchID;

 
 SELECT b_latitude,b_longitude
 FROM containers, batch
 WHERE c_dateTime > '1996-01-01';

 SELECT c_ID, b_batchID
 FROM containers, batch
 WHERE c_dateTime > '1996-03-01'
 AND b_batchID = c_batchID;
