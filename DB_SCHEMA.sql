CREATE DATABASE  IF NOT EXISTS `heybeach`a;
USE `heybeach`;

DROP TABLE IF EXISTS hashtags;

CREATE TABLE hashtags (
  id bigint(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name_UNIQUE (`name`)
);

DROP TABLE IF EXISTS likes;

CREATE TABLE likes (
  user_id bigint(20) NOT NULL,
  picture_id bigint(20) NOT NULL,
  PRIMARY KEY (user_id,picture_id),
  KEY user_id_idx (user_id),
  KEY picture_id_idx (picture_id),
  CONSTRAINT picture_id_fk FOREIGN KEY (picture_id) REFERENCES pictures (id) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES `users` (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items (
  id bigint(20) NOT NULL,
  order_id bigint(20) NOT NULL,
  picture_id bigint(20) NOT NULL,
  title varchar(100) NOT NULL,
  price decimal(13,2) NOT NULL,
  PRIMARY KEY (id),
  KEY order_item_fk_idx (order_id),
  KEY picteure_if_oi_fk_idx (picture_id),
  CONSTRAINT order_item_fk FOREIGN KEY (order_id) REFERENCES `orders` (id) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT picteure_if_oi_fk FOREIGN KEY (picture_id) REFERENCES pictures (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id bigint(20) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  amount decimal(13,2) NOT NULL,
  billing_address varchar(150) NOT NULL,
  shipping_address varchar(150) NOT NULL,
  full_name varchar(60) NOT NULL,
  PRIMARY KEY (id),
  KEY user_id_order_fk_idx (user_id),
  CONSTRAINT user_id_order_fk FOREIGN KEY (user_id) REFERENCES `users` (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS payments;

CREATE TABLE payments (
  id bigint(20) NOT NULL,
  order_id bigint(20) NOT NULL,
  payment_method varchar(100) NOT NULL,
  result bit(1) NOT NULL,
  `transaction` varchar(100) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  error_reason varchar(45) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY order_id_idx (order_id),
  CONSTRAINT order_id FOREIGN KEY (order_id) REFERENCES `orders` (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS picture_hashtags;

CREATE TABLE picture_hashtags (
  picture_id bigint(20) NOT NULL,
  hashtag_id bigint(20) NOT NULL,
  PRIMARY KEY (hashtag_id,picture_id),
  KEY picture_id_idx (picture_id),
  CONSTRAINT hashtag_id FOREIGN KEY (hashtag_id) REFERENCES hashtags (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT picture_id FOREIGN KEY (picture_id) REFERENCES pictures (id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS picture_status_log;

CREATE TABLE picture_status_log (
  id bigint(20) NOT NULL,
  approved_by bigint(20) DEFAULT NULL,
  `comment` varchar(45) DEFAULT NULL,
  status_id bigint(20) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  picture_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY approved_by_fk2_idx (approved_by),
  KEY picture_id_fk2_idx (picture_id),
  CONSTRAINT approved_by_fk2 FOREIGN KEY (approved_by) REFERENCES `users` (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT picture_id_fk2 FOREIGN KEY (picture_id) REFERENCES pictures (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS pictures;

CREATE TABLE pictures (
  id bigint(20) NOT NULL,
  path varchar(200) NOT NULL,
  user_id bigint(20) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  picture_status bigint(20) NOT NULL,
  title varchar(100) NOT NULL,
  description varchar(400) DEFAULT NULL,
  price decimal(13,2) NOT NULL,
  PRIMARY KEY (id),
  KEY creator_id_idx (user_id),
  CONSTRAINT creator_id FOREIGN KEY (user_id) REFERENCES `users` (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
  id bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  priority int(11) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO roles VALUES (1,'ROLE_AD',3);
INSERT INTO roles VALUES (2,'ROLE_PSU',2);
INSERT INTO roles VALUES (3,'ROLE_PBU',1);
DROP TABLE IF EXISTS user_roles;

CREATE TABLE user_roles (
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  PRIMARY KEY (role_id,user_id),
  KEY role_id_idx (role_id),
  KEY user_id_idx (user_id),
  CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES `users` (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

INSERT INTO user_roles VALUES (35,1);
INSERT INTO user_roles VALUES (37,2);
INSERT INTO user_roles VALUES (36,3);
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id bigint(20) NOT NULL,
  username varchar(95) NOT NULL,
  `password` varchar(60) NOT NULL,
  email varchar(100) NOT NULL,
  firstname varchar(45) DEFAULT NULL,
  lastname varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users VALUES (35,'admin','$2a$10$9zRcpNgzIUYVQcSz49e0eO3k3DwruAy7otUYeDaOJjnPyn/m.ktBW','admin@admin.com','Admin','Admin');
INSERT INTO users VALUES (36,'buyer','$2a$10$dUCSouDJ0ZcvwbuztyWPweMGfQ6sq7d2Xe2wK662WbFxHhcjVS5we','buyer@buyer.com','Buyer','Buyer');
INSERT INTO users VALUES (37,'seller','$2a$10$wlqGKYCcWw6hLBxqlYeR2e6xDndesh2W7.p/gJ5DBqUFhObIzR5dq','seller@seller.com','Seller','Seller');