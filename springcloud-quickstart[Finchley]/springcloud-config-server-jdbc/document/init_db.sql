--创建数据库
CREATE DATABASE IF NOT EXISTS jeedev DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE `properties` (
  `id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `value` varchar(500) NOT NULL,
  `application` varchar(50) NOT NULL,
  `profile` varchar(50) NOT NULL,
  `label` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO properties VALUES(1, 'jeedev.message', 'test-dev-master', 'config-client', 'dev', 'master');
INSERT INTO properties VALUES(2, 'jeedev.message', 'test-prod-master', 'config-client', 'prod', 'master');
INSERT INTO properties VALUES(3, 'jeedev.message', 'test-prod-develop', 'config-client', 'prod', 'develop');
INSERT INTO properties VALUES(4, 'jeedev.message', 'hello-prod-master', 'hello-service', 'prod', 'master');
INSERT INTO properties VALUES(5, 'jeedev.message', 'hello-prod-develop', 'hello-service', 'prod', 'develop');