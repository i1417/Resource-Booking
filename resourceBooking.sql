CREATE DATABASE  IF NOT EXISTS `resource_booking` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `resource_booking`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: resource_booking
-- ------------------------------------------------------
-- Server version	5.5.23

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
INSERT INTO `bookings` VALUES ('Cab20161015-001',10011,101,'2016-10-15','18:15:00','18:15:00','Approved',1,'',''),('Conf20161018-001',10012,100,'2016-10-18','11:00:00','11:30:00','Approved',13,'',''),('Conf20161020-001',10012,100,'2016-10-20','13:00:00','14:00:00','Approved',9,'',''),('Conf20161023-001',10011,100,'2016-10-12','10:00:00','11:00:00','pending',7,'',''),('Conf20161056-001',10012,100,'2016-10-12','10:00:00','11:00:00','pending',8,'','');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cabs`
--

DROP TABLE IF EXISTS `cabs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cabs` (
  `resource_id` int(3) NOT NULL,
  `driver_name` varchar(30) NOT NULL,
  `driver_number` bigint(11) NOT NULL,
  `route` varchar(100) NOT NULL,
  `cab_number` varchar(15) NOT NULL,
  PRIMARY KEY (`cab_number`),
  UNIQUE KEY `resource_id_UNIQUE` (`resource_id`),
  CONSTRAINT `fk_cabs_resource` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cabs`
--

LOCK TABLES `cabs` WRITE;
/*!40000 ALTER TABLE `cabs` DISABLE KEYS */;
INSERT INTO `cabs` VALUES (101,'Chote Lal',7412589630,'Metacube,India Gate,Sanganer','RJ14-TA-2374');
/*!40000 ALTER TABLE `cabs` ENABLE KEYS */;
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
INSERT INTO `admins` VALUES (10011,100),(10011,101);
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
  `email` varchar(30) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `mobile_number` bigint(11) NOT NULL,
  `role` enum('user','res_admin','admin') NOT NULL,
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10013 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10000,'John','Davis','john.davis@company.com','johndavis',9825136985,'user'),(10011,'Anant Sharma','Web Developer','anant.sharma@company.com','93c731f1c3a84ef05cd54d044c379eaa',7788996655,'admin'),(10012,'Jack Daneils','Back end Developer','jack.daneils@company.com','93c731f1c3a84ef05cd54d044c379eaa',9874563211,'res_admin');
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
INSERT INTO `resources` VALUES (100,'B3F2C2','Conference',15),(101,'Cab-01','Cabs',6);
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

-- Dump completed on 2016-10-15 15:07:56
