-- 0==false, 1==true
INSERT INTO frame_dev.`user`
(id,version,account_non_expired,account_non_locked,credentials_non_expired,enabled,password,phone,pre_register,username)
VALUES
(1,1,1,1,1,1,'$2a$10$53hI3hGXOH3SAcksuUpeDO/hWHe50vODW7AZYqieOhTSfKDJhcSLi','13000000000', 0, 'test_user');
-- 上面对应的密码为 111111

INSERT INTO frame_dev.`role`
(id,version,description,display_name,enabled,name)
VALUES
(1,1,NULL,'ROLE_BUYER',1,'ROLE_BUYER');

INSERT INTO frame_dev.`user_role`
(user_id,role_id)
VALUES
(1,1);