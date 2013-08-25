insert into co_application(co_application_id, name, secret_key, isacive, created, updated, createdby, updatedby)
values(0,'UtopiaUI','UtopiaSecret',1,sysdate,sysdate,1,1);

insert into CO_APP_USCS_ACCSS(co_app_uscs_accss_id, co_application_id, co_usecase_id, co_usecase_action_id) 
values (1,0,null,null);


commit;
