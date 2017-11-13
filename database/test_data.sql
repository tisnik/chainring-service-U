insert into project(id, name) values(1, 'Testovaci projekt 1');

insert into building(id, project, name) values(1, 1, 'Budova 1');
insert into building(id, project, name) values(2, 1, 'Budova 2');

insert into drawing(id, building, name) values(1, 1, 'Podlazi 1');
insert into drawing(id, building, name) values(2, 1, 'Podlazi 2');
insert into drawing(id, building, name) values(3, 1, 'Podlazi 3');

