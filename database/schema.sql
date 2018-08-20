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


-- also named 'Hospodářská jednotka' in SAP
create table PROJECT (
    id       text primary key not null,
    name     text not null,
    created  text,
    modified text
);

create table BUILDING (
    id       text primary key not null,
    project  text not null,
    name     text not null,
    created  text,
    modified text,
    foreign key (project) references PROJECT(id)
);

create table FLOOR (
    id       text primary key not null,
    building text not null,
    name     text not null,
    created  text,
    modified text,
    foreign key (building) references BUILDING(id)
);

create table SAP_ROOM (
    id          text primary key asc,
    version     char, -- 'C'-current 'N'-new change in SAP
    floor       text not null,
    name        text, -- oznaceni
    room_type   integer not null,
    created     text,
    modified    text,
    valid_from  text,
    valid_to    text,
    area        float,
    capacity    integer,
    occupation  char, -- 'E'-external 'I'-internal 'F'-free
    occupied_by text,
    foreign key (room_type) references ROOM_TYPE(id),
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

create table ROOM_TYPE (
    id       integer primary key asc,
    label    text
);

create table DRAWING (
    id       integer primary key asc,
    floor    text not null,
    name     text not null,
    created  text,
    modified text,
    version  integer not null,
    foreign key (floor) references FLOOR(id)
);

create table DRAWING_NEW (
    id            integer primary key asc,
    area_aoid     text not null,
    building_aoid text not null,
    floor_aoid    text not null,
    valid_from    integer
);

create table USERS (
    id                  integer primary key autoincrement,
    name                text,
    resolution          text,
    selected_room_color text,
    pen_width           text
);
