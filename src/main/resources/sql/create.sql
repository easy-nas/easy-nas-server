create table user
(
	username varchar(30) default '' not null comment '用户名',
	password_salt_md5 varchar(100) default '' not null comment '密码撒盐的md5'
)
comment '用户信息表';

create unique index user_username_uindex
	on user (username);

alter table user
	add constraint user_pk
		primary key (username);
