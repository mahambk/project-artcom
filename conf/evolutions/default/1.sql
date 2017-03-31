# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                            serial not null,
  cmnt_body                     varchar(255),
  author_username               varchar(255),
  post_id                       integer,
  date_time_sent                timestamp,
  constraint pk_comment primary key (id)
);

create table feedback (
  id                            serial not null,
  fdbk_body                     varchar(255),
  author_username               varchar(255),
  post_id                       integer,
  date_time_sent                timestamp,
  constraint pk_feedback primary key (id)
);

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
  location                      varchar(255),
  date_joined                   timestamp,
  constraint pk_member primary key (username)
);

create table post (
  id                            serial not null,
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
  constraint pk_post primary key (id)
);

create table project (
  id                            serial not null,
  project_title                 varchar(255),
  description                   varchar(255),
  date_created                  timestamp,
  constraint pk_project primary key (id)
);

create table project_creators (
  project_id                    integer not null,
  creator_username              varchar(255) not null,
  constraint pk_project_creators primary key (project_id,creator_username)
);

alter table comment add constraint fk_comment_author_username foreign key (author_username) references member (username) on delete restrict on update restrict;
create index ix_comment_author_username on comment (author_username);

alter table comment add constraint fk_comment_post_id foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_comment_post_id on comment (post_id);

alter table feedback add constraint fk_feedback_author_username foreign key (author_username) references member (username) on delete restrict on update restrict;
create index ix_feedback_author_username on feedback (author_username);

alter table feedback add constraint fk_feedback_post_id foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_feedback_post_id on feedback (post_id);

alter table post add constraint fk_post_author_username foreign key (author_username) references member (username) on delete restrict on update restrict;
create index ix_post_author_username on post (author_username);

alter table project_creators add constraint fk_project_creators_project foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_project_creators_project on project_creators (project_id);

alter table project_creators add constraint fk_project_creators_member foreign key (creator_username) references member (username) on delete restrict on update restrict;
create index ix_project_creators_member on project_creators (creator_username);


# --- !Downs

alter table if exists comment drop constraint if exists fk_comment_author_username;
drop index if exists ix_comment_author_username;

alter table if exists comment drop constraint if exists fk_comment_post_id;
drop index if exists ix_comment_post_id;

alter table if exists feedback drop constraint if exists fk_feedback_author_username;
drop index if exists ix_feedback_author_username;

alter table if exists feedback drop constraint if exists fk_feedback_post_id;
drop index if exists ix_feedback_post_id;

alter table if exists post drop constraint if exists fk_post_author_username;
drop index if exists ix_post_author_username;

alter table if exists project_creators drop constraint if exists fk_project_creators_project;
drop index if exists ix_project_creators_project;

alter table if exists project_creators drop constraint if exists fk_project_creators_member;
drop index if exists ix_project_creators_member;

drop table if exists comment cascade;

drop table if exists feedback cascade;

drop table if exists member cascade;

drop table if exists post cascade;

drop table if exists project cascade;

drop table if exists project_creators cascade;

