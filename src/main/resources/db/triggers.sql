DROP TRIGGER IF EXISTS deleted_record_insert ON Product;
CREATE TRIGGER deleted_record_insert
    AFTER DELETE
    ON Product
    FOR EACH ROW
EXECUTE FUNCTION deleted_record_insert();

DROP TRIGGER IF EXISTS deleted_record_insert ON Publication;
CREATE TRIGGER deleted_record_insert
    AFTER DELETE
    ON Publication
    FOR EACH ROW
EXECUTE FUNCTION deleted_record_insert();

DROP TRIGGER IF EXISTS deleted_record_insert ON Project;
CREATE TRIGGER deleted_record_insert
    AFTER DELETE
    ON Project
    FOR EACH ROW
EXECUTE FUNCTION deleted_record_insert();