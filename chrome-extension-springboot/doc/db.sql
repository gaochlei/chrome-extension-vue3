-- 用户表
drop table if exists `test`;
create table `test` (
                        `id` bigint not null comment 'ID',
                        `login_name` varchar(50) not null comment '登陆名',
                        `name` varchar(50) comment '昵称',
                        `password` char(32) not null comment '密码',
                        primary key (`id`),
                        unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `test` (id)
values (1);

select * from test

drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment 'ID',
                        `name` varchar(50) comment '昵称',
                        `password` char(32) not null comment '密码',
                        primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';


insert into `demo` (id, name, password) values (1,'lusy','lllL');
insert into `demo` (id, name, password) values (2,'dannu','lllL');
insert into `demo` (id,  name, password) values (3,'wwang','lllL');
insert into `demo` (id,  name, password) values (4,'zhang','lllL');

--网站表
drop table if exists `website`;
create table `website` (
                        `id` bigint not null comment 'ID',
                        `user_id` bigint not null default 0 comment '用户id',
                        `parent_id` bigint default 0 comment '文件夹id',
                        `parent_name` varchar(50) comment '文件夹名称',
                        `website_name` varchar(50) not null comment '网站名',
                        `website_nick` varchar(50) comment '网站别名',
                        `site_url` varchar(100) not null comment '网站地址',
                        `name_nick` tinyint(1) not null comment '是否显示为别名，true，false',
                        primary key (`id`),
                        unique key `id` (`id`)

) engine=innodb default charset=utf8mb4 comment='网站表';