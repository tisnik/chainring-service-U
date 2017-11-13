create table project (
    id       integer primary key asc,
    name     text not null
);

create table building (
    id       integer primary key asc,
    project  integer not null,
    name     text not null,
    sap      text,
    foreign key (project) references project(id)
);

create table drawing (
    id       integer primary key asc,
    building integer not null,
    name     text not null,
    foreign key (building) references building(id)
);

create table entity_type (
    id       integer primary key asc,
    type     text not null
);

create table entity (
    id       integer primary key asc,
    drawing  integer not null,
    type     integer not null,
    foreign key (type) references entity_type(id)
    foreign key (drawing) references drawing(id)
);

create table attribute_type (
    id       integer primary key asc,
    name     text not null
);

create table room (
    id       integer primary key asc,
    drawing  integer not null,
    sap      text,
    foreign key (drawing) references drawing(id)
);

create table room_attribute (
    id       integer primary key asc,
    room     integer not null,
    type     integer not null,
    value    text,
    foreign key (type) references attribute_type(id)
    foreign key (room) references room(id)
);

create table room_vertex (
    id       integer primary key asc,
    room     integer not null,
    x        real not null,
    y        real not null,
    foreign key (room) references room(id)
);

