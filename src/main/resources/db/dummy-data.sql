-----
-----
----- INSERT ENTITIES
-----
-----

--PROJECT
insert into Project("id", title, description, ref)
values (1, 'Prosjekt1', 'Beskrivelse1', 'ccc4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, ref)
values (2, 'Prosjekt2', 'Beskrivelse2', 'aaa4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, ref)
values (3, 'Prosjekt3', 'Beskrivelse3', 'bbb4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, ref)
values (22, 'ScriptProsjekt4', 'Beskrivelse3', 'prosjekt4-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, ref)
values (24, 'ScriptProsjekt5', 'Beskrivelse3', 'prosjekt5-edb2-431f-855a-4368e2bcddd1');
insert into Project("id", title, description, ref)
values (26, 'ScriptProsjekt6', 'Beskrivelse3', 'prosjekt6-edb2-431f-855a-4368e2bcddd1');

--CODELIST
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (4, 'CodelistTittel1', '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse1', 'qqq4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (5, 'CodelistTittel2', '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'asd4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (21, 'CodelistTittel3', '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'newlist14db69-edb2-431f-855a-4368e2bcddd1',
        22);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (25, 'CodelistTittel4', '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'newlist2222db69-edb2-431f-855a-4368e2bcddd1', 24);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (46, 'CodelistTittel4',
        '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'ref234567890', 26);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (45, 'CodelistTittel4',
        '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'gfdgfd-edb2-431f-855a-4368e2bcddd1', 26);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (47, 'CodelistTittel4',
        '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'sadsada-edb2-431f-855a-4368e2bcddd1', 26);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (48, 'CodelistTittel4',
        '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'sadsad-edb2-431f-855a-4368e2bcddd1', 26);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (49, 'CodelistTittel4',
        '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'sadfsa-edb2-431f-855a-4368e2bcddd1', 26);
insert into Codelist("id", title, serialized_codes, description, ref, project_id)
values (50, 'CodelistTittel4',
        '[{"ref":"cf2b2950-54d7-4b4e-9691-42ada1c233ff","title":"code1","description":"code beskrivelse1"},{"ref":"26da61be-0967-4cbf-8204-262b4e70e2fd","title":"code2","description":"code beskrivelse2"}]
', 'CodelistBeskrivelse2', 'edb2-431f-855a-4368e2bcddd1', 26);


-- PUBLICATION
insert into Publication("id", comment, version, date, ref, project_id)
values (8, 'comment1', 2, '2020-09-29 21:17:30.23195', 'zzz4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Publication("id", comment, version, date, ref, project_id)
values (9, 'comment2', 4, '1999-09-10 21:17:30.23195', 'xxx4db69-edb2-431f-855a-4368e2bcddd1', 3);

-- NEED
insert into Need("id", title, description, ref, project_id)
values (10, 'Need tittel fra script', 'Need beskrivelse fra script', 'need1b69-edb2-431f-855a-4368e2bcddd1', 2);
insert into Need("id", title, description, ref, project_id)
values (11, 'Need tittel fra script #2', 'Need beskrivelse fra script #2', 'need2b69-edb2-431f-855a-4368e2bcddd1', 2);

-- REQUIREMENT
insert into Requirement("id", title, description, ref, project_id, need_id)
values (12, 'Requirement tittel fra script', 'Requirement beskrivelse fra script',
        'req1b69-edb2-431f-855a-4368e2bcddd1', 2, 10);
insert into Requirement("id", title, description, ref, project_id, need_id)
values (13, 'Requirement tittel fra script #2', 'Requirement beskrivelse fra script #2',
        'reqd2b69-edb2-431f-855a-4368e2bcddd1', 2, 10);

--REQUIREMENT VARIANT
insert into RequirementVariant("id", description, instruction, ref, requirementtext, useproduct, usequalification,
                               usespecification, requirement_id)
values (14, 'Requirement variant beskrivelse fra script', 'instruksjon', 'rvrv1b69-edb2-431f-855a-4368e2bcddd1',
        'req text', true, true, true, 12);
insert into RequirementVariant("id", description, instruction, ref, requirementtext, useproduct, usequalification,
                               usespecification, requirement_id)
values (15, 'Requirement variant beskrivelse fra script #2 ', 'instruksjon', 'rvrv2b69-edb2-431f-855a-4368e2bcddd1',
        'req text', true, true, true, 12);
insert into RequirementVariant("id", description, instruction, ref, requirementtext, useproduct, usequalification,
                               usespecification, requirement_id)
values (16, 'Requirement variant beskrivelse fra script #2 ', 'instruksjon', 'rvrv3b69-edb2-431f-855a-4368e2bcddd1',
        'req text', true, true, true, 12);

-- PRODUCT
insert into product("id", title, description, ref, project_id, requirementvariant_id)
values (5, 'ProduktTittel1', 'ProduktBeskrivelse1', 'edb4db69-edb2-431f-855a-4368e2bcddd1', 3, 14);
insert into Product("id", title, description, ref, project_id, requirementvariant_id)
values (6, 'ProduktTittel2', 'ProduktBeskrivelse2', 'kuk4db69-edb2-431f-855a-4368e2bcddd1', 3, 14);
insert into Product("id", title, description, ref, project_id, requirementvariant_id)
values (7, 'ProduktTittel3', 'ProduktBeskrivelse3', 'kua4db69-edb2-431f-855a-4368e2bcddd1', 1, 14);


-- DELETE RECORD
insert into DeletedRecord (id, data, deletedAt, updatedAt, tableName, objectId)
values (67, '{
  "id": 999,
  "ref": "ref-til-product-999",
  "title": "product-9999 tittel",
  "description": "propduct-9999-beskrivelse",
  "project_id_fk": 3,
  "requirementvariant_id_fk": 14
}', '2021-09-29 21:17:30.23195', null, 'product', 999)
ON CONFLICT DO NOTHING;

insert into DeletedRecord (id, data, deletedAt, updatedAt, tableName, objectId)
values (68, '{
  "id": 998,
  "ref": "ref-til-product-998",
  "title": "product-998 tittel",
  "description": "propduct-9999-beskrivelse",
  "project_id_fk": 3,
  "requirementvariant_id_fk": 14
}', '2021-09-29 21:17:30.23195', null, 'product', 998)
ON CONFLICT DO NOTHING;


-- PUBLICATION EXPORT
insert into PublicationExport("id", ref, publicationRef, serializedProject)
values (30, 'export-2314456-32454236', 'zzz4db69-edb2-431f-855a-4368e2bcddd1',
        '{"id":3,"ref":"bbb4db69-edb2-431f-855a-4368e2bcddd1","title":"Prosjekt3","description":"Beskrivelse3","products":[{"id":5,"title":"ProduktTittel1","description":"ProduktBeskrivelse1","ref":"edb4db69-edb2-431f-855a-4368e2bcddd1"},{"id":6,"title":"ProduktTittel2","description":"ProduktBeskrivelse2","ref":"kuk4db69-edb2-431f-855a-4368e2bcddd1"}],"publications":[{"id":8,"ref":"zzz4db69-edb2-431f-855a-4368e2bcddd1","comment":"comment1","date":[2020,9,29,21,17,30,231950000],"version":2,"publicationExportRef":null},{"id":9,"ref":"xxx4db69-edb2-431f-855a-4368e2bcddd1","comment":"comment2","date":[1999,9,10,21,17,30,231950000],"version":4,"publicationExportRef":null}],"requirements":[],"needs":[],"codelist":[{"id":4,"title":"CodelistTittel1","description":"CodelistBeskrivelse1","ref":"qqq4db69-edb2-431f-855a-4368e2bcddd1"},{"id":5,"title":"CodelistTittel2","description":"CodelistBeskrivelse2","ref":"asd4db69-edb2-431f-855a-4368e2bcddd1"}]}');

--CONFIG
select setval('hibernate_sequence', 100, true);