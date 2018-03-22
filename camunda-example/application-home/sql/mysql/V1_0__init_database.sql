create table Document (
    id integer not null auto_increment,
    data longblob,
    description varchar(255),
    groupName varchar(255),
    initiator varchar(255),
    name varchar(255),
    state varchar(255),
    version datetime(6),
    primary key (id)
) engine=InnoDB;