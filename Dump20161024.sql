CREATE DATABASE  IF NOT EXISTS `resource_booking` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `resource_booking`;
-- MySQL dump 10.13  Distrib 5.7.14, for Win32 (AMD64)
--
-- Host: localhost    Database: resource_booking
-- ------------------------------------------------------
-- Server version	5.7.14-log

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
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bookings` (
  `booking_id` varchar(20) NOT NULL,
  `employee_id` int(5) NOT NULL,
  `resource_id` int(3) NOT NULL,
  `date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `status` varchar(15) NOT NULL,
  `participants` int(3) NOT NULL,
  `title` varchar(30) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `fk_bookings_employee` (`employee_id`),
  KEY `fk_bookings_resource` (`resource_id`),
  CONSTRAINT `fk_bookings_employee` FOREIGN KEY (`employee_id`) REFERENCES `users` (`employee_id`),
  CONSTRAINT `fk_bookings_resource` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES ('B2F1C120161024-1',10001,103,'2016-10-24','22:30:00','23:30:00','Approved',15,'Scrum Meeting','Project meeting on sprint 3-Salesforce'),('B2F1C120161025-16',10003,103,'2016-10-25','13:00:00','15:00:00','Approved',10,'Interview','Interview for Software Engineer'),('B2F1C120161026-3',10002,103,'2016-10-26','16:15:00','18:45:00','Approved',10,'Meeting','Discussion regarding upcoming Projects'),('B2F1C120161027-1',10001,103,'2016-10-27','06:45:00','11:45:00','Approved',32,'Birthday Party','My birthday party'),('B2F1C120161028-2',10001,103,'2016-10-28','14:00:00','17:00:00','Approved',3,'Meeting with HR','HR weekly meeting'),('B3F1C520161025-13',10002,101,'2016-10-25','10:45:00','14:00:00','Pending',6,'Meeting','Discussion regarding ongoing web projects'),('B3F1C520161025-18',10004,101,'2016-10-25','16:45:00','18:15:00','Approved',13,'Team Party','Quarterly Party'),('B3F1C520161025-3',10003,101,'2016-10-25','04:15:00','06:15:00','Approved',5,'Team Meeting','Project Scrum Metting'),('B3F1C520161025-6',10003,101,'2016-10-25','05:45:00','07:45:00','Rejected',5,'Trainee Meeting','Meeting with Mentor'),('B3F2C220161024-4',10004,100,'2016-10-24','23:30:00','23:45:00','Approved',5,'Presentation','Mock Presentation'),('B3F2C220161025-1',10001,100,'2016-10-25','15:00:00','18:00:00','Approved',5,'Team Meeting','Project discussion-Resource Booking'),('B3F2C220161025-17',10003,100,'2016-10-25','19:00:00','22:00:00','Approved',9,'Client Meeting','Sales Force client for new project'),('B3F2C220161025-2',10002,100,'2016-10-25','11:45:00','13:30:00','Approved',4,'Meeting','Networking in metacube'),('B3F2C220161025-4',10001,100,'2016-10-25','12:45:00','14:30:00','Pending',10,'Scrum Meeting','Project discussion'),('B3F2C220161025-5',10002,100,'2016-10-25','14:45:00','17:00:00','Pending',8,'Meeting','Interaction with mentors'),('B3F2C220161026-1',10002,100,'2016-10-26','10:15:00','11:30:00','Approved',7,'Meeting','Salesforce Team Project Meeting'),('B3F3C220161025-14',10001,102,'2016-10-25','02:00:00','06:00:00','Approved',20,'Farewell Party','Bidding a farewell to Mr. Singh'),('B3F3C220161025-15',10008,102,'2016-10-25','08:30:00','16:30:00','Approved',25,'salesforce meeting','for monthly plan'),('B3F3C220161026-2',10001,102,'2016-10-26','12:00:00','15:15:00','Approved',15,'Resource Booking Demonstration','Team Ashok Kumar Presentation on training project'),('B3F3C220161028-1',10002,102,'2016-10-28','10:45:00','12:30:00','Approved',4,'Meeting','Conference Room Booking'),('B3F3C220161028-5',10000,102,'2016-10-28','11:00:00','16:15:00','Pending',10,'Client Meeting','Japan client Meeting');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tokens` (
  `token` bigint(7) NOT NULL AUTO_INCREMENT,
  `request` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `employee_id` int(5) NOT NULL,
  PRIMARY KEY (`token`),
  KEY `fk_tokens_employee` (`employee_id`),
  CONSTRAINT `fk_tokens_employee` FOREIGN KEY (`employee_id`) REFERENCES `users` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000012 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
INSERT INTO `tokens` VALUES (1000011,'2016-10-24 16:04:36',10003);
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admins` (
  `employee_id` int(5) NOT NULL,
  `resource_id` int(3) NOT NULL,
  PRIMARY KEY (`employee_id`,`resource_id`),
  KEY `fk_admins_resource` (`resource_id`),
  CONSTRAINT `fk_admins_employee` FOREIGN KEY (`employee_id`) REFERENCES `users` (`employee_id`),
  CONSTRAINT `fk_admins_resource` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (10001,100),(10004,100),(10001,101),(10002,101),(10006,102),(10001,103),(10003,103),(10004,103);
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `employee_id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `designation` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `mobile_number` bigint(11) NOT NULL,
  `role` enum('user','res_admin','admin') NOT NULL,
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10009 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10000,'Super Admin','Senior Web Developer','resourcebooking.project@gmail.com','4d09f5764e116ea1ff75527943be258f',7821042473,'admin'),(10001,'Amit Sharma','Trainee Software Engineer','amit.sharma1@metacube.com','4d09f5764e116ea1ff75527943be258f',8384933461,'res_admin'),(10002,'Vivek Mittal','Trainee','vivek.mittal@metacube.com','4d09f5764e116ea1ff75527943be258f',9461955470,'res_admin'),(10003,'Pratap  Singh Ranwat','Trainee','pratap.ranawat@metacube.com','4d09f5764e116ea1ff75527943be258f',8561083067,'res_admin'),(10004,'Arpit Pittie','Trainee','arpit.pittie@metacube.com',NULL,8829055507,'res_admin'),(10005,'Yash Jain','Trainee','yash.jain@metacube.com','4d09f5764e116ea1ff75527943be258f',7737682201,'user'),(10006,'Rohit Singhavi','Developer','rohit.singhavi@metacube.com','4d09f5764e116ea1ff75527943be258f',7737607435,'res_admin'),(10007,'Pulkit Gupta','Back end Developer','pulkit.gupta@metacube.com','4d09f5764e116ea1ff75527943be258f',9782066788,'user'),(10008,'abhishek sharma','CEO','abhishek.sharma@metacube.com','4d09f5764e116ea1ff75527943be258f',7792933800,'user');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resources`
--

DROP TABLE IF EXISTS `resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resources` (
  `resource_id` int(3) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(30) NOT NULL,
  `type` varchar(20) NOT NULL,
  `capacity` int(3) NOT NULL,
  PRIMARY KEY (`resource_id`),
  UNIQUE KEY `resource_name` (`resource_name`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resources`
--

LOCK TABLES `resources` WRITE;
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
INSERT INTO `resources` VALUES (100,'B3F2C2','Conference',15),(101,'B3F1C5','Conference',6),(102,'B3F3C2','Conference',30),(103,'B2F1C1','Conference',40);
/*!40000 ALTER TABLE `resources` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-24 22:11:24
