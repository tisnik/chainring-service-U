insert into PROJECT(id, AOID, name, created, modified) values(1, 'SAP10000', 'Areal 1', datetime('now'), datetime('now'));
insert into PROJECT(id, AOID, name, created, modified) values(2, 'SAP20000', 'Areal 2', datetime('now'), datetime('now'));
insert into PROJECT(id, AOID, name, created, modified) values(3, 'SAP30000', 'Areal 3', datetime('now'), datetime('now'));

insert into BUILDING(id, project, AOID, name, created, modified) values(1, 1, 'SAP11000', 'Budova 1', datetime('now'), datetime('now'));
insert into BUILDING(id, project, AOID, name, created, modified) values(2, 1, 'SAP12000', 'Budova 2', datetime('now'), datetime('now'));
insert into BUILDING(id, project, AOID, name, created, modified) values(3, 1, 'SAP13000', 'Budova 3', datetime('now'), datetime('now'));

insert into FLOOR(id, building, AOID, name, created, modified) values(1, 1, 'SAP11100', 'Podlazi 1', datetime('now'), datetime('now'));
insert into FLOOR(id, building, AOID, name, created, modified) values(2, 1, 'SAP11110', 'Podlazi 2', datetime('now'), datetime('now'));
insert into FLOOR(id, building, AOID, name, created, modified) values(3, 1, 'SAP11120', 'Podlazi 3', datetime('now'), datetime('now'));

insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 1, 'C', 1, 'místnost1', 'SAP1110001', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 2, 'C', 1, 'místnost2', 'SAP1110002', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 3, 'C', 1, 'místnost3', 'SAP1110003', 3, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 4, 'C', 1, 'místnost4', 'SAP1110004', 4, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');

insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 5, 'C', 2, 'místnost1', 'SAP1111001', 5, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 6, 'C', 2, 'místnost2', 'SAP1111002', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 7, 'C', 2, 'místnost3', 'SAP1111003', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 8, 'C', 2, 'místnost4', 'SAP1111004', 3, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');

insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values( 9, 'C', 3, 'místnost1', 'SAP1112001', 4, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values(10, 'C', 3, 'místnost2', 'SAP1112002', 5, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values(11, 'C', 3, 'místnost3', 'SAP1112003', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, AOID, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values(12, 'C', 3, 'místnost4', 'SAP1112004', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');

insert into DRAWING(id, floor, name, AOID, version, created, modified) values (1, 1, "prvni verze", "AOID1", 1, datetime('now'), datetime('now'));

insert into USERS(name) values('tester');

-- use only when the system is configured to use DB for storing drawings
--insert into drawing_raw_data(id, drawing, raw_data) values(1, 1, readfile('drawing1.drw'));
