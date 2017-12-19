create table person (
    id integer not null auto_increment,
    birthDay datetime,
    cashAmount double precision,
    gender varchar(255),
    lastname varchar(255) not null,
    name varchar(255) not null,
    pictureData longblob,
    version datetime not null,
    primary key (id)
);
