-- insert data into position table 
INSERT INTO position (posID, posName) VALUES
('pg', 'pointGuard'),
('sg', 'shootingGuard'),
('sf', 'smallForward'),
('pf', 'powerForward'),
('ct', 'center');

-- insert data into sponsorship table
INSERT INTO sponsorship (sName, contribution) VALUES
('Nike', 4300000), --sID will be 8000
('Adidas', 2600000), --sID will be 8001
('Gatorade', 4500000), --sID will be 8002
('Redbull', 3000000); --sID will be 8003

-- insert data into arena table
INSERT INTO arena (aName, numGames, hWins, vWins, seats, avgTixPrice) VALUES
('Redrock Arena', 5000, 35, 20, 15000, 4200), --aID will be 5000
('Harbor Arena', 3200, 20, 25, 10000, 3700), --aID will be 5001
('Ridgeline Arena', 2900, 15, 30, 8500, 2900); --aID will be 5002

-- insert data into coach table
INSERT INTO coach (fname, lname, age, jobNum, yearHired, careerWins) VALUES
('Mark', 'Williams', 50, 3, 2018, 110),    -- coaID will be 3000
('Lisa', 'Smith', 42, 2, 2020, 80),    -- coaID will be 3001
('Bob', 'Jones', 63, 1, 2013, 200),	 -- coaID will be 3002
('Dan', 'Lee', 57, 1, 2022, 50);	--coaID will be 3003
('Lebron', 'James', 41, 0, 2025, 600) --coaID will be 3004

-- insert data into college table
INSERT INTO college (aID, colName, division, numStudents) VALUES
(5000, 'Northpoint University', 1, 14500), --colID will be 9000
(5001, 'Southport College', 2, 7800),	--colID will be 9001
(5001, 'Westvale College', 2, 8200),	--colID will be 9002
(NULL, 'Eastbrook University', 3, 3300);	--colID will be 9003

-- insert data into team table
-- "record" is the number of wins
INSERT INTO team (tName, colID, record, coaID) VALUES
('Northpoint Knights', 9000, 12, 3000), --tID will be 1000
('Southport Sharks', 9001, 8, 3001),	--tID will be 1001
('Westvale Warriors', 9002, 10, 3002),	--tID will be 1002
('Eastbrook Eagles', 9003, 9, 3003);	--tID will be 1003

-- insert data into player table
INSERT INTO player (tID, posID, fname, lname, age, year, highPPG) VALUES
(1000, 'pg', 'Max', 'Johnson', 19, 1, 18),	--pID will be 2000
(1000, NULL, 'Riley', 'Brown', 20, 2, 12),	--pID will be 2001
(1001, 'sg', 'Henry', 'Miller', 21, 3, 20),	--pID will be 2002
(1001, 'sf', 'David', 'Wilson', 22, 3, 22),	--pID will be 2003
(1001, 'pf', 'Jack', 'Davis', 23, 4, 25),	--pID will be 2004
(1003, 'ct', 'Tre', 'Hill', 20, 1, 15),	--pID will be 2005
(1003, 'pg', 'Jay', 'Adams', 19, 1, 16);   --pID will be 2006

-- insert data into games table
INSERT INTO games (date, homeID, visitorID, hScore, vScore, aID) VALUES
('2025-01-15', 1000, 1001, 75, 68, 5000), --gID will be 4000
('2025-02-10', 1001, 1003, 80, 82, 5001), --gID will be 4001
('2025-03-05', 1002, 1000, 70, 72, 5002); --gID will be 4002

-- insert data into fanbase table
INSERT INTO fanbase (tID, mascot, followers, numSoldOut) VALUES
(1000, 'Kenny Knight', 12000, 3),	--fbID will be 6000
(1003, 'Eddie Eagle', 8500, 2);		--fbID will be 6001


-- insert data into tickets table
INSERT INTO tickets (gID, purDate, section, seatNum, rowNum, price) VALUES
(4000, '2024-12-20', 1, 12, 3, 3500),	--tixID will be 7000
(4000, '2024-12-21', 1, 15, 3, 3600),	--tixID will be 7001
(4001, '2025-01-05', 2, 8, 4, 3700),	--tixID will be 7002
(4001, '2025-01-06', 2, 9, 4, 3800),	--tixID will be 7003
(4002, '2025-02-20', 3, 5, 2, 3300);	--tixID will be 7004

-- insert data into teamSponsors tale
INSERT INTO teamSponsors (tID, sID) VALUES
(1000, 8000),
(1000, 8001),
(1001, 8001),
(1002, 8002),
(1003, 8003),
(1003, 8000); 

