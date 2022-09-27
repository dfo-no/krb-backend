
--PROJECT

insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(1,'Prosjekt1', 'Beskrivelse1', '1','030303','Ikke slettet', 'ccc4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(2,'Prosjekt2', 'Beskrivelse2', '1','030303','Ikke slettet', 'aaa4db69-edb2-431f-855a-4368e2bcddd1');
insert into Project("id",title, description, version, publisheddate, deleteddate, ref)
values(3,'Prosjekt3', 'Beskrivelse3', '1','010101','Ikke slettet', 'bbb4db69-edb2-431f-855a-4368e2bcddd1');


--CODELIST

insert into Codelist("id",title, description,  ref)
values(4,'CodelistTittel1', 'CodelistBeskrivelse1', 'qqq4db69-edb2-431f-855a-4368e2bcddd1');
insert into Codelist("id",title, description,  ref)
values(5,'CodelistTittel2', 'CodelistBeskrivelse2', 'asd4db69-edb2-431f-855a-4368e2bcddd1');

insert into project_codelist(project_id, codelist_id)
values(3,4);

insert into project_codelist(project_id, codelist_id)
values(3,5);

-- PRODUCT

insert into Product("id",title, description,  ref, deleteddate)
values(5,'ProduktTittel1', 'ProduktBeskrivelse1', 'edb4db69-edb2-431f-855a-4368e2bcddd1', '00-00-00');
insert into Product("id",title, description,  ref, deleteddate)
values(6,'ProduktTittel2', 'ProduktBeskrivelse2', 'kuk4db69-edb2-431f-855a-4368e2bcddd1', '01-02-03');
insert into Product("id",title, description,  ref, deleteddate)
values(7,'ProduktTittel3', 'ProduktBeskrivelse3', 'kua4db69-edb2-431f-855a-4368e2bcddd1', '01-02-03');

insert into project_product(project_id, products_id)
values(3,5);
insert into project_product(project_id, products_id)
values(3,6);
insert into project_product(project_id, products_id)
values(1,7);

-- PUBLICATION

insert into Publication("id",comment, version, date,  ref, deleteddate)
values(8,'comment1', 'date', 'v11','zzz4db69-edb2-431f-855a-4368e2bcddd1', 'Ikke satt, endre type');
insert into Publication("id",comment, version, date,  ref, deleteddate)
values(9,'comment2', 'date2', 'v123','xxx4db69-edb2-431f-855a-4368e2bcddd1', '');


insert into project_publication(project_id, publications_id)
values(3,8);
insert into project_publication(project_id, publications_id)
values(3,9);

-- NEED

insert into Need("id",title, description, ref)
values(10,'Need tittel fra script', 'Need beskrivelse fra script','need1b69-edb2-431f-855a-4368e2bcddd1');
insert into Need("id",title, description, ref)
values(11,'Need tittel fra script #2', 'Need beskrivelse fra script #2','need2b69-edb2-431f-855a-4368e2bcddd1');

insert into project_need(project_id, needs_id)
values(2,10);
insert into project_need(project_id, needs_id)
values(2,11);

-- REQUIREMENT

insert into Requirement("id",title, description, ref)
values(12,'Requirement tittel fra script', 'Requirement beskrivelse fra script','req1b69-edb2-431f-855a-4368e2bcddd1');
insert into Requirement("id",title, description, ref)
values(13,'Requirement tittel fra script #2', 'Requirement beskrivelse fra script #2','reqd2b69-edb2-431f-855a-4368e2bcddd1');

insert into project_requirement(project_id, requirements_id)
values(2,12);
insert into project_requirement(project_id, requirements_id)
values(2,13);






--CONFIG
select setval('hibernate_sequence', 100, true);
