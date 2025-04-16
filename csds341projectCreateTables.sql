/*creation of tables*/

create table position( 
posID	char(2), 
posName varchar(13), 
primary key(posID), 
check(posID in('pg', 'sg', 'sf', 'pf', 'ct')), 
check(posName in('pointGuard', 'shootingGuard', 'smallForward', 'powerForward', 'center')),
); 

create table sponsorship( 
sID		int identity(8000, 1), 
sName	varchar(50) not null, 
contribution	int /*again money stored in cents as an integer*/
primary key(sID)
); 

create table arena( 
aID		int identity(5000, 1), 
aName	varchar(50) not null, 
numGames	int, 
hWins	int, 
vWins	int, 
seats	int, 
avgTixPrice int, 
primary key(aID), 
);

create table coach( 
coaID	int identity(3000, 1), 
fname	varchar(20) not null, 
lname	varchar(20) not null, 
age		int, 
jobNum	int, 
yearHired	int, 
careerWins	int
primary key(coaID)
); 

create table college(
colID	int identity(9000, 1), 
aID		int, 
colName varChar(50), /*assuming all college names under 50 characters- Case Western Reserve University is the longest college name I've ever heard and its 30 characters*/  
division	int not null, 
numStudents	int, 
primary key(colID), 
foreign key(aID) references arena, 
check(division in(1, 2, 3))
); 

create table team(
tID		int identity(1000, 1), 
tName	varchar(100) not null, /*assuming the team name is less than 100 characters*/
colID	int not null, 
record	int, /*figure out how to format this so its 'wins-losses'-- i think ill have to create two seperate wins and losses and combine them*/
coaID	int not null, 
primary key(tID), 
foreign key(colID) references college, 
foreign key(coaID) references coach, 
);

create table player( 
pID		int identity(2000, 1), 
tID		int not null, 
posID	char(2), 
fname	varchar(20) not null, 
lname	varchar(20) not null, 
age		int, 
year	int not null, 
highPPG	int, 
primary key(pID), 
foreign key(tID) references team, 
foreign key(posID) references position, 
check(year in(0, 1, 2, 3, 4, 5, 6, 7, 8 , 9, 10)), /*the most years of eligibility ever used was 8, assuming no player will use more than 10 years of eligibility*/
);

create table games(
gID		int identity(4000, 1), 
date	date not null, 
homeID	int not null, 
visitorID int not null, 
hScore	int not null, 
vScore	int not null, 
aID		int not null, 
primary key(gID), 
foreign key(homeID) references team(tID), 
foreign key(visitorID) references team(tID), 
foreign key(aID) references arena, 
);

create table fanbase( 
fbID	int identity(6000, 1), 
tID		int not null, 
mascot	varchar(20), 
followers	int, 
numSoldOut	int, 
primary key(fbID), 
foreign key(tID) references team, 
); 

create table tickets( 
tixID	int identity(7000, 1), 
gID		int, 
purDate	date not null, 
section	int not null, 
seatNum	int not null,
rowNum	int not null, 
price	int not null, /*money store in cents as an integer*/
primary key(tixID), 
foreign key(gID) references games, 
); 


create table teamSponsors( 
tID		int, 
sID		int, 
primary key(tID, sID), 
foreign key(tID) references team, 
foreign key(sID) references sponsorship
); 

CREATE NONCLUSTERED INDEX tid_to_col
ON team (colID);


CREATE TRIGGER updateDivision
ON college
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE c
    SET division = 
	-- compute the division a college should be in
        CASE 
            WHEN i.numStudents < 4000 THEN 3
            WHEN i.numStudents < 9000 THEN 2
            ELSE 1
        END
    FROM college c
    INNER JOIN inserted i ON c.colID = i.colID
    WHERE c.division != 
	-- don't update divisions if it is already correct
        CASE 
            WHEN i.numStudents < 4000 THEN 3
            WHEN i.numStudents < 9000 THEN 2
            ELSE 1
        END
END

