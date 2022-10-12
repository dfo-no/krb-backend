
--PROJECT
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(1,'Prosjekt1', 'Beskrivelse1', 1,'2020-09-29 21:17:30.23195',null, 'ccc4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(2,'Prosjekt2', 'Beskrivelse2',2,'2021-09-29 21:17:30.23195',null, 'aaa4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(3,'Prosjekt3', 'Beskrivelse3', 3,'2000-09-29 21:17:30.23195',null, 'bbb4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(22,'ScriptProsjekt4', 'Beskrivelse3', 4,'1999-09-29 21:17:30.23195','2021-09-29 21:17:30.23195', 'prosjekt4-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(24,'ScriptProsjekt5', 'Beskrivelse3', 5,'2005-09-29 21:17:30.23195',null, 'prosjekt5-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(26,'ScriptProsjekt6', 'Beskrivelse3', 6,'1998-09-29 21:17:30.23195',null, 'prosjekt6-edb2-431f-855a-4368e2bcddd1');


--CODELIST

insert into Codelist("id",title, description,  ref, project_id_fk)
values(4,'CodelistTittel1', 'CodelistBeskrivelse1', 'qqq4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Codelist("id",title, description,  ref, project_id_fk)
values(5,'CodelistTittel2', 'CodelistBeskrivelse2','asd4db69-edb2-431f-855a-4368e2bcddd1', 3);
insert into Codelist("id",title, description,  ref, project_id_fk)
values(21,'CodelistTittel3', 'CodelistBeskrivelse2', 'newlist14db69-edb2-431f-855a-4368e2bcddd1', 22);
insert into Codelist("id",title, description,  ref, project_id_fk)
values(25,'CodelistTittel4', 'CodelistBeskrivelse2', 'newlist2222db69-edb2-431f-855a-4368e2bcddd1', 24);
insert into Codelist("id",title, description,  ref, project_id_fk)
values(27,'CodelistTittel4', 'CodelistBeskrivelse2', 'newlist33333db69-edb2-431f-855a-4368e2bcddd1', 26);


-- insert into project_codelist(project_id, codelist_id)
-- values(3,4);
-- insert into project_codelist(project_id, codelist_id)
-- values(3,5);
-- insert into project_codelist(project_id, codelist_id)
-- values(22,21);
-- insert into project_codelist(project_id, codelist_id)
-- values(24,25);
-- insert into project_codelist(project_id, codelist_id)
-- values(26,27);

-- PRODUCT
insert into product("id",title, description,  ref, deleteddate, project_id_fk)
values(5,'ProduktTittel1', 'ProduktBeskrivelse1', 'edb4db69-edb2-431f-855a-4368e2bcddd1', null, 3);
insert into Product("id",title, description,  ref, deleteddate, project_id_fk)
values(6,'ProduktTittel2', 'ProduktBeskrivelse2', 'kuk4db69-edb2-431f-855a-4368e2bcddd1', null, 3);
insert into Product("id",title, description,  ref, deleteddate, project_id_fk)
values(7,'ProduktTittel3', 'ProduktBeskrivelse3', 'kua4db69-edb2-431f-855a-4368e2bcddd1', null, 1);


--insert into project_product(project_id, products_id)
--values(3,5);
--insert into project_product(project_id, products_id)
--values(3,6);
-- insert into project_product(project_id, products_id)
-- values(1,7);

-- PUBLICATION
insert into Publication("id",comment, version, date,  ref, deleteddate, project_id_fk)
values(8,'comment1', 2, '2019-10-04T12:27:55.191667','zzz4db69-edb2-431f-855a-4368e2bcddd1', null, 3);
insert into Publication("id",comment, version, date,  ref, deleteddate, project_id_fk)
values(9,'comment2', 4, '2020-10-04T12:27:55.191667','xxx4db69-edb2-431f-855a-4368e2bcddd1', null, 3);

-- insert into project_publication(project_id, publications_id)
-- values(3,8);
-- insert into project_publication(project_id, publications_id)
-- values(3,9);

-- NEED
insert into Need("id",title, description, ref, project_id_fk)
values(10,'Need tittel fra script', 'Need beskrivelse fra script','need1b69-edb2-431f-855a-4368e2bcddd1', 2);
insert into Need("id",title, description, ref, project_id_fk)
values(11,'Need tittel fra script #2', 'Need beskrivelse fra script #2','need2b69-edb2-431f-855a-4368e2bcddd1', 2);

-- insert into project_need(project_id, needs_id)
-- values(2,10);
-- insert into project_need(project_id, needs_id)
-- values(2,11);

-- REQUIREMENT
insert into Requirement("id",title, description, ref, project_id_fk)
values(12,'Requirement tittel fra script', 'Requirement beskrivelse fra script','req1b69-edb2-431f-855a-4368e2bcddd1', 2);
insert into Requirement("id",title, description, ref, project_id_fk)
values(13,'Requirement tittel fra script #2', 'Requirement beskrivelse fra script #2','reqd2b69-edb2-431f-855a-4368e2bcddd1', 2);

-- insert into project_requirement(project_id, requirements_id)
-- values(2,12);
-- insert into project_requirement(project_id, requirements_id)
-- values(2,13);

--REQUIREMENT VARIANT
insert into RequirementVariant("id",description, instruction, ref, requirementtext,useproduct, usequalification, usespesification )
values(14,'Requirement variant beskrivelse fra script','instruksjon','rvrv1b69-edb2-431f-855a-4368e2bcddd1', 'req text',true, true, true);
insert into RequirementVariant("id",description, instruction, ref, requirementtext,useproduct, usequalification, usespesification )
values(15,'Requirement variant beskrivelse fra script #2 ','instruksjon','rvrv2b69-edb2-431f-855a-4368e2bcddd1', 'req text',true, true, true);
insert into RequirementVariant("id",description, instruction, ref, requirementtext,useproduct, usequalification, usespesification )
values(16,'Requirement variant beskrivelse fra script #2 ','instruksjon','rvrv3b69-edb2-431f-855a-4368e2bcddd1', 'req text',true, true, true);

insert into requirement_requirementvariant(requirement_id, requirementvariants_id)
values(12,14);
insert into requirement_requirementvariant(requirement_id, requirementvariants_id)
values(12,15);
insert into requirement_requirementvariant(requirement_id, requirementvariants_id)
values(12,16);



-- CODE
insert into Code("id", title, description, ref, codelist_id_fk)
values(17,'code tittel fra script1','code beskrivelse fra script','script1b69-edb2-431f-855a-4368e2bcddd1', 4);
insert into Code("id", title, description, ref, codelist_id_fk)
values(18,'code tittel fra script2','code beskrivelse fra script','scrip21b69-edb2-431f-855a-4368e2bcddd1', 4);
insert into Code("id", title, description, ref, codelist_id_fk)
values(19,'code tittel fra script3','code beskrivelse fra script','script3b69-edb2-431f-855a-4368e2bcddd1', 5);
insert into Code("id", title, description, ref, codelist_id_fk)
values(20,'code tittel fra script4','code beskrivelse fra script','script4b69-edb2-431f-855a-4368e2bcddd1', 21);
insert into Code("id", title, description, ref, codelist_id_fk)
values(23,'code tittel fra script5','code beskrivelse fra script','script5b69-edb2-431f-855a-4368e2bcddd1', 21);
insert into Code("id", title, description, ref, codelist_id_fk)
values(28,'code tittel fra script6','code beskrivelse fra script','script6b69-edb2-431f-855a-4368e2bcddd1', 27);


--CONFIG
select setval('hibernate_sequence', 100, true);



