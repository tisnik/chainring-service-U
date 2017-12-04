insert into project(id, sap, name) values(1, 'SAP10000', 'Testovaci projekt 1');

insert into building(id, project, sap, name) values(1, 1, 'SAP11000', 'Budova 1');
insert into building(id, project, sap, name) values(2, 1, 'SAP12000', 'Budova 2');

insert into drawing(id, building, sap, name) values(1, 1, 'SAP11100', 'Podlazi 1');
insert into drawing(id, building, sap, name) values(2, 1, 'SAP11110', 'Podlazi 2');
insert into drawing(id, building, sap, name) values(3, 1, 'SAP11120', 'Podlazi 3');

--insert into drawing_raw_data(id, drawing, raw_data) values(1, 1, readfile('drawing1.drw'));
