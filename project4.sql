-- MySQL dump 10.13  Distrib 8.0.25, for macos11 (x86_64)
--
-- Host: localhost    Database: project4
-- ------------------------------------------------------
-- Server version	8.0.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Events`
--

DROP TABLE IF EXISTS `Events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Events` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `eventname` varchar(256) NOT NULL,
  `createbyuserid` int NOT NULL,
  `createdate` timestamp NOT NULL,
  `address1` varchar(512) NOT NULL,
  `address2` varchar(512) DEFAULT NULL,
  `city` varchar(256) NOT NULL,
  `state` varchar(256) NOT NULL,
  `zipcode` varchar(128) NOT NULL,
  `capacity` int NOT NULL,
  `price` double NOT NULL,
  `description` longtext,
  `start_time` timestamp NOT NULL,
  `end_time` timestamp NOT NULL,
  `image_name` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `fk_userid_idx` (`createbyuserid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Events`
--

LOCK TABLES `Events` WRITE;
/*!40000 ALTER TABLE `Events` DISABLE KEYS */;
INSERT INTO `Events` VALUES (1,'coffee workshop',1,'2021-12-03 06:46:33','38651 Greenwich Cir','','Fremont','California','94536',22,12.99,'Let\'s share your coffee experience.','2021-11-19 19:00:00','2021-11-19 22:00:00','1435a3b4-0ce7-4eb0-a2e6-c3afb67c1558'),(3,'Skating Club',1,'2021-12-03 06:47:19','38651 Greenwich Cir','','Fremont','California','94536',12,15.99,'Bring your skateboard to play with us.','2021-11-25 20:30:00','2021-11-25 22:00:00','bd148c8f-71cb-4fc3-a3f2-a3510f744d16'),(4,'Hiking',1,'2021-12-03 06:47:54','1234 Happy RD','','Pleasanton','California','94566',30,0,'','2021-11-25 17:00:00','2021-11-25 19:00:00','ef836579-f014-4396-a0c9-d2ab86d4b112'),(5,'Art of Flower Arrangement',1,'2021-11-29 06:42:08','2021 Usfca Rd','','San Francisco','California','94565',15,22.99,'Come and learn how to arrange flowers in your vase with us. You don\'t need to bring anything.','2021-12-20 22:30:00','2021-12-21 00:30:00','222d6fcb-a044-4866-8667-b0fb9256dbdc'),(6,'Paint from Life',1,'2021-12-03 06:48:42','Diablo Mountain','','Walnut Creek','California','94507',10,10,'Please bring your drawing board and other tools that you may need.','2021-12-21 17:00:00','2021-12-21 20:00:00','e488564b-d793-4c69-a124-5f597e9ddf36'),(7,'Tea Party',52,'2021-12-04 05:50:32','2022 Pleasanton Dr','','Pleasanton','California','94566',12,10,'Let\'s share our tea experience. Welcome to bring your favorite tea to share with us.','2021-12-31 18:00:00','2021-12-31 20:00:00','84be99c5-2ce7-4e61-a077-1488e39a002a');
/*!40000 ALTER TABLE `Events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `identity_providers`
--

