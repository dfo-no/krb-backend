-----
-----
----- STORED PROCEDURES
-----
-----

drop function if exists deleted_record_insert cascade;

CREATE FUNCTION deleted_record_insert()
    RETURNS trigger
AS
'
    BEGIN
        EXECUTE ''INSERT INTO DeletedRecord (data, deletedAt, objectId, tableName) VALUES ($1, $2, $3, $4)''
            USING to_jsonb(OLD.*), current_timestamp, OLD.id, TG_TABLE_NAME;
        RETURN OLD;
    End;
' LANGUAGE plpgsql;

-- Soft deletables
CREATE TRIGGER deleted_record_insert
    AFTER DELETE
    ON Product
    FOR EACH ROW
EXECUTE FUNCTION deleted_record_insert();

CREATE TRIGGER deleted_record_insert
    AFTER DELETE
    ON Publication
    FOR EACH ROW
EXECUTE FUNCTION deleted_record_insert();

CREATE TRIGGER deleted_record_insert
    AFTER DELETE
    ON Project
    FOR EACH ROW
EXECUTE FUNCTION deleted_record_insert();