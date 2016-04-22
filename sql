CREATE SCHEMA `commonservice` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE commonservice;

CREATE TABLE `t_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filename` varchar(256) NOT NULL COMMENT '本地上传文件名',
  `remote_filename` varchar(256) NOT NULL COMMENT '远程服务唯一文件名',
  `group_name` varchar(45) NOT NULL COMMENT '远程分组名',
  `biz` int(2) NOT NULL COMMENT '业务方标示 如 order=1 user=2',
  `biz_key` int(2) NOT NULL COMMENT '特定业务方 特定关键字 如biz=1 key可以有order=1 car=2',
  `biz_key_id` bigint(20) NOT NULL COMMENT '特定业务方 特定关键字 对应的外部id 如biz=order,key=car key_id=car_id',
  `file_size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小 单位字节',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `biz_key_key_id_index` (`biz`,`biz_key`,`biz_key_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2500 DEFAULT CHARSET=utf8;


