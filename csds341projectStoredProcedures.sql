CREATE PROCEDURE transferPlayer
    @pID INT,
    @new_tID INT,
	@player_name varchar(40) OUTPUT,
	@new_college_name varchar(50) OUTPUT
AS
BEGIN
    DECLARE @old_colID INT;
	DECLARE @new_colID INT;

    -- Get the old college ID for the player
    SELECT @old_colID = colID
    FROM team
    WHERE tID = (SELECT tID FROM player WHERE pID = @pID);

	-- Get the new college ID for the player
    SELECT @new_colID = colID
    FROM team
    WHERE tID = @new_tID;

    -- Update player's team
    UPDATE player
    SET tID = @new_tID
    WHERE pID = @pID;

    -- Decrement numStudents on the old team
    UPDATE college
    SET numStudents = numStudents - 1
    WHERE colID = @old_colID;

    -- Increment numStudents on the new team
    UPDATE college
    SET numStudents = numStudents + 1
    WHERE colID = @new_colID;

    -- Return player's name and new school to output
	SELECT @player_name = fname + ' ' + lname  FROM player WHERE pID = @pID;
	SELECT @new_college_name = colName FROM college WHERE colID = @new_colID;
END;

CREATE PROCEDURE replaceCoach
    @tID INT,
    @new_coaID INT,
	@old_coach_name varchar(40) OUTPUT,
	@new_coach_name varchar(40) OUTPUT,
	@team_college_name varchar(50) OUTPUT
AS
BEGIN
    DECLARE @old_coaID INT;

    -- Get the coach we are firing
    SELECT @old_coaID = coaID
    FROM team
    WHERE tID = @tID;

    -- Update the team's coach
    UPDATE team
    SET coaID = @new_coaID
    WHERE tID = @tID;

    -- Increment jobNum for newly hired coach
    UPDATE coach
    SET jobNum = COALESCE(jobNum, 0) + 1
    WHERE coaID = @new_coaID;

    -- Return player's name and new school to output
	SELECT @old_coach_name = fname + ' ' + lname  FROM coach WHERE coaID = @old_coaID;
	SELECT @new_coach_name = fname + ' ' + lname  FROM coach WHERE coaID = @new_coaID;
	SELECT @team_college_name = colName FROM college WHERE colID = (
		SELECT colID FROM team WHERE tID = @tID
	);
END;

CREATE PROCEDURE removePlayer
	@pID int,
	@player_name varchar (40) output,
	@college_name varchar (50) output,
	@updated_numStudents int
AS
BEGIN
	DECLARE @colID int;

	--get player's name and current school
	SELECT
		@player_name = p.fname + ' ' + p.lname,
		@colID = t.colID
	FROM player as p
	JOIN team as t
	ON p.tID = t.tID
	WHERE p.pID = @pID;

	--get college name
	SELECT @college_name = colName
	FROM college
	WHERE colID = @colID;

	--decrement that college's numStudents
	UPDATE college
	SET numStudents = numStudents -1
	WHERE colID = @colID;

	--show updated number
	SELECT @updated_numStudents = numStudents
	FROM college
	WHERE colID = @colID;

	--delete player
	DELETE FROM player
	WHERE pID = @pID;

	-- select statement to summarize changes
	SELECT 
		@player_name as RemovedPlayer,
		@college_name as College,
		@updated_numStudents as RemainingStudents;
END;

CREATE PROCEDURE expandSponsorship
	@sID int,
	@additionalContribution int,
	@new_tID int,
	@sponsor_name varchar(50) output,
	@new_contribution int output
AS
BEGIN
	--increase sponsor contribution
	UPDATE sponsorship
	SET contribution = contribution +@additionalContribution
	WHERE sID = @sID;

	--sponsor name and new total contribution
	SELECT 
		@sponsor_name = sName,
		@new_contribution = contribution
	FROM sponsorship
	WHERE sID = @sID;

	--Link sponsor to new team
	INSERT INTO teamSponsors (tID, sID)
	VALUES (@new_tID, @sID);

	--summary of what happened
	SELECT 
		@sponsor_name as Sponsor,
		@new_contribution as TotalContribution;

	--list all teams the sponsor now supports
	SELECT 
		ts.tID,
		t.tName
	FROM teamSponsors as ts
	JOIN team as t
		ON ts.tID = t.tID
	WHERE ts.sID = @sID;
END;

CREATE or ALTER PROCEDURE newSponsor 
	-- Add the input parameters for the stored procedure here
	@sID int output,
	@sName varchar(50), 
	@contribution int, 
	@tName varchar(100)
AS
BEGIN
	--insert new sponsor into sponsorhip table 
	insert into sponsorship(sName, contribution)
	values (@sName, @contribution)
	set @sID = SCOPE_IDENTITY(); 

	--inserts relationship into teamSponsors table
	declare @tID as int; 
	select @tID = tID from team where tName = @tName
	insert into teamSponsors(tID, sID)
	values(@tID, @sID); 

  --select sID, sName, contribution from sponsorship;
  --select sID, tID from teamSponsors;
	select sponsorship.sID, team.tID, team.tName, sponsorship.contribution, sponsorship.sName
	from sponsorship inner join teamSponsors on sponsorship.sID = teamSponsors.sID inner join team on teamSponsors.tID = team.tID
	where sponsorship.sID = @sID;
END; 

CREATE or ALTER PROCEDURE followersSoldOut 
	@tID int,  --need to identitfy the team for which the update it occuring
	@new_followers int, --to update the followers
 
	--info needed to insert the first ticket
	@purDate date, 
	@section1 int, 
	@seatNum1 int, 
	@rowNum1 int, 
	@price1 int, 
	--info needed to insert the second ticket 
	@section2 int, 
	@seatNum2 int, 
	@rowNum2 int, 
	@price2 int

AS 
BEGIN
  -- update the teams' followers 
	update fanbase 
	set followers = @new_followers; 

  -- update the number of sold out games 
	update fanbase 
	set numSoldOut = numSoldOut + 1
	where tID = @tID;

  -- insert first ticket for the sold out game 
	declare @tixID1 as int; 
	insert into tickets(purDate, section, seatNum, rowNum, price)
	values(@purDate, @section1, @seatNum1, @rowNum1, @price1)
	set @tixID1 = SCOPE_IDENTITY(); 
	
  --insert second ticket for the sold out game 
	declare @tixID2 as int; 
	insert into tickets( purDate, section, seatNum, rowNum, price)
	values(@purDate, @section2, @seatNum2, @rowNum2, @price2)
	set @tixID2 = SCOPE_IDENTITY(); 

	--select statement to show information properly populated 
	select fb.followers, fb.numSoldOut, fb.tID
	from fanBase as fb 
	where fb.tID = @tID
	select ti.tixID, ti.purDate, ti.section, ti.seatNum, ti.rowNum, ti.price 
	from tickets as ti
	where purDate = @purDate
END; 


-- Give dbuser ability to execute this stored procedures
GRANT EXECUTE ON OBJECT::transferPlayer TO dbuser;
GRANT EXECUTE ON OBJECT::replaceCoach TO dbuser;
GRANT EXECUTE ON OBJECT::removePlayer TO dbuser;
GRANT EXECUTE ON OBJECT::expandSponsorship TO dbuser;
GRANT EXECUTE ON OBJECT::newSponsor TO dbuser;
GRANT EXECUTE ON OBJECT::followersSoldOut TO dbuser; 

