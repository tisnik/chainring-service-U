--
--  (C) Copyright 2017, 2018  Pavel Tisnovsky
--
--  All rights reserved. This program and the accompanying materials
--  are made available under the terms of the Eclipse Public License v1.0
--  which accompanies this distribution, and is available at
--  http://www.eclipse.org/legal/epl-v10.html
--
--  Contributors:
--      Pavel Tisnovsky
--


-- also named 'Areal' in SAP
create table PROJECT (
    id       integer primary key asc,
    name     text not null,
    sap      text,
    created  text
);

create table BUILDING (
    id       integer primary key asc,
    project  integer not null,
    name     text not null,
    sap      text,
    created  text,
    foreign key (project) references PROJECT(id)
);

create table FLOOR (
    id       integer primary key asc,
    building integer not null,
    name     text not null,
    sap      text,
    created  text,
    foreign key (building) references BUILDING(id)
);

create table SAP_ROOM (
    id       integer primary key asc,
    floor    integer not null,
    sap      text,
    created  text,
    foreign key (floor) references FLOOR(id)
);

create table ATTRIBUTE_TYPE (
    id       integer primary key asc,
    name     text not null
);

create table ROOM_ATTRIBUTE (
    id       integer primary key asc,
    room     integer not null,
    type     integer not null,
    value    text,
    foreign key (type) references ATTRIBUTE_TYPE(id)
    foreign key (room) references SAP_ROOM(id)
);

create table DRAWING (
    id       integer primary key asc,
    floor    integer not null,
    name     text not null,
    sap      text,
    created  text,
    version  integer not null,
    foreign key (floor) references FLOOR(id)
);

create table DRAWING_RAW_DATA (
    id       integer primary key asc,
    drawing  integer not null,
    raw_data blob,
    foreign key (drawing) references DRAWING(id)
);

create table DRAWING_ROOM (
    id       integer primary key asc,
    drawing  integer not null,
    sap      text,
    foreign key (drawing) references DRAWING(id)
);

create table ROOM_VERTEX (
    id       integer primary key asc,
    room     integer not null,
    x        real not null,
    y        real not null,
    foreign key (room) references DRAWING_ROOM(id)
);

