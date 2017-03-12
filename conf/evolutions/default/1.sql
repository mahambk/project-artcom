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

create table post (
  post_id                       serial not null,
  post_type                     varchar(255),
  title                         varchar(255),
  subtitle                      varchar(255),
  author_username               varchar(255),
  description                   varchar(255),
  tags                          varchar(255),
  category                      varchar(255),
  image_file                    varchar(255),
  date_posted                   timestamp,
  date_last_edited              timestamp,
  feedback_enabled              boolean,
  constraint pk_post primary key (post_id)
);

alter table post add constraint fk_post_author_username foreign key (author_username) references member (username) on delete restrict on update restrict;
create index ix_post_author_username on post (author_username);


# --- !Downs

alter table if exists post drop constraint if exists fk_post_author_username;
drop index if exists ix_post_author_username;

drop table if exists member cascade;

drop table if exists post cascade;

