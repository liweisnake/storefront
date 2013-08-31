insert into jbp_users (jbp_uid, jbp_uname, jbp_password, jbp_realemail, jbp_regdate, jbp_viewrealemail, jbp_enabled) values ('1', 'admin', MD5('admin'), 'portal@example.com', NOW(), '1', '1');  
insert into jbp_users (jbp_uid, jbp_uname, jbp_password, jbp_realemail, jbp_regdate, jbp_viewrealemail, jbp_enabled) values ('2', 'user', MD5('user'), 'portal@example.com', NOW(), '1', '1');  
insert into jbp_roles (jbp_rid, jbp_name, jbp_displayname) values ('1', 'Admin', 'Administrators');
insert into jbp_roles (jbp_rid, jbp_name, jbp_displayname) values ('2', 'User', 'Users');
insert into jbp_role_membership (jbp_uid, jbp_rid) values ('1', '1');
insert into jbp_role_membership (jbp_uid, jbp_rid) values ('2', '2');
