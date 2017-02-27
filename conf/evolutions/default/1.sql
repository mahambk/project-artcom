# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table member (
  username                      varchar(255) not null,
  firstname                     varchar(255),
  lastname                      varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  dob                           timestamp,
  level                         varchar(255),
  bio                           varchar(255),
  profile_pic                   varchar(255),
  skills                        varchar(255),
  region                        varchar(255),
  date_joined                   timestamp,
  constraint pk_member primary key (username)
);


# --- !Downs

drop table if exists member cascade;

