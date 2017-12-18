insert into project(id, sap, name) values(1, 'SAP10000', 'Areal 1');
insert into project(id, sap, name) values(2, 'SAP20000', 'Areal 2');
insert into project(id, sap, name) values(3, 'SAP30000', 'Areal 3');

insert into building(id, project, sap, name) values(1, 1, 'SAP11000', 'Budova 1');
insert into building(id, project, sap, name) values(2, 1, 'SAP12000', 'Budova 2');
insert into building(id, project, sap, name) values(3, 1, 'SAP13000', 'Budova 3');

insert into floor(id, building, sap, name) values(1, 1, 'SAP11100', 'Podlazi 1');
insert into floor(id, building, sap, name) values(2, 1, 'SAP11110', 'Podlazi 2');
insert into floor(id, building, sap, name) values(3, 1, 'SAP11120', 'Podlazi 3');

insert into sap_room(id, floor, sap) values(1, 1, 'SAP1110001');
insert into sap_room(id, floor, sap) values(2, 1, 'SAP1110002');
insert into sap_room(id, floor, sap) values(3, 1, 'SAP1110003');
insert into sap_room(id, floor, sap) values(4, 1, 'SAP1110004');

insert into sap_room(id, floor, sap) values(5, 2, 'SAP1111001');
insert into sap_room(id, floor, sap) values(6, 2, 'SAP1111002');
insert into sap_room(id, floor, sap) values(7, 2, 'SAP1111003');
insert into sap_room(id, floor, sap) values(8, 2, 'SAP1111004');

insert into sap_room(id, floor, sap) values(9, 3, 'SAP1112001');
insert into sap_room(id, floor, sap) values(10, 3, 'SAP1112002');
insert into sap_room(id, floor, sap) values(11, 3, 'SAP1112003');
insert into sap_room(id, floor, sap) values(12, 3, 'SAP1112004');

--insert into drawing_raw_data(id, drawing, raw_data) values(1, 1, readfile('drawing1.drw'));