DROP TABLE IF EXISTS `identity_providers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `identity_providers` (
  `idp_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`idp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `identity_providers`
--

LOCK TABLES `identity_providers` WRITE;
/*!40000 ALTER TABLE `identity_providers` DISABLE KEYS */;
INSERT INTO `identity_providers` VALUES (1,'google');
/*!40000 ALTER TABLE `identity_providers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `ticket_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `user_id` int NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `ticket_code` varchar(256) NOT NULL,
  `purchased_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ticket_id`),
  UNIQUE KEY `ticket_name_UNIQUE` (`ticket_code`),
  KEY `fk_userid_idx` (`user_id`),
  KEY `fk_eventid_idx` (`event_id`),
  CONSTRAINT `fk_tickets_eventid` FOREIGN KEY (`event_id`) REFERENCES `Events` (`event_id`),
  CONSTRAINT `fk_tickets_userid` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (1,3,52,15.99,'c8579b78-64e1-4369-a070-b2b26ce96066','2021-11-27 20:17:40'),(2,3,1,15.99,'9775cf3d-1bf3-4f23-856d-313fa30c21b1','2021-11-27 20:17:40'),(3,3,1,15.99,'fde5750d-f6ee-4229-b9a6-4bd35cdc883b','2021-11-27 20:17:40'),(4,1,52,12.99,'d55a297b-4346-4d3d-893d-189d4a98eac1','2021-11-27 21:50:00'),(5,1,1,12.99,'152ce88d-99f7-4cf1-b950-92734557878c','2021-11-27 21:50:00'),(6,1,1,12.99,'3247a3b4-3a6e-4931-9bf2-4ffada9c236d','2021-11-27 21:55:42'),(7,1,1,12.99,'0e9784b8-8988-4631-8572-b49f9ee4fa9a','2021-11-27 22:06:00'),(8,1,1,12.99,'4858abc5-06bc-4662-99b7-86bf446a698d','2021-11-27 22:06:00'),(9,4,52,0.00,'2ccd6527-acc1-4ce9-aed2-0b5dd469de55','2021-11-27 22:06:14'),(10,4,1,0.00,'090aedfe-051e-43c2-9ead-0b55dcd302ec','2021-11-27 22:06:14'),(11,4,1,0.00,'ad2bf271-cd55-457d-8381-75aa5c792bc9','2021-11-27 22:06:14'),(12,4,1,0.00,'84d4026c-1c64-40ab-b141-89b762c29c10','2021-11-27 22:06:26'),(13,6,52,10.00,'bd365a74-d314-4812-a7ce-e0d736e6d37c','2021-11-30 19:51:43'),(14,6,52,10.00,'ad700874-18a5-4346-830a-1f84624f2695','2021-11-30 19:51:43'),(15,6,52,10.00,'e0da115e-cf40-4719-b15f-2ea3b1d4962d','2021-11-30 19:51:43'),(16,6,52,10.00,'f5e72337-2872-4e53-aa1d-477a0250028a','2021-11-30 19:51:43');
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `idp_id` int NOT NULL,
  `sub` varchar(512) NOT NULL,
  `email` varchar(256) NOT NULL,
  `name` varchar(256) NOT NULL,
  `picture` varchar(1024) NOT NULL,
  `given_name` varchar(256) NOT NULL,
  `family_name` varchar(256) NOT NULL,
  `active` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `idp_sub` (`idp_id`,`sub`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,1,'101247760396406203680','wszjy0420@gmail.com','Jiayun Zhang','https://lh3.googleusercontent.com/a-/AOh14GgYBxJ5wCsv99Z1JCJGGenCFxTYLyDDzud1AyOvxw=s96-c','Jiayun','Zhang',1),(52,1,'104523184296400049260','jzhang230@dons.usfca.edu','Vivian','https://lh3.googleusercontent.com/a/AATXAJxxSsGfxYc4y7UTbG8NuuIHDn7fM4EXCnUt20aN=s96-c','Jiayun','Zhang',1);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_session`
--

