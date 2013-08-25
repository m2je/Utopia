create table CO_APPLICATION
(
  CO_APPLICATION_ID NUMBER(10) not null,
  NAME              VARCHAR2(3000) not null,
  SECRET_KEY        VARCHAR2(3000) not null,
  ISACIVE           NUMBER(1) default 1 not null,
  CREATED           DATE default sysdate not null,
  UPDATED           DATE default sysdate not null,
  CREATEDBY         NUMBER(10) not null,
  UPDATEDBY         NUMBER(10) not null
)
tablespace CORE_DEFAULT
  pctfree 10
  initrans 1
  maxtrans 255;
alter table CO_APPLICATION
  add constraint PK_CO_APPLICATION primary key (CO_APPLICATION_ID);
  
alter table CO_APPLICATION
  add constraint UN_CO_APPLICATION_NAME unique (NAME);
  
  -- Create table
create table co_app_uscs_accss
(
  co_app_uscs_accss_id number(10) not null,
  co_application_id    number(10) not null,
  co_usecase_id        number(10),
  co_usecase_action_id number(10)
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table co_app_uscs_accss
  add constraint PK_app_uscs_accss primary key (CO_APP_USCS_ACCSS_ID);
alter table co_app_uscs_accss
  add constraint UN_co_app_uscs_accss unique (CO_USECASE_ID, CO_APPLICATION_ID);
alter table co_app_uscs_accss
  add constraint FK_app_uscs_accss_uscase_id foreign key (CO_USECASE_ID)
  references co_usecase (CO_USECASE_ID) on delete cascade;
alter table co_app_uscs_accss
  add constraint FK_co_app_uscs_accss_app_Id foreign key (CO_APPLICATION_ID)
  references co_application (CO_APPLICATION_ID) on delete cascade;
  
  -- Create table
create table CO_USER_APP_TOKEN
(
  CO_USER_APP_TOKEN_ID number(10) not null,
  CO_USER_ID           number(10) not null,
  co_application_id    number(10) not null,
  token                varchar2(3000) not null,
  valid_to             date not null,
  created              date default sysdate not null,
  updated              date default sysdate not null
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table CO_USER_APP_TOKEN
  add constraint PK_CO_USER_APP_TOKEN primary key (CO_USER_APP_TOKEN_ID);
alter table CO_USER_APP_TOKEN
  add constraint FK_CO_USER_APP_TOKEN_User foreign key (CO_USER_ID)
  references co_user (CO_USER_ID) on delete cascade;
alter table CO_USER_APP_TOKEN
  add constraint FK_CO_USER_APP_TOKEN_APP foreign key (CO_APPLICATION_ID)
  references co_application (CO_APPLICATION_ID) on delete cascade;