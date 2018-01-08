insert into PROJECT(id, sap, name, created) values(1, 'SAP10000', 'Areal 1', datetime('now'));
insert into PROJECT(id, sap, name, created) values(2, 'SAP20000', 'Areal 2', datetime('now'));
insert into PROJECT(id, sap, name, created) values(3, 'SAP30000', 'Areal 3', datetime('now'));

insert into BUILDING(id, project, sap, name, created) values(1, 1, 'SAP11000', 'Budova 1', datetime('now'));
insert into BUILDING(id, project, sap, name, created) values(2, 1, 'SAP12000', 'Budova 2', datetime('now'));
insert into BUILDING(id, project, sap, name, created) values(3, 1, 'SAP13000', 'Budova 3', datetime('now'));

insert into FLOOR(id, building, sap, name, created) values(1, 1, 'SAP11100', 'Podlazi 1', datetime('now'));
insert into FLOOR(id, building, sap, name, created) values(2, 1, 'SAP11110', 'Podlazi 2', datetime('now'));
insert into FLOOR(id, building, sap, name, created) values(3, 1, 'SAP11120', 'Podlazi 3', datetime('now'));

insert into SAP_ROOM(id, floor, sap, created) values(1, 1, 'SAP1110001', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(2, 1, 'SAP1110002', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(3, 1, 'SAP1110003', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(4, 1, 'SAP1110004', datetime('now'));

insert into SAP_ROOM(id, floor, sap, created) values(5, 2, 'SAP1111001', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(6, 2, 'SAP1111002', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(7, 2, 'SAP1111003', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(8, 2, 'SAP1111004', datetime('now'));

insert into SAP_ROOM(id, floor, sap, created) values( 9, 3, 'SAP1112001', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(10, 3, 'SAP1112002', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(11, 3, 'SAP1112003', datetime('now'));
insert into SAP_ROOM(id, floor, sap, created) values(12, 3, 'SAP1112004', datetime('now'));

--insert into drawing_raw_data(id, drawing, raw_data) values(1, 1, readfile('drawing1.drw'));