DROP TABLE IF EXISTS `User_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_session` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `session` varchar(256) NOT NULL,
  `expiration` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`session_id`),
  UNIQUE KEY `session_UNIQUE` (`session`),
  KEY `fk_userid_idx` (`user_id`),
  CONSTRAINT `fk_userid` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_session`
--

LOCK TABLES `User_session` WRITE;
/*!40000 ALTER TABLE `User_session` DISABLE KEYS */;
INSERT INTO `User_session` VALUES (1,1,'5841b97f-6a6f-4e72-9405-3f818d20f573','2021-12-04 17:57:39',1),(2,1,'33000be5-6b79-4b35-99d2-f9cd874ff05b','2021-12-04 18:15:55',1),(3,1,'d66b3bff-58cb-4889-830b-fcc2adc7a780','2021-12-04 18:17:31',1),(4,1,'f6d8d0b5-2dbb-453d-8857-a95d287e7462','2021-12-04 18:22:52',1),(5,1,'b65f2e88-2dc2-48f5-a0df-868cedafe80b','2021-12-04 18:23:01',1),(6,1,'2537788c-6651-48ec-8b8d-8380f3b4f2c6','2021-12-04 18:25:38',1),(7,1,'6835513f-ceda-4e3b-9b8d-a78c5c32838b','2021-12-04 18:31:16',1),(8,1,'d4c609fc-5beb-445e-aad7-9b4f2ece69c8','2021-12-04 18:35:36',1),(9,1,'84d4e33b-4f9e-459b-a4f5-5bb1bb8c1053','2021-12-04 18:36:51',1),(10,1,'7c910b15-60f6-470e-a067-6bdc56a4c7bc','2021-12-05 02:53:20',1),(11,1,'c121a163-4f9f-4e1f-9bf9-88a7b837cc09','2021-12-05 02:56:28',1),(12,1,'a303d3a5-6521-468f-b2ca-b56792a18d47','2021-12-05 03:01:50',1),(13,1,'0720568a-a664-43ad-b63b-eb63869142e9','2021-12-05 03:03:14',1),(14,1,'46e3e8ea-b93a-43b6-9423-0118f8c533e3','2021-12-05 03:05:41',1),(15,1,'d814820f-6187-4849-ac5e-c3ae425122c9','2021-12-05 03:07:38',1),(16,1,'8bc8a0f6-ad1a-4365-8c23-8466549d112d','2021-12-05 05:55:18',1),(17,1,'e316488a-d9b9-4458-bd55-fa1b8ea9caf5','2021-12-05 06:03:44',1),(18,1,'b7af189b-09d9-486e-b826-d8a366e7c70b','2021-12-05 06:31:49',1),(19,1,'c8c342ac-adf8-4546-a8d8-d85f080e2320','2021-12-05 06:42:53',1),(20,1,'5ff6a74b-c2b3-44ef-b4b1-5ae8fef11da4','2021-12-05 06:53:29',1),(21,1,'afab1f58-1606-4cbf-914f-9110f7a738a8','2021-12-05 06:54:35',1),(22,1,'ffcaee33-f201-4d54-a52b-a4a57bbe6095','2021-12-05 07:04:21',1),(23,1,'2538a4fe-2720-4b83-8883-ff6d2ba9c9c3','2021-12-05 07:15:56',1),(24,1,'d2dad075-d7c0-4e73-aa7c-61776adcd4dc','2021-12-05 07:25:46',1),(25,1,'2b3f8c62-59cc-4218-8647-8c06c6989ae3','2021-12-05 07:28:28',1),(26,1,'b4a20711-15ae-48f7-bb6b-c8d5b5d504d3','2021-12-05 07:30:33',1),(27,1,'05b8c5f9-61b0-47b3-9c70-d18f8f57f708','2021-12-05 07:35:20',1),(28,1,'8a305e1a-0052-4d04-8d90-55733eaa458d','2021-12-05 16:46:33',1),(29,1,'eca167f6-2161-4f2c-b614-97c5f45bf1d1','2021-12-05 16:48:16',1),(30,1,'7f9168a0-1c1b-4d53-9b74-5fbf393f41a2','2021-12-05 16:51:13',1),(31,1,'f42d0381-55d1-41a9-992c-49b2c08c48b9','2021-12-05 16:51:41',1),(32,1,'8248f20b-fe0a-41ec-ba26-4e006950a6d2','2021-12-05 16:52:34',1),(33,1,'95d4473d-1fb3-40be-a2ca-262040243309','2021-12-05 16:54:14',1),(34,1,'2e30028f-4114-4063-b99f-9c09487ebb30','2021-12-05 16:55:59',1),(35,1,'391b5e45-7b8f-4faf-a2d1-e4717e042909','2021-12-05 16:56:44',1),(36,1,'cef529d9-fe1e-491c-a39a-0e908b59dc5f','2021-12-05 17:00:54',1),(37,1,'e7356ad2-9dd9-4b03-b267-931da145841a','2021-12-05 17:02:52',1),(38,1,'1126ac22-958d-4aa2-8c1c-d0f5ab142ecb','2021-12-05 17:04:08',1),(39,1,'3898a259-1785-4583-a0ab-6042d0e219a9','2021-12-05 17:08:19',1),(40,1,'6a7babaf-702f-452b-a49d-9e63598265da','2021-12-05 17:13:38',1),(41,1,'340a9fd9-e1aa-482c-b2fb-38db9796fa52','2021-12-05 17:14:06',1),(42,1,'1e625eaf-7587-497a-9ee8-3f74222832de','2021-12-05 17:14:13',1),(43,1,'1cf47191-c888-4176-82e3-d7ce1f5dbb0a','2021-12-05 17:16:17',1),(44,1,'95797305-6eb2-4b62-8ae1-16ff559d44dc','2021-12-06 00:14:15',1),(45,1,'346696bb-2c7d-4d26-ac75-ae6c168adbbf','2021-12-06 07:21:52',0),(46,1,'2e8aa601-0220-4f49-b51d-9c60557f7d30','2021-12-06 21:06:29',1),(47,1,'2fae42a6-cf90-4829-9200-082aa936c378','2021-12-07 20:46:57',1),(48,1,'a15c4ee5-1e8d-40ed-857e-6fa9fc33bfef','2021-12-07 20:49:31',1),(49,1,'34124f01-e3cd-4b6a-9237-a88d6ac6906b','2021-12-07 20:53:10',1),(50,1,'1f2bdc3c-4dc9-431d-9ccd-f4eaa9383d86','2021-12-10 06:33:52',0),(51,52,'20caac37-d53b-4a96-848a-996cf054b995','2021-12-13 19:27:46',0),(52,1,'7347e9c7-394d-43c2-82d2-40fdd2a589a2','2021-12-13 19:28:16',0),(53,52,'9780e9f1-2d2e-432e-8869-a728b2701ec1','2021-12-13 19:29:21',0),(54,1,'9e2be46f-5935-4b71-8912-91c5232393f2','2021-12-14 05:47:16',0),(55,52,'be5840c6-2db3-496b-bf5c-0e79675176f3','2021-12-15 19:51:24',0),(56,1,'15343348-329c-4804-9877-630fd0e3c25b','2021-12-15 22:45:10',0),(57,52,'3e54833f-a805-465e-842a-4895edc6003d','2021-12-18 07:05:59',0),(58,52,'5e744c38-f667-4ffe-ae0b-461a93fc4fe0','2021-12-19 05:39:08',0),(59,52,'203f05a1-8fb3-4b3c-92bd-b7afc02d98fd','2021-12-19 06:19:31',0),(60,52,'5ece2e09-f661-450f-bc33-e50ce1b0f39a','2021-12-19 06:21:57',0),(61,52,'0d9fb3f1-a158-4e33-b2a0-dc5db637f688','2021-12-19 06:23:47',0),(62,1,'41095d8b-7f48-467b-8410-caa5136db735','2021-12-19 06:25:23',0),(63,52,'8dbfad6c-b36c-4499-8a4d-25374c3d54ad','2021-12-19 06:26:01',0);
/*!40000 ALTER TABLE `User_session` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-03 23:24:21
