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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

INSERT INTO `game` (`game_id`, `players_left`, `game_type`, `name`, `is_started`, `current_hand_id`, `game_structure_id`, `btn_player_id`) VALUES
(1, 0, 'TOURNAMENT', 'name', 1, NULL, 5, NULL),
(2, 0, 'TOURNAMENT', 'naaame', 0, 0, 4, NULL),
(12, 0, 'CASH', 'naaameTest', 0, 0, 3, NULL),
(13, 2, 'CASH', 'asdf', 0, 15, 6, 'amir'),
(14, 0, 'CASH', 'hellllo', 0, 0, 7, NULL),
(15, 0, 'CASH', 'amir', 0, 0, 8, NULL),
(16, 0, 'CASH', 'test', 0, NULL, 9, NULL),
(17, 0, 'CASH', 'mas', 0, 0, 10, NULL),
(18, 0, 'CASH', 'mas', 0, 0, 11, NULL),
(19, 1, 'CASH', 'massadfasdf', 0, 0, 12, NULL),
(20, 0, 'CASH', 'zxcvzcv', 0, NULL, 13, NULL),
(21, 0, 'CASH', 'xzcvzcvx', 0, NULL, 14, NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

INSERT INTO `game_structure` (`game_structure_id`, `current_blind_level`, `blind_length`, `current_blind_ends`, `pause_start_time`, `starting_chips`) VALUES
(3, 'BLIND_15_30', 1, NULL, NULL, 15),
(4, 'BLIND_400_800', 1, NULL, NULL, 400),
(5, 'BLIND_3000_6000', 1, NULL, NULL, 3000),
(6, 'BLIND_10_20', 0, NULL, NULL, 0),
(7, 'BLIND_700_1400', 0, NULL, NULL, 0),
(8, 'BLIND_10_20', 0, NULL, NULL, 0),
(9, 'BLIND_2500_5000', 0, NULL, NULL, 0),
(10, 'BLIND_600_1200', 0, NULL, NULL, 0),
(11, 'BLIND_600_1200', 0, NULL, NULL, 0),
(12, 'BLIND_9000_18K', 0, NULL, NULL, 0),
(13, 'BLIND_9000_18K', 0, NULL, NULL, 0),
(14, 'BLIND_12K_24K', 0, NULL, NULL, 0);

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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

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
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `player` (`username`, `game_id`, `name`, `chips`, `game_position`, `finished_place`, `sitting_out`, `password`, `registeration_date`) VALUES
('amir', 13, 'amir', 100, 1, 0, 0, 'asd', 'now'),
('amirS', 13, NULL, 100, 2, 0, 0, '1', 'Thu Mar 08 05:36:02 CAT 2018');

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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

INSERT INTO `player_hand` (`player_hand_id`, `player_id`, `hand_id`, `card1`, `card2`, `bet_amount`, `round_bet_amount`) VALUES
(9, 'amirS', 14, 'THREE_OF_CLUBS', 'SIX_OF_HEARTS', 20, 20),
(10, 'amir', 14, 'JACK_OF_CLUBS', 'SIX_OF_CLUBS', 10, 10),
(11, 'amirS', 15, 'SIX_OF_SPADES', 'EIGHT_OF_SPADES', 20, 20),
(12, 'amir', 15, 'QUEEN_OF_SPADES', 'FOUR_OF_DIAMONDS', 10, 10);

DROP TABLE IF EXISTS `player_roles`;
CREATE TABLE IF NOT EXISTS `player_roles` (
  `username` varchar(22) NOT NULL,
  `role_name` varchar(22) NOT NULL,
  `player_role_id` bigint(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

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
('SuperAdmin', 'Determine all access levels for TFC admins. It should be included in all use-cases.', 'TFC Administration');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
