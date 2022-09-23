
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




--CONFIG
select setval('hibernate_sequence', 5, true);
