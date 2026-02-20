-- --------------------------------------------------------
-- Script de creación de la base de datos del proyecto Trivial
-- Compatible con MySQL 8 (Clever Cloud)
-- Fecha: 2026
-- --------------------------------------------------------


-- Volcando estructura para tabla proyectotrivial.categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOMBRE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- Volcando datos para la tabla proyectotrivial.categorias: ~5 rows (aproximadamente)
INSERT INTO `categorias` (`ID`, `nombre`, `DESCRIPCION`) VALUES
	(1, 'Matemáticas', 'Preguntas de operaciones y lógica numérica'),
	(2, 'Geografía', 'Preguntas sobre países, capitales y lugares del mundo'),
	(3, 'Historia', 'Preguntas sobre hechos históricos y personajes importantes'),
	(4, 'Ciencia', 'Preguntas de biología, física y química'),
	(5, 'Arte', 'Preguntas sobre arte, pintura y artistas famosos');



-- Volcando estructura para tabla proyectotrivial.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `PASSWORD_HASH` varchar(255) NOT NULL,
  `nombre_us` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EMAIL` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Volcando estructura para tabla proyectotrivial.preguntas
CREATE TABLE IF NOT EXISTS `preguntas` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CATEGORIA_ID` bigint(20) NOT NULL,
  `texto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IX_PREGUNTAS_CAT` (`CATEGORIA_ID`),
  CONSTRAINT `preguntas_ibfk_1` FOREIGN KEY (`CATEGORIA_ID`) REFERENCES `categorias` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Volcando datos para la tabla proyectotrivial.preguntas: ~25 rows (aproximadamente)
INSERT INTO `preguntas` (`ID`, `CATEGORIA_ID`, `texto`) VALUES
	(1, 1, '¿Cuánto es 12 x 12?'),
	(2, 1, '¿Cuál es 2^5?'),
	(3, 1, '¿Cuál es la raíz cuadrada de 81?'),
	(4, 1, '¿Cuánto es 15 + 28?'),
	(5, 1, '¿Cuál es el resultado de 100 ÷ 4?'),
	(6, 2, '¿Cuál es la capital de Francia?'),
	(7, 2, '¿Cuál es el país más grande del mundo?'),
	(8, 2, '¿Cuál es el río más largo del mundo?'),
	(9, 2, '¿Qué país tiene forma de bota?'),
	(10, 2, '¿Cuál es la capital de Japón?'),
	(11, 3, '¿En qué año comenzó la Segunda Guerra Mundial?'),
	(12, 3, '¿Quién fue el primer presidente de Estados Unidos?'),
	(13, 3, '¿Qué imperio construyó el Coliseo?'),
	(14, 3, '¿Quién descubrió América?'),
	(15, 3, '¿En qué año cayó el Muro de Berlín?'),
	(16, 4, '¿Cuál es el planeta más grande del sistema solar?'),
	(17, 4, '¿Qué gas respiramos principalmente?'),
	(18, 4, '¿Cuál es la unidad básica de la vida?'),
	(19, 4, '¿Qué órgano bombea sangre a todo el cuerpo?'),
	(20, 4, '¿Cuál es el metal más ligero?'),
	(21, 5, '¿Quién pintó la Mona Lisa?'),
	(22, 5, '¿Qué estilo artístico se caracteriza por formas geométricas y colores vivos?'),
	(23, 5, '¿Quién pintó "La noche estrellada"?'),
	(24, 5, '¿En qué país nació Frida Kahlo?'),
	(25, 5, '¿Qué movimiento artístico es Salvador Dalí famoso por?');


