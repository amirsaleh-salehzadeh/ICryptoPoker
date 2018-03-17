SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `icryptopoker` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `icryptopoker`;

DROP TABLE IF EXISTS `board`;
CREATE TABLE IF NOT EXISTS `board` (
  `board_id` int(11) NOT NULL AUTO_INCREMENT,
  `flop1` varchar(25) DEFAULT NULL,
  `flop2` varchar(25) DEFAULT NULL,
  `flop3` varchar(25) DEFAULT NULL,
  `turn` varchar(25) DEFAULT NULL,
  `river` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`board_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

INSERT INTO `board` (`board_id`, `flop1`, `flop2`, `flop3`, `turn`, `river`) VALUES
(11, NULL, NULL, NULL, NULL, NULL);

DROP TABLE IF EXISTS `finance_payments`;
CREATE TABLE IF NOT EXISTS `finance_payments` (
  `payment_id` bigint(20) NOT NULL,
  `username` varchar(255) CHARACTER SET latin1 NOT NULL,
  `date_time` datetime NOT NULL,
  `status` int(5) NOT NULL,
  `amount` double NOT NULL,
  `payment_reason` varchar(25) NOT NULL,
  `bank_response` longtext NOT NULL,
  `currency` text NOT NULL,
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `finance_sales`;
CREATE TABLE IF NOT EXISTS `finance_sales` (
  `sale_id` bigint(20) NOT NULL,
  `username` varchar(255) CHARACTER SET latin1 NOT NULL,
  `amount` double NOT NULL,
  `date_time` datetime NOT NULL,
  `payment_method` int(5) NOT NULL,
  `currency` text NOT NULL,
  `status` int(5) NOT NULL,
  `bank_response` longtext NOT NULL,
  PRIMARY KEY (`sale_id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `game`;
CREATE TABLE IF NOT EXISTS `game` (
  `game_id` bigint(255) NOT NULL AUTO_INCREMENT,
  `players_left` int(11) NOT NULL,
  `game_type` varchar(15) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `is_started` tinyint(1) NOT NULL,
  `current_hand_id` bigint(254) DEFAULT NULL,
  `game_structure_id` bigint(255) DEFAULT NULL,
  `btn_player_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

INSERT INTO `game` (`game_id`, `players_left`, `game_type`, `name`, `is_started`, `current_hand_id`, `game_structure_id`, `btn_player_id`) VALUES
(1, 2, 'CASH', 'Game 1', 1, 11, 1, 'neil');

DROP TABLE IF EXISTS `game_blind`;
CREATE TABLE IF NOT EXISTS `game_blind` (
  `game_structure_id` int(11) NOT NULL DEFAULT '0',
  `blind` varchar(25) NOT NULL DEFAULT '',
  PRIMARY KEY (`game_structure_id`,`blind`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `game_structure`;
CREATE TABLE IF NOT EXISTS `game_structure` (
  `game_structure_id` int(11) NOT NULL AUTO_INCREMENT,
  `current_blind_level` varchar(25) DEFAULT NULL,
  `blind_length` int(11) DEFAULT NULL,
  `current_blind_ends` datetime DEFAULT NULL,
  `pause_start_time` datetime DEFAULT NULL,
  `starting_chips` int(11) DEFAULT NULL,
  PRIMARY KEY (`game_structure_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

INSERT INTO `game_structure` (`game_structure_id`, `current_blind_level`, `blind_length`, `current_blind_ends`, `pause_start_time`, `starting_chips`) VALUES
(1, 'BLIND_10_20', 0, NULL, NULL, 0);

DROP TABLE IF EXISTS `hand`;
CREATE TABLE IF NOT EXISTS `hand` (
  `hand_id` int(11) NOT NULL AUTO_INCREMENT,
  `board_id` int(11) DEFAULT NULL,
  `game_id` int(11) NOT NULL,
  `player_to_act_id` varchar(255) DEFAULT NULL,
  `blind_level` varchar(25) NOT NULL,
  `pot` int(11) DEFAULT NULL,
  `bet_amount` int(11) DEFAULT NULL,
  `total_bet_amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`hand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

INSERT INTO `hand` (`hand_id`, `board_id`, `game_id`, `player_to_act_id`, `blind_level`, `pot`, `bet_amount`, `total_bet_amount`) VALUES
(11, 11, 1, 'neil', 'BLIND_10_20', 30, 20, 20);

DROP TABLE IF EXISTS `hand_deck`;
CREATE TABLE IF NOT EXISTS `hand_deck` (
  `hand_id` int(11) NOT NULL,
  `card` varchar(25) NOT NULL,
  PRIMARY KEY (`hand_id`,`card`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `hibernate_sequences`;
CREATE TABLE IF NOT EXISTS `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `player`;
CREATE TABLE IF NOT EXISTS `player` (
  `username` varchar(255) NOT NULL DEFAULT '',
  `game_id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `chips` int(11) DEFAULT NULL,
  `game_position` int(11) NOT NULL,
  `finished_place` int(11) DEFAULT NULL,
  `sitting_out` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL,
  `registeration_date` varchar(33) NOT NULL,
  `session_id` varchar(255) DEFAULT NULL,
  `total_chips` int(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `player` (`username`, `game_id`, `name`, `chips`, `game_position`, `finished_place`, `sitting_out`, `password`, `registeration_date`, `session_id`, `total_chips`) VALUES
('amir', 1, 'amirN', 100, 2, 0, 0, '202cb962ac59075b964b07152d234b70', 'Sat Mar 17 01:37:11 CAT 2018', NULL, 0),
('neil', 1, 'NeilN', 100, 1, 0, 0, '202cb962ac59075b964b07152d234b70', 'Sat Mar 17 01:37:18 CAT 2018', NULL, 0),
('test', 0, 'testN', 90, 0, NULL, 0, '202cb962ac59075b964b07152d234b70', 'Sat Mar 17 01:37:26 CAT 2018', NULL, 0),
('test2', 0, 'nickName', NULL, 0, NULL, 0, '202cb962ac59075b964b07152d234b70', 'Sat Mar 17 01:37:36 CAT 2018', NULL, 0),
('test3', 0, 'someName', NULL, 0, NULL, 0, '202cb962ac59075b964b07152d234b70', 'Sat Mar 17 01:37:44 CAT 2018', NULL, 0);

DROP TABLE IF EXISTS `player_hand`;
CREATE TABLE IF NOT EXISTS `player_hand` (
  `player_hand_id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` varchar(255) NOT NULL,
  `hand_id` int(11) NOT NULL,
  `card1` varchar(25) DEFAULT NULL,
  `card2` varchar(25) DEFAULT NULL,
  `bet_amount` int(11) DEFAULT NULL,
  `round_bet_amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`player_hand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

INSERT INTO `player_hand` (`player_hand_id`, `player_id`, `hand_id`, `card1`, `card2`, `bet_amount`, `round_bet_amount`) VALUES
(18, 'test', 9, 'FOUR_OF_DIAMONDS', 'JACK_OF_DIAMONDS', 20, 20),
(19, 'neil', 9, 'NINE_OF_DIAMONDS', 'FOUR_OF_CLUBS', 10, 10),
(20, 'amir', 9, 'ACE_OF_HEARTS', 'SIX_OF_SPADES', 0, 0),
(21, 'neil', 10, 'JACK_OF_HEARTS', 'EIGHT_OF_SPADES', 0, 0),
(22, 'amir', 10, 'SIX_OF_HEARTS', 'KING_OF_HEARTS', 20, 20),
(23, 'test', 10, 'NINE_OF_SPADES', 'JACK_OF_DIAMONDS', 10, 10),
(24, 'amir', 11, 'SIX_OF_HEARTS', 'KING_OF_DIAMONDS', 20, 20),
(25, 'neil', 11, 'FIVE_OF_CLUBS', 'KING_OF_HEARTS', 10, 10);

DROP TABLE IF EXISTS `player_roles`;
CREATE TABLE IF NOT EXISTS `player_roles` (
  `username` varchar(22) NOT NULL,
  `role_name` varchar(22) NOT NULL,
  `player_role_id` bigint(255) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`player_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

INSERT INTO `player_roles` (`username`, `role_name`, `player_role_id`) VALUES
('amir', 'Player', 1),
('neil', 'Player', 2),
('test', 'Player', 3),
('test2', 'Player', 4),
('test3', 'Player', 5);

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `role_name` varchar(22) NOT NULL DEFAULT 'NULL_VAL',
  `comment` varchar(10000) CHARACTER SET latin1 NOT NULL,
  `category_role` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

INSERT INTO `roles` (`role_name`, `comment`, `category_role`) VALUES
('AddUser', 'Can add a user', 'User Management'),
('EditUser', 'Can edit a user', 'User Management'),
('RemoveUser', 'Can remove a user', 'User Management'),
('RoleManagement', 'Super Admin role, to be able to add/remove/edit/view a high-level security management role.', 'TFC Administration'),
('SuperAdmin', 'Determine all access levels for TFC admins. It should be included in all use-cases.', 'TFC Administration'),
('Player', 'Normal Cash or Tournament Players', 'Players');


ALTER TABLE `finance_payments`
  ADD CONSTRAINT `finance_payments_ibfk_1` FOREIGN KEY (`username`) REFERENCES `player` (`username`);

ALTER TABLE `finance_sales`
  ADD CONSTRAINT `finance_sales_ibfk_1` FOREIGN KEY (`username`) REFERENCES `player` (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
