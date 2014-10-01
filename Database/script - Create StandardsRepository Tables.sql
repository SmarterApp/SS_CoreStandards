/****************
** drop tables **
*****************/
DROP TABLE IF EXISTS `loader_benchmarkgrades`;
DROP TABLE IF EXISTS `loader_categories`;
DROP TABLE IF EXISTS `loader_errors`;
DROP TABLE IF EXISTS `loader_initialize`;
DROP TABLE IF EXISTS `loader_publication`;
DROP TABLE IF EXISTS `loader_socks`;
DROP TABLE IF EXISTS `loader_standard_relationship`;
DROP TABLE IF EXISTS `loader_standards`;
DROP TABLE IF EXISTS `session`;

DROP TABLE IF EXISTS `standard_relationship`;
DROP TABLE IF EXISTS `standard_grade`;
DROP TABLE IF EXISTS `standard`;
DROP TABLE IF EXISTS `standard_category`;
DROP TABLE IF EXISTS `sock_relationship`;
DROP TABLE IF EXISTS `sock_drawer`;
DROP TABLE IF EXISTS `publication`;
DROP TABLE IF EXISTS `gradelevel`;
DROP TABLE IF EXISTS `publisher`;
DROP TABLE IF EXISTS `subject`;
DROP TABLE IF EXISTS `relationshiptype`;
DROP TABLE IF EXISTS `role_map`;

DROP TABLE IF EXISTS `latencylog`;
DROP TABLE IF EXISTS `logconfig`;


