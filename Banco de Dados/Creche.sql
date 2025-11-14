-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: Creche
-- ------------------------------------------------------
-- Server version	8.4.6-0ubuntu0.25.04.3

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
-- Table structure for table `Aluno`
--

DROP TABLE IF EXISTS `Aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Aluno` (
  `id_aluno` int NOT NULL AUTO_INCREMENT,
  `id_responsavel` int NOT NULL,
  `nome` varchar(40) DEFAULT NULL,
  `idade` int NOT NULL,
  `cpf` varchar(15) NOT NULL,
  `necessidade_especial` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id_aluno`),
  KEY `id_responsavel` (`id_responsavel`),
  CONSTRAINT `Aluno_ibfk_1` FOREIGN KEY (`id_responsavel`) REFERENCES `Responsavel` (`id_responsavel`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aluno`
--

LOCK TABLES `Aluno` WRITE;
/*!40000 ALTER TABLE `Aluno` DISABLE KEYS */;
INSERT INTO `Aluno` VALUES (1,1,'zazaaa',2,'324234',''),(7,7,'Zepekeno',2,'5684045896','Doenca'),(8,1,'Sasha',3,'4533464678',''),(9,1,'zazaaa',2,'324234',''),(10,8,'Erwin Smith',4,'123-986544','Sindrome do protagonista'),(11,9,'Armin Arlet',4,'0976574532','Alergico a frutos do mar'),(12,10,'Erwin Smith',3,'3468901208945','Sindrome do protagonista'),(13,10,'Mikasa',2,'3432486757845','Perfeita');
/*!40000 ALTER TABLE `Aluno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Funcionario`
--

DROP TABLE IF EXISTS `Funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Funcionario` (
  `id_funcionario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(40) DEFAULT NULL,
  `idade` int NOT NULL,
  `cpf` varchar(15) NOT NULL,
  `cargo` enum('Professor','Cuidador','Coordenador') NOT NULL,
  `vinculo` varchar(20) NOT NULL,
  `turno` varchar(15) DEFAULT NULL,
  `setor_responsavel` varchar(20) DEFAULT NULL,
  `faixa_etaria_atendida` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Funcionario`
--

LOCK TABLES `Funcionario` WRITE;
/*!40000 ALTER TABLE `Funcionario` DISABLE KEYS */;
INSERT INTO `Funcionario` VALUES (1,'Ze pereira',34,'234255667','Professor','CLT','Manha',NULL,NULL),(2,'Lima',24,'89087097676','Professor','clt','manha',NULL,NULL),(3,'Antoniel',46,'989765534','Cuidador','CNPJ',NULL,NULL,'2-3'),(4,'Jean',34,'807637254','Coordenador','CNPJ',NULL,'Maternal',NULL),(5,'Hange',25,'140','Coordenador','CLT',NULL,'Setor tatakae',NULL),(6,'Sasha Brown ',23,'089896534e21034','Cuidador','CLT',NULL,NULL,'3-5');
/*!40000 ALTER TABLE `Funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Irmaos`
--

DROP TABLE IF EXISTS `Irmaos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Irmaos` (
  `id_irmao1` int NOT NULL,
  `id_irmao2` int NOT NULL,
  PRIMARY KEY (`id_irmao1`,`id_irmao2`),
  KEY `id_irmao2` (`id_irmao2`),
  CONSTRAINT `Irmaos_ibfk_1` FOREIGN KEY (`id_irmao1`) REFERENCES `Aluno` (`id_aluno`) ON DELETE CASCADE,
  CONSTRAINT `Irmaos_ibfk_2` FOREIGN KEY (`id_irmao2`) REFERENCES `Aluno` (`id_aluno`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Irmaos`
--

LOCK TABLES `Irmaos` WRITE;
/*!40000 ALTER TABLE `Irmaos` DISABLE KEYS */;
INSERT INTO `Irmaos` VALUES (7,8),(8,10),(12,13);
/*!40000 ALTER TABLE `Irmaos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Matricula`
--

DROP TABLE IF EXISTS `Matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Matricula` (
  `id_matricula` int NOT NULL AUTO_INCREMENT,
  `pre_matricula` tinyint(1) DEFAULT NULL,
  `id_funcionario` int DEFAULT NULL,
  `id_aluno` int NOT NULL,
  `id_turma` int DEFAULT NULL,
  `observacoes` varchar(150) DEFAULT NULL,
  `endereco` varchar(150) DEFAULT NULL,
  `data_matricula` date DEFAULT NULL,
  `situacao` varchar(20) DEFAULT 'PENDENTE',
  PRIMARY KEY (`id_matricula`),
  KEY `id_aluno` (`id_aluno`),
  KEY `id_turma` (`id_turma`),
  KEY `id_funcionario` (`id_funcionario`),
  CONSTRAINT `Matricula_ibfk_1` FOREIGN KEY (`id_aluno`) REFERENCES `Aluno` (`id_aluno`),
  CONSTRAINT `Matricula_ibfk_3` FOREIGN KEY (`id_turma`) REFERENCES `Turma` (`id_turma`),
  CONSTRAINT `Matricula_ibfk_4` FOREIGN KEY (`id_funcionario`) REFERENCES `Funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matricula`
--

LOCK TABLES `Matricula` WRITE;
/*!40000 ALTER TABLE `Matricula` DISABLE KEYS */;
INSERT INTO `Matricula` VALUES (1,0,4,8,1,'Esfomeada','na casa do teu pai','2025-11-10','ATIVA'),(2,1,NULL,9,NULL,'Visita nao realizada','Marley','2025-11-10','PENDENTE'),(3,1,NULL,9,NULL,'Visita domiciliar nao realizada','marley','2025-11-10','PENDENTE'),(4,1,NULL,7,NULL,'visita nao realizada ainda','coroadinho','2025-11-10','PENDENTE'),(5,1,NULL,8,NULL,'','sadasdas','2025-11-10','PENDENTE'),(6,1,NULL,10,NULL,'Sla','av. tal rua: n sei oque','2025-11-11','PENDENTE'),(7,1,NULL,9,NULL,'','sla ksksksks','2025-11-11','PENDENTE'),(8,1,NULL,8,NULL,'','','2025-11-11','PENDENTE'),(9,1,NULL,8,NULL,'','aaaaa','2025-11-11','PENDENTE'),(10,0,6,8,1,'','sdaff','2025-11-11','ATIVA'),(11,0,3,10,4,'Precisa ver se é pobre mesmo','Coroadinho','2025-11-12','ATIVA'),(12,1,NULL,11,NULL,'Analisar parentesco','Paradis','2025-11-14','PENDENTE'),(13,1,NULL,12,NULL,'Pai preso','Marley','2025-11-14','PENDENTE'),(14,0,6,13,1,'Casal suspeito','Cidade do subterraneo de paradis','2025-11-14','ATIVA'),(15,0,6,11,3,'','Renascença','2025-11-14','PENDENTE'),(16,0,6,7,1,'','ffgvfbv','2025-11-14','PENDENTE'),(17,0,6,10,3,'Sindrome do protagonista','sla kkkk','2025-11-14','PENDENTE');
/*!40000 ALTER TABLE `Matricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Matricula_Responsavel`
--

DROP TABLE IF EXISTS `Matricula_Responsavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Matricula_Responsavel` (
  `id_matricula` int NOT NULL,
  `id_responsavel` int NOT NULL,
  PRIMARY KEY (`id_matricula`,`id_responsavel`),
  KEY `id_responsavel` (`id_responsavel`),
  CONSTRAINT `Matricula_Responsavel_ibfk_1` FOREIGN KEY (`id_matricula`) REFERENCES `Matricula` (`id_matricula`) ON DELETE CASCADE,
  CONSTRAINT `Matricula_Responsavel_ibfk_2` FOREIGN KEY (`id_responsavel`) REFERENCES `Responsavel` (`id_responsavel`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matricula_Responsavel`
--

LOCK TABLES `Matricula_Responsavel` WRITE;
/*!40000 ALTER TABLE `Matricula_Responsavel` DISABLE KEYS */;
INSERT INTO `Matricula_Responsavel` VALUES (1,1),(1,7),(1,8),(1,9),(1,10);
/*!40000 ALTER TABLE `Matricula_Responsavel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Responsavel`
--

DROP TABLE IF EXISTS `Responsavel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Responsavel` (
  `id_responsavel` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(40) DEFAULT NULL,
  `idade` int NOT NULL,
  `cpf` varchar(15) NOT NULL,
  `telefone` varchar(20) NOT NULL,
  `parentesco` varchar(20) NOT NULL,
  PRIMARY KEY (`id_responsavel`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Responsavel`
--

LOCK TABLES `Responsavel` WRITE;
/*!40000 ALTER TABLE `Responsavel` DISABLE KEYS */;
INSERT INTO `Responsavel` VALUES (1,'Xavier',23,'7346227364','5345543','Pai'),(7,'Jubileu ',68,'834278327497','40028922','Avô'),(8,'Levi Ackerman',34,'8907896432','(98) 981853648','Pai'),(9,'エレンイエーガー',23,'08970374057','08723467823','戦え！'),(10,'Annie Leonhart',45,'097785323234945','(45)9247-9874','Mãe');
/*!40000 ALTER TABLE `Responsavel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SituacaoMatricula`
--

DROP TABLE IF EXISTS `SituacaoMatricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SituacaoMatricula` (
  `status` enum('ATIVA','INATIVA','PENDENTE','CONCLUÍDA') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SituacaoMatricula`
--

LOCK TABLES `SituacaoMatricula` WRITE;
/*!40000 ALTER TABLE `SituacaoMatricula` DISABLE KEYS */;
/*!40000 ALTER TABLE `SituacaoMatricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Turma`
--

DROP TABLE IF EXISTS `Turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Turma` (
  `id_turma` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `faixa_etaria` varchar(20) DEFAULT NULL,
  `turno` varchar(20) DEFAULT NULL,
  `tipo_turma` enum('Creche','Infantil','Pre') NOT NULL,
  `hora_cochilo` varchar(20) DEFAULT NULL,
  `aulas_psicomotricidade` varchar(50) DEFAULT NULL,
  `aulas_alfabetizacao` varchar(50) DEFAULT NULL,
  `id_professor` int DEFAULT NULL,
  PRIMARY KEY (`id_turma`),
  KEY `fk_turma_professor` (`id_professor`),
  CONSTRAINT `fk_turma_professor` FOREIGN KEY (`id_professor`) REFERENCES `Funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Turma`
--

LOCK TABLES `Turma` WRITE;
/*!40000 ALTER TABLE `Turma` DISABLE KEYS */;
INSERT INTO `Turma` VALUES (1,'Creche 1','2-3 anos','Manhã','Creche','11:00',NULL,NULL,NULL),(2,'Infantil 1','4-5 anos','manha','Infantil',NULL,'09:00',NULL,2),(3,'Infantil 2','4-5 anos','Manha','Infantil',NULL,'10:00',NULL,2),(4,'Pre','6 anos','Noturno','Pre',NULL,NULL,'19:00',1),(5,'Pré Alfa','6 anos','Manhã','Pre',NULL,NULL,'12:00',2);
/*!40000 ALTER TABLE `Turma` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-14 18:38:47
