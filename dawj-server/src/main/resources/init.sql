

delete from `user`;
delete from `organization`;
delete from `area`;

INSERT INTO `organization` (id,create_time,description,last_modify_time,name,parent_id,principal_user_id,position,deleted) values ('orgRoot',NOW(),'系统创建的顶级部门',NOW(),'XXX数据中心',null,null,0,false);
INSERT INTO `user` (`id`, `birthday`, `create_time`, `email`, `last_login_time`, `last_modify_time`, `mobile`, `name`, `password`, `username`, `organization_id`, `sex`,`position`,`deleted`,`isSysAdmin`) VALUES ('user1', '2015-12-22 00:33:02', '2015-12-22 00:33:04', '', null, null, '', '管理员', 'e10adc3949ba59abbe56e057f20f883e', 'admin', 'orgRoot', 1,0,false,true);
INSERT INTO `area` (`id`, `create_time`, `last_modify_time`, `deleted`, `description`, `name`, `position`, `scene_data`, `scene_object_id`, `type`, `parent_id`) VALUES ('areaRoot', '2016-01-20 12:24:12', '2016-01-20 12:24:12', '\0', 'xxx数据中心', 'xxx数据中心', '0', null, null, '0', null);