/******************
** create tables **
*******************/
CREATE TABLE `session` (
    `_key` CHAR(32) NOT NULL,
    `user_id` VARCHAR(100) NOT NULL,
    `startdate` TIMESTAMP NOT NULL,
    `enddate` TIMESTAMP,
    `openam_role` VARCHAR(100),
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_initialize` (
    `_fk_sessionkey` CHAR(32) NOT NULL,
    `operation` VARCHAR(20) NOT NULL,
    `publicationkey` VARCHAR(20),
    CONSTRAINT `fk_sessionkey_linitialize` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_benchmarkgrades` (
    `benchmark` VARCHAR(150) NOT NULL,
    `grade` VARCHAR(255) NOT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lbenchmarkgrades` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_errors` (
    `severity` VARCHAR(20) DEFAULT NULL,
    `object` VARCHAR(100) DEFAULT NULL,
    `error` VARCHAR(7000) DEFAULT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lerrors` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_categories` (
    `category` VARCHAR(50) NOT NULL,
    `treelevel` INT(11) NOT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lcategories` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_publication` (
    `publishername` VARCHAR(25) NOT NULL,
    `publicationdescription` VARCHAR(255) NOT NULL,
    `subject` VARCHAR(50) NOT NULL,
    `version` INT(11) NOT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lpublication` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_socks` (
    `knowledgecategory` VARCHAR(50) NOT NULL,
    `description` VARCHAR(500) NOT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lsocks` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_standard_relationship` (
    `standard_a` VARCHAR(170) NOT NULL,
    `standard_b` VARCHAR(170) NOT NULL,
    `relationshiptype` VARCHAR(15) NOT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lstandard_relationship` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `loader_standards` (
    `level` INT(11) NOT NULL,
    `key` VARCHAR(150) NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT NOT NULL,
    `shortname` VARCHAR(100) DEFAULT NULL,
    `_fk_sessionkey` CHAR(32),
    CONSTRAINT `fk_sessionkey_lstandards` FOREIGN KEY (`_fk_sessionkey`)
        REFERENCES `session` (`_key`)
        ON DELETE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `gradelevel` (
    `_key` VARCHAR(25) NOT NULL,
    `name` VARCHAR(25) NOT NULL,
    `description` VARCHAR(200) DEFAULT NULL,
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `publisher` (
    `_key` VARCHAR(8) NOT NULL,
    `name` VARCHAR(25) NOT NULL,
    `url` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `subject` (
    `_key` VARCHAR(10) NOT NULL,
    `name` VARCHAR(50) DEFAULT NULL,
    `code` VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `publication` (
    `_key` VARCHAR(20) NOT NULL,
    `_fk_publisher` VARCHAR(8) NOT NULL,
    `_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `version` FLOAT NOT NULL,
    `iscanonical` TINYINT(1) NOT NULL DEFAULT '0',
    `description` VARCHAR(1024) DEFAULT NULL,
    `_fk_subject` VARCHAR(10) NOT NULL,
    `subjectlabel` VARCHAR(50) DEFAULT NULL,
    `url` VARCHAR(255) DEFAULT NULL,
    `status` VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (`_key`),
    CONSTRAINT `fk_publicationpublisher` FOREIGN KEY (`_fk_publisher`)
        REFERENCES `publisher` (`_key`)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT `fk_publicationsubject` FOREIGN KEY (`_fk_subject`)
        REFERENCES `subject` (`_key`)
        ON DELETE NO ACTION ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `sock_drawer` (
    `_key` VARCHAR(100) NOT NULL,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(500) DEFAULT NULL,
    `_fk_publication` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`_key`),
    CONSTRAINT `fk_kdpublication` FOREIGN KEY (`_fk_publication`)
        REFERENCES `publication` (`_key`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `sock_relationship` (
    `_fk_sock_a` VARCHAR(100) NOT NULL,
    `_fk_sock_b` VARCHAR(100) NOT NULL,
    `_fk_relationshiptype` VARCHAR(15) NOT NULL,
    CONSTRAINT `fk_kd_a` FOREIGN KEY (`_fk_sock_a`)
        REFERENCES `sock_drawer` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_kd_b` FOREIGN KEY (`_fk_sock_b`)
        REFERENCES `sock_drawer` (`_key`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `relationshiptype` (
    `_key` VARCHAR(15) NOT NULL,
    `name` VARCHAR(25) NOT NULL,
    `description` VARCHAR(200) DEFAULT NULL,
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `standard` (
    `_key` VARCHAR(170) NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `_fk_parent` VARCHAR(170) DEFAULT NULL,
    `_fk_publication` VARCHAR(20) NOT NULL,
    `description` TEXT,
    `treelevel` INT(11) DEFAULT NULL,
    `isactive` TINYINT(1) NOT NULL DEFAULT '1',
    `pubkey` VARCHAR(150) DEFAULT NULL,
    `shortname` VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (`_key`),
    CONSTRAINT `fk_standard` FOREIGN KEY (`_fk_parent`)
        REFERENCES `standard` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_standard_pub` FOREIGN KEY (`_fk_publication`)
        REFERENCES `publication` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `standard_category` (
    `name` VARCHAR(50) DEFAULT NULL,
    `treelevel` INT(11) NOT NULL,
    `_fk_publication` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`_fk_publication` , `treelevel`),
    CONSTRAINT `fk_categorypub` FOREIGN KEY (`_fk_publication`)
        REFERENCES `publication` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `standard_grade` (
    `_fk_standard` VARCHAR(170) NOT NULL,
    `_fk_gradelevel` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`_fk_standard` , `_fk_gradelevel`),
    CONSTRAINT `fk_standard_grade_gradelevel` FOREIGN KEY (`_fk_gradelevel`)
        REFERENCES `gradelevel` (`_key`)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT `fk_standard_grade_standard` FOREIGN KEY (`_fk_standard`)
        REFERENCES `standard` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `standard_relationship` (
    `_fk_standard_a` VARCHAR(170) NOT NULL,
    `_fk_standard_b` VARCHAR(170) NOT NULL,
    `_fk_relationshiptype` VARCHAR(15) NOT NULL,
    PRIMARY KEY (`_fk_standard_a` , `_fk_standard_b` , `_fk_relationshiptype`),
    CONSTRAINT `fk_stndrel_a` FOREIGN KEY (`_fk_standard_a`)
        REFERENCES `standard` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_stndrel_b` FOREIGN KEY (`_fk_standard_b`)
        REFERENCES `standard` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_reltype` FOREIGN KEY (`_fk_relationshiptype`)
        REFERENCES `relationshiptype` (`_key`)
        ON DELETE CASCADE ON UPDATE NO ACTION
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `role_map` (
    `_key` INT NOT NULL AUTO_INCREMENT,
    `openam_role` VARCHAR(100) NOT NULL,
    `cs_role` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `latencylog` (
	`_key` INT NOT NULL AUTO_INCREMENT,
	`procname` VARCHAR(200),
	`params` VARCHAR(5000),
	`startdate` DATETIME,
	`enddate` DATETIME,
    PRIMARY KEY (`_key`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;


CREATE TABLE `logconfig` (
	loglatency BIT
) ENGINE=INNODB DEFAULT CHARSET=UTF8;