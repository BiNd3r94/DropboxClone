/* README: */
-- In memory databases are rejected for testing because they can never truly
-- tell us if the functionality will work on a production MySQL system, 
-- therefore we use just a DB on the same (or later different) MySQL server for testing purposes. 
-- Since we do not want to have duplicate code to create the same tables in different databases
-- we will use a gradle to expand on the database name during setup for live and test database,
-- in order to avoid having to do hackerish stuff with mysql prepared statements.

DROP DATABASE IF EXISTS `${DATABASE_NAME}`;
CREATE DATABASE `${DATABASE_NAME}` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `${DATABASE_NAME}`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: ws18-swa10
-- ------------------------------------------------------
-- Server version	5.7.15-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `file_request`
--

DROP TABLE IF EXISTS `file_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `requester_id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL UNIQUE,
  `request` varchar(255) NOT NULL,
  `target_path` varchar(255) NOT NULL,
  `fulfilled` TINYINT(1) NOT NULL DEFAULT 0,
  `open` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Requester_idx` (`requester_id`),
  CONSTRAINT `FK_Requester` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(128) NOT NULL UNIQUE,
  `username` varchar(60) NOT NULL UNIQUE,
  `password` varchar(256) NOT NULL,
  `first_name` varchar(256) NOT NULL,
  `last_name` varchar(256) NOT NULL,
  `file_space_name` varchar(256) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

SET @CURRENT_DATETIME = NOW();

INSERT INTO `user`
	(email, password, first_name, last_name, file_space_name, created_at, updated_at)
VALUES
	('test@test.de', 'test', 'Max', 'Mustermann', 'test01', @CURRENT_DATETIME, @CURRENT_DATETIME)
;

--
-- Table structure for table `file_share`
--

DROP TABLE IF EXISTS `file_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `is_directory` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Owner_idx` (`owner_id`),
  CONSTRAINT `FK_Owner` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `is_directory` tinyint(1) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER TABLE `permission` ADD UNIQUE `Permission_name_is_directory`(`name`, `is_directory`);

--
-- Dumping data for table `permission`
--

SET @CURRENT_DATETIME = NOW();

INSERT INTO `permission` (id, name, is_directory, created_at, updated_at)
VALUES 
	(1, "CREATE", 0, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(2, "READ", 0, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(3, "UPDATE", 0, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(4, "DELETE", 0, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(5, "CREATE", 1, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(6, "READ", 1, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(7, "UPDATE", 1, @CURRENT_DATETIME, @CURRENT_DATETIME), 
	(8, "DELETE", 1, @CURRENT_DATETIME, @CURRENT_DATETIME)
;

--
-- Table structure for table `file_share_permission_map`
--

DROP TABLE IF EXISTS `file_share_permission_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_share_permission_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_share_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_File_share_idx` (`file_share_id`),
  KEY `FK_Permission_idx` (`permission_id`),
  CONSTRAINT `FK_File_share` FOREIGN KEY (`file_share_id`) REFERENCES `file_share` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_share_user_map`
--

DROP TABLE IF EXISTS `file_share_user_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_share_user_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_share_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_File_share_map_idx` (`file_share_id`),
  KEY `FK_User_map_idx` (`user_id`),
  CONSTRAINT `FK_File_share_map` FOREIGN KEY (`file_share_id`) REFERENCES `file_share` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_User_map` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `auth_token`
--

DROP TABLE IF EXISTS `auth_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auth_token` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`token` varchar(256) NOT NULL UNIQUE,
	`owner_id` int(11) NOT NULL,
	`expires_at` DATETIME NOT NULL,
	`created_at` DATETIME NOT NULL,
  	`updated_at` DATETIME NOT NULL,
  	PRIMARY KEY (`id`),
  	KEY `FK_Token_Owner_idx` (`owner_id`),
    CONSTRAINT `FK_Token_Owner` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
