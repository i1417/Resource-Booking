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
INSERT INTO `bookings` VALUES ('B3F1C520161022-4',10011,101,'2016-10-22','21:00:00','23:00:00','Approved',7,'dfsd','sfgsfdg'),('B3F2C22016-10-22-1',10012,100,'2016-10-22','12:15:00','13:30:00','Approved',5,'Meeting','Scrum Meeting with thte team'),('B3F2C220161019-4',10011,100,'2016-10-19','12:00:00','12:15:00','Approved',3,'Meeting','anything'),('B3F2C220161021-1',10011,100,'2016-10-23','17:30:00','18:30:00','Approved',2,'Testing Approved','Try'),('B3F2C220161021-2',10017,100,'2016-10-21','17:00:00','18:00:00','Approved',4,'Testing Pending','My booking'),('B3F2C220161021-3',10011,100,'2016-10-22','08:30:00','11:45:00','Approved',45,'dfsd','dgsd'),('B3F2C220161022-6',10011,100,'2016-10-22','20:30:00','21:45:00','Approved',6,'df','sdfg'),('B3F2C220161026-1',10011,100,'2016-10-26','02:00:00','03:00:00','Rejected',4,'Meeting','Client'),('B3F2C220161028-1',10011,100,'2016-10-28','08:00:00','08:15:00','Approved',13,'Party','Birthday'),('Cab20161015-001',10011,101,'2016-10-15','18:15:00','18:15:00','Approved',1,'Going Home','Going Home'),('Conf20161018-001',10012,100,'2016-10-19','10:00:00','10:30:00','Approved',13,'Scrum Meeting','Team Name'),('Conf20161018-002',10012,100,'2016-10-19','11:00:00','13:00:00','Rejected',11,'Meeting','Desc'),('Conf20161020-001',10012,100,'2016-10-20','13:00:00','14:00:00','Approved',9,'Team Meeting','Team Name');
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
) ENGINE=InnoDB AUTO_INCREMENT=1000005 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
INSERT INTO `tokens` VALUES (1000001,'2016-10-22 15:02:32',10011),(1000002,'2016-10-22 15:04:04',10011),(1000003,'2016-10-22 15:07:37',10011),(1000004,'2016-10-22 15:07:48',10011);
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
INSERT INTO `admins` VALUES (10012,100),(10013,100),(10015,100),(10012,101);
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
) ENGINE=InnoDB AUTO_INCREMENT=10021 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10000,'John','Davis','john.davis@company.com','johndavis',9825136985,'user'),(10011,'Anant Sharma','Trainee','anant.sharma@company.com','93c731f1c3a84ef05cd54d044c379eaa',7788996655,'admin'),(10012,'Jack Dan','Back end Developer','jack.dan@company.com','93c731f1c3a84ef05cd54d044c379eaa',9874563211,'res_admin'),(10013,'Hemant Gupta','Tester','hemant.gupta@company.com','93c731f1c3a84ef05cd54d044c379eaa',9874563212,'res_admin'),(10014,'Himesh Kataria','Developer','himesh.kataria@company.com','93c731f1c3a84ef05cd54d044c379eaa',1234567899,'user'),(10015,'Lokesh ','Salesforce','lokesh1@company.com','93c731f1c3a84ef05cd54d044c379eaa',6655447788,'res_admin'),(10016,'Resource Booking','Software Developer','resourcebooking.project@gmail.com',NULL,7896544569,'user'),(10017,'Arpit Pittie','Trainee','arpit.pittie@metacube.com','93c731f1c3a84ef05cd54d044c379eaa',8829055507,'user'),(10018,'Kamal Meena','Tester','kamal.meena@company.com','93c731f1c3a84ef05cd54d044c379eaa',9874563211,'user'),(10020,'Arpit Pittie','Trainee','arpit.pittie@gmail.com','93c731f1c3a84ef05cd54d044c379eaa',1236547899,'user');
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
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resources`
--

LOCK TABLES `resources` WRITE;
/*!40000 ALTER TABLE `resources` DISABLE KEYS */;
INSERT INTO `resources` VALUES (100,'B3F2C2','Conference',15),(101,'B3F1C5','Conference',6);
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

-- Dump completed on 2016-10-22 20:57:13
