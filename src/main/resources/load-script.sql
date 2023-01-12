-----
-----
----- INSERT ENTITIES
-----
-----

--PROJECT
insert into Project("id", title, description, deleteddate, ref)
values (1, 'Prosjekt1', 'Beskrivelse1', null, 'ccc4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, deleteddate, ref)
values (2, 'Prosjekt2', 'Beskrivelse2', null, 'aaa4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, deleteddate, ref)
values (3, 'Prosjekt3', 'Beskrivelse3', null, 'bbb4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, deleteddate, ref)
values (22, 'ScriptProsjekt4', 'Beskrivelse3', '2021-09-29 21:17:30.23195', 'prosjekt4-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, deleteddate, ref)
values (24, 'ScriptProsjekt5', 'Beskrivelse3', null, 'prosjekt5-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, deleteddate, ref)
values (26, 'ScriptProsjekt6', 'Beskrivelse3', null, 'prosjekt6-edb2-431f-855a-4368e2bcddd1');

--CODELIST
insert into Codelist("id", title, description, ref, project_id_fk)
values (4, 'CodelistTittel1', 'CodelistBeskrivelse1', 'qqq4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Codelist("id", title, description, ref, project_id_fk)
values (5, 'CodelistTittel2', 'CodelistBeskrivelse2', 'asd4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Codelist("id", title, description, ref, project_id_fk)
values (21, 'CodelistTittel3', 'CodelistBeskrivelse2', 'newlist14db69-edb2-431f-855a-4368e2bcddd1', 22);
insert into Codelist("id", title, description, ref, project_id_fk)
values (25, 'CodelistTittel4', 'CodelistBeskrivelse2', 'newlist2222db69-edb2-431f-855a-4368e2bcddd1', 24);
insert into Codelist("id", title, description, ref, project_id_fk)
values (27, 'CodelistTittel4', 'CodelistBeskrivelse2', 'newlist33333db69-edb2-431f-855a-4368e2bcddd1', 26);

-- PUBLICATION
insert into Publication("id", comment, version, date, ref, deleteddate, project_id_fk)
values (8, 'comment1', 2, '2020-09-29 21:17:30.23195', 'zzz4db69-edb2-431f-855a-4368e2bcddd1', null, 3);
insert into Publication("id", comment, version, date, ref, deleteddate, project_id_fk)
values (9, 'comment2', 4, '1999-09-10 21:17:30.23195', 'xxx4db69-edb2-431f-855a-4368e2bcddd1', null, 3);

-- NEED
insert into Need("id", title, description, ref, project_id_fk)
values (10, 'Need tittel fra script', 'Need beskrivelse fra script', 'need1b69-edb2-431f-855a-4368e2bcddd1', 2);
insert into Need("id", title, description, ref, project_id_fk)
values (11, 'Need tittel fra script #2', 'Need beskrivelse fra script #2', 'need2b69-edb2-431f-855a-4368e2bcddd1', 2);

-- REQUIREMENT
insert into Requirement("id", title, description, ref, project_id_fk, need_id_fk)
values (12, 'Requirement tittel fra script', 'Requirement beskrivelse fra script',
        'req1b69-edb2-431f-855a-4368e2bcddd1', 2, 10);
insert into Requirement("id", title, description, ref, project_id_fk, need_id_fk)
values (13, 'Requirement tittel fra script #2', 'Requirement beskrivelse fra script #2',
        'reqd2b69-edb2-431f-855a-4368e2bcddd1', 2, 10);

--REQUIREMENT VARIANT
insert into RequirementVariant("id", description, instruction, ref, requirementtext, useproduct, usequalification,
                               usespecification, requirement_id_fk)
values (14, 'Requirement variant beskrivelse fra script', 'instruksjon', 'rvrv1b69-edb2-431f-855a-4368e2bcddd1',
        'req text', true, true, true, 12);
insert into RequirementVariant("id", description, instruction, ref, requirementtext, useproduct, usequalification,
                               usespecification, requirement_id_fk)
values (15, 'Requirement variant beskrivelse fra script #2 ', 'instruksjon', 'rvrv2b69-edb2-431f-855a-4368e2bcddd1',
        'req text', true, true, true, 12);
insert into RequirementVariant("id", description, instruction, ref, requirementtext, useproduct, usequalification,
                               usespecification, requirement_id_fk)
values (16, 'Requirement variant beskrivelse fra script #2 ', 'instruksjon', 'rvrv3b69-edb2-431f-855a-4368e2bcddd1',
        'req text', true, true, true, 12);

-- PRODUCT
insert into product("id", title, description, ref, project_id_fk, requirementvariant_id_fk)
values (5, 'ProduktTittel1', 'ProduktBeskrivelse1', 'edb4db69-edb2-431f-855a-4368e2bcddd1', 3, 14);
insert into Product("id", title, description, ref, project_id_fk, requirementvariant_id_fk)
values (6, 'ProduktTittel2', 'ProduktBeskrivelse2', 'kuk4db69-edb2-431f-855a-4368e2bcddd1', 3, 14);
insert into Product("id", title, description, ref, project_id_fk, requirementvariant_id_fk)
values (7, 'ProduktTittel3', 'ProduktBeskrivelse3', 'kua4db69-edb2-431f-855a-4368e2bcddd1', 1, 14);

-- CODE
insert into Code("id", title, description, ref, codelist_id_fk)
values (17, 'code tittel fra script1', 'code beskrivelse fra script', 'script1b69-edb2-431f-855a-4368e2bcddd1', 4);
insert into Code("id", title, description, ref, codelist_id_fk)
values (18, 'code tittel fra script2', 'code beskrivelse fra script', 'scrip21b69-edb2-431f-855a-4368e2bcddd1', 4);
insert into Code("id", title, description, ref, codelist_id_fk)
values (19, 'code tittel fra script3', 'code beskrivelse fra script', 'script3b69-edb2-431f-855a-4368e2bcddd1', 5);
insert into Code("id", title, description, ref, codelist_id_fk)
values (20, 'code tittel fra script4', 'code beskrivelse fra script', 'script4b69-edb2-431f-855a-4368e2bcddd1', 21);
insert into Code("id", title, description, ref, codelist_id_fk)
values (23, 'code tittel fra script5', 'code beskrivelse fra script', 'script5b69-edb2-431f-855a-4368e2bcddd1', 21);
insert into Code("id", title, description, ref, codelist_id_fk)
values (28, 'code tittel fra script6', 'code beskrivelse fra script', 'script6b69-edb2-431f-855a-4368e2bcddd1', 27);

-- DELETE RECORD
insert into DeleteRecord (id, data, deletedAt, updatedAt, tableName, objectId)
values (109, '{
  "id": 999,
  "ref": "ref-til-product-999",
  "title": "product-9999 tittel",
  "description": "propduct-9999-beskrivelse",
  "project_id_fk": 3,
  "requirementvariant_id_fk": 14
}', '2021-09-29 21:17:30.23195', null, 'product', 999);

insert into DeleteRecord (id, data, deletedAt, updatedAt, tableName, objectId)
values (110, '{
  "id": 998,
  "ref": "ref-til-product-998",
  "title": "product-998 tittel",
  "description": "propduct-9999-beskrivelse",
  "project_id_fk": 3,
  "requirementvariant_id_fk": 14
}', '2021-09-29 21:17:30.23195', null, 'product', 998);


--CONFIG
select setval('hibernate_sequence', 100, true);


-----
-----
----- STORED PROCEDURES
-----
-----


CREATE FUNCTION delete_record_insert()
    RETURNS trigger
AS
'
    BEGIN
        EXECUTE ''INSERT INTO deleteRecord (data, objectId, tableName, deletedAt) VALUES ($1, $2, $3, $4)''
            USING to_jsonb(OLD.*), OLD.id, TG_TABLE_NAME, current_timestamp;
        RETURN OLD;
    End;
' LANGUAGE plpgsql;

-- Soft deletables
CREATE TRIGGER delete_record_insert
    AFTER DELETE
    ON Product
    FOR EACH ROW
EXECUTE FUNCTION delete_record_insert();

CREATE TRIGGER delete_record_insert
    AFTER DELETE
    ON Publication
    FOR EACH ROW
EXECUTE FUNCTION delete_record_insert();

CREATE TRIGGER delete_record_insert
    AFTER DELETE
    ON Project
    FOR EACH ROW
EXECUTE FUNCTION delete_record_insert();