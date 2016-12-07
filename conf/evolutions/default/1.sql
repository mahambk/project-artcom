# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table member (
  username                      varchar(255) not null,
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  email                         varchar(255),
  date_joined                   timestamp,
  dob                           timestamp,
  bio                           varchar(255),
  profile_pic                   varchar(255),
  constraint pk_member primary key (username)
);


# --- !Downs

drop table if exists member cascade;

