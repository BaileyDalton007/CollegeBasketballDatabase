ALTER PROCEDURE transferPlayer
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


-- Give dbuser ability to execute this stored procedures
GRANT EXECUTE ON OBJECT::transferPlayer TO dbuser;