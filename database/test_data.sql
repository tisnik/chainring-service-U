insert into PROJECT(id, name, created, modified) values('HOST', 'Areal 1', datetime('now'), datetime('now'));
insert into PROJECT(id, name, created, modified) values('SAP20000', 'Areal 2', datetime('now'), datetime('now'));
insert into PROJECT(id, name, created, modified) values('SAP30000', 'Areal 3', datetime('now'), datetime('now'));

insert into BUILDING(id, project, name, created, modified) values('HOST.10', 'HOST', 'Budova 1', datetime('now'), datetime('now'));
insert into BUILDING(id, project, name, created, modified) values('HOST.20', 'HOST', 'Budova 2', datetime('now'), datetime('now'));
insert into BUILDING(id, project, name, created, modified) values('HOST.30', 'HOST', 'Budova 3', datetime('now'), datetime('now'));

insert into FLOOR(id, building, name, created, modified) values('HOST.10.1S', 'HOST.10', 'Podlazi 1', datetime('now'), datetime('now'));
insert into FLOOR(id, building, name, created, modified) values('HOST.10.2S', 'HOST.10', 'Podlazi 2', datetime('now'), datetime('now'));
insert into FLOOR(id, building, name, created, modified) values('HOST.10.3S', 'HOST.10', 'Podlazi 3', datetime('now'), datetime('now'));

insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.01', 'C', 'HOST.10.1S', 'místnost1', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.02', 'C', 'HOST.10.1S', 'místnost2', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.03', 'C', 'HOST.10.1S', 'místnost3', 3, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.04', 'C', 'HOST.10.1S', 'místnost4', 4, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.05', 'C', 'HOST.10.1S', 'místnost1', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.06', 'C', 'HOST.10.1S', 'místnost2', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.07', 'C', 'HOST.10.1S', 'místnost3', 3, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.1S.08', 'C', 'HOST.10.1S', 'místnost4', 4, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');

insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.2S.01', 'C', 'HOST.10.2S', 'místnost1', 5, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.2S.02', 'C', 'HOST.10.2S', 'místnost2', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.2S.03', 'C', 'HOST.10.2S', 'místnost3', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.2S.04', 'C', 'HOST.10.2S', 'místnost4', 3, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');

insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.3S.01', 'C', 'HOST.10.3S', 'místnost1', 4, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 10, 10, "interni 1", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.3S.02', 'C', 'HOST.10.3S', 'místnost2', 5, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 20, 1, "volna", 'F');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.3S.03', 'C', 'HOST.10.3S', 'místnost3', 1, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12',  5, 1, "interni 2", 'I');
insert into SAP_ROOM(id, version, floor, name, room_type, created, modified, valid_from, valid_to, area, capacity, occupied_by, occupation) values('HOST.10.3S.04', 'C', 'HOST.10.3S', 'místnost4', 2, datetime('now'), datetime('now'), '2000-01-01', '2099-31-12', 15, 2, "Novak", 'E');

insert into DRAWING(id, floor, name, version, created, modified) values (1, 'HOST.10.1S', "prvni verze", datetime('now'), datetime('now'), 1);
insert into DRAWING(id, floor, name, version, created, modified) values (2, 'HOST.10.2S', "prvni verze", datetime('now'), datetime('now'), 1);
insert into DRAWING(id, floor, name, version, created, modified) values (3, 'HOST.10.3S', "prvni verze", datetime('now'), datetime('now'), 1);

insert into USERS(name) values('tester');

