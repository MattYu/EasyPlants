DROPTABLE IF EXISTS users;
	CREATE TABLE users (
	  user_id                        INT(32)      NOT NULL AUTO_INCREMENT,
	  user_name                      VARCHAR(50)  NOT NULL,
	  user_pass                      VARCHAR(255) NOT NULL, -- NOTE: NEVER send raw password to server. Must be one way crypto hashed by app locally first.
	  user_email                     VARCHAR(255) NOT NULL,
	  user_type                      INT(3),
	  user_sensor_count              INT(16)               DEFAULT 0, -- keep track of the user's sensor numbers
	  user_notification_on     INT(1)                DEFAULT 1, -- If 1: notifications will be forwarded to user app
	  user_deleted INT(1)  DEFAULT 0, -- If 0: user is active. If 1, user has deleted his account
	  UNIQUE INDEX user_name_unique (user_email),
	  PRIMARY KEY (user_id)
	)
  ENGINE = INNODB;

DROP TABLE IF EXISTS user_sensor_pairing;
-- Many to one association sensor to user 
	CREATE TABLE user_sensor_pairing (
	  sensor_id           INT(16) NOT NULL AUTO_INCREMENT,
	  user_id             INT(16), -- foreign key to table users
	  sensor_security_code       INT(16), -- you need to autogenerate any random 16 digits integer upon. More details later. 
  pairing_success INT(1) DEFAULT 0,
  sensor_deleted INT(1) DEFAULT 0,
	  PRIMARY KEY (sensor_id)
	)
ENGINE = INNODB;

DROP TABLE IF EXISTS sensor_data;
	CREATE TABLE sensor_data (
	data_id     INT(32) NOT NULL,
sensor_id           INT(16),

	humidity_value      INT(16),
humidity_alert  INT(1) DEFAULT 0,
data_date DATETIME,
	read_status INT(1) DEFAULT 0, -- 1 == read, 0 == unread
	  PRIMARY KEY (data_id, sensor_id)
	)
	  ENGINE = INNODB;