-- Volcando estructura para tabla proyectotrivial.respuestas
CREATE TABLE IF NOT EXISTS `respuestas` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PREGUNTA_ID` bigint(20) NOT NULL,
  `texto` varchar(255) DEFAULT NULL,
  `ES_CORRECTA` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `PREGUNTA_ID` (`PREGUNTA_ID`),
  CONSTRAINT `respuestas_ibfk_1` FOREIGN KEY (`PREGUNTA_ID`) REFERENCES `preguntas` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Volcando datos para la tabla proyectotrivial.respuestas: ~100 rows (aproximadamente)
INSERT INTO `respuestas` (`ID`, `PREGUNTA_ID`, `texto`, `ES_CORRECTA`) VALUES
	(1, 1, '144', 1),
	(2, 1, '124', 0),
	(3, 1, '132', 0),
	(4, 1, '142', 0),
	(5, 2, '32', 1),
	(6, 2, '16', 0),
	(7, 2, '64', 0),
	(8, 2, '25', 0),
	(9, 3, '9', 1),
	(10, 3, '8', 0),
	(11, 3, '7', 0),
	(12, 3, '6', 0),
	(13, 4, '42', 0),
	(14, 4, '43', 1),
	(15, 4, '44', 0),
	(16, 4, '45', 0),
	(17, 5, '20', 0),
	(18, 5, '25', 1),
	(19, 5, '30', 0),
	(20, 5, '40', 0),
	(21, 6, 'Madrid', 0),
	(22, 6, 'París', 1),
	(23, 6, 'Berlín', 0),
	(24, 6, 'Roma', 0),
	(25, 7, 'Canadá', 0),
	(26, 7, 'China', 0),
	(27, 7, 'Estados Unidos', 0),
	(28, 7, 'Rusia', 1),
	(29, 8, 'Amazonas', 0),
	(30, 8, 'Nilo', 1),
	(31, 8, 'Yangtsé', 0),
	(32, 8, 'Misisipi', 0),
	(33, 9, 'Italia', 1),
	(34, 9, 'Grecia', 0),
	(35, 9, 'España', 0),
	(36, 9, 'Portugal', 0),
	(37, 10, 'Seúl', 0),
	(38, 10, 'Beijing', 0),
	(39, 10, 'Tokio', 1),
	(40, 10, 'Bangkok', 0),
	(41, 11, '1937', 0),
	(42, 11, '1939', 1),
	(43, 11, '1941', 0),
	(44, 11, '1945', 0),
	(45, 12, 'Abraham Lincoln', 0),
	(46, 12, 'Thomas Jefferson', 0),
	(47, 12, 'George Washington', 1),
	(48, 12, 'John Adams', 0),
	(49, 13, 'Griego', 0),
	(50, 13, 'Egipcio', 0),
	(51, 13, 'Romano', 1),
	(52, 13, 'Bizantino', 0),
	(53, 14, 'Cristóbal Colón', 1),
	(54, 14, 'Magallanes', 0),
	(55, 14, 'Vasco de Gama', 0),
	(56, 14, 'Américo Vespucio', 0),
	(57, 15, '1987', 0),
	(58, 15, '1988', 0),
	(59, 15, '1989', 1),
	(60, 15, '1990', 0),
	(61, 16, 'Júpiter', 1),
	(62, 16, 'Saturno', 0),
	(63, 16, 'Neptuno', 0),
	(64, 16, 'Marte', 0),
	(65, 17, 'Oxígeno', 1),
	(66, 17, 'Hidrógeno', 0),
	(67, 17, 'Nitrógeno', 0),
	(68, 17, 'Dióxido de carbono', 0),
	(69, 18, 'Átomo', 0),
	(70, 18, 'Molécula', 0),
	(71, 18, 'Célula', 1),
	(72, 18, 'Organelo', 0),
	(73, 19, 'Pulmón', 0),
	(74, 19, 'Hígado', 0),
	(75, 19, 'Corazón', 1),
	(76, 19, 'Riñón', 0),
	(77, 20, 'Aluminio', 0),
	(78, 20, 'Litio', 1),
	(79, 20, 'Oro', 0),
	(80, 20, 'Plata', 0),
	(81, 21, 'Miguel Ángel', 0),
	(82, 21, 'Leonardo da Vinci', 1),
	(83, 21, 'Pablo Picasso', 0),
	(84, 21, 'Vincent van Gogh', 0),
	(85, 22, 'Impresionismo', 0),
	(86, 22, 'Cubismo', 1),
	(87, 22, 'Barroco', 0),
	(88, 22, 'Romanticismo', 0),
	(89, 23, 'Claude Monet', 0),
	(90, 23, 'Vincent van Gogh', 1),
	(91, 23, 'Salvador Dalí', 0),
	(92, 23, 'Frida Kahlo', 0),
	(93, 24, 'España', 0),
	(94, 24, 'México', 1),
	(95, 24, 'Italia', 0),
	(96, 24, 'Francia', 0),
	(97, 25, 'Surrealismo', 1),
	(98, 25, 'Cubismo', 0),
	(99, 25, 'Impresionismo', 0),
	(100, 25, 'Expresionismo', 0);



-- Volcando estructura para tabla proyectotrivial.partidas
CREATE TABLE IF NOT EXISTS `partidas` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USUARIO_ID` bigint(20) NOT NULL,
  `PUNTUACION` int(11) NOT NULL,
  `aciertos` int(11) NOT NULL,
  `jugador` varchar(255) DEFAULT NULL,
  `total_preguntas` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USUARIO_ID` (`USUARIO_ID`),
  CONSTRAINT `partidas_ibfk_1` FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuarios` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Volcando estructura para tabla proyectotrivial.partida_preguntas
CREATE TABLE IF NOT EXISTS `partida_preguntas` (
  `PARTIDA_ID` bigint(20) NOT NULL,
  `PREGUNTA_ID` bigint(20) NOT NULL,
  `RESPUESTA_ID` bigint(20) DEFAULT NULL,
  `pregunta` bigint(20) NOT NULL,
  PRIMARY KEY (`PARTIDA_ID`,`PREGUNTA_ID`),
  KEY `PREGUNTA_ID` (`PREGUNTA_ID`),
  KEY `RESPUESTA_ID` (`RESPUESTA_ID`),
  CONSTRAINT `partida_preguntas_ibfk_1` FOREIGN KEY (`PARTIDA_ID`) REFERENCES `partidas` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `partida_preguntas_ibfk_2` FOREIGN KEY (`PREGUNTA_ID`) REFERENCES `preguntas` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `partida_preguntas_ibfk_3` FOREIGN KEY (`RESPUESTA_ID`) REFERENCES `respuestas` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

