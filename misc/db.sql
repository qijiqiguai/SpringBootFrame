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


INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (1,1,'p01','p01','p01');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (2,1,'p02','p02','p02');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (3,1,'p03','p03','p03');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (4,1,'p04','p04','p04');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (5,1,'p05','p05','p05');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (6,1,'p06','p06','p06');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (7,1,'p07','p07','p07');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (8,1,'p08','p08','p08');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (9,1,'p09','p09','p09');
INSERT INTO frame_dev.`permission` (id,version,description,display_name,name) VALUES (10,1,'p10','p10','p10');