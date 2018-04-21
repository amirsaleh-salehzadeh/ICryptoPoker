-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 21, 2018 at 10:54 AM
-- Server version: 10.1.28-MariaDB
-- PHP Version: 5.6.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `icryptopoker`
--

-- --------------------------------------------------------

--
-- Table structure for table `board`
--

CREATE TABLE `board` (
  `board_id` int(11) NOT NULL,
  `flop1` varchar(25) DEFAULT NULL,
  `flop2` varchar(25) DEFAULT NULL,
  `flop3` varchar(25) DEFAULT NULL,
  `turn` varchar(25) DEFAULT NULL,
  `river` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `finance_payments`
--

CREATE TABLE `finance_payments` (
  `payment_id` bigint(255) NOT NULL,
  `creator_username` varchar(255) NOT NULL,
  `username` varchar(255) CHARACTER SET latin1 NOT NULL,
  `date_time` datetime NOT NULL,
  `status` int(5) NOT NULL,
  `amount` double NOT NULL,
  `payment_reason` varchar(25) NOT NULL,
  `bank_response` longtext NOT NULL,
  `payment_type` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `finance_payments`
--

INSERT INTO `finance_payments` (`payment_id`, `creator_username`, `username`, `date_time`, `status`, `amount`, `payment_reason`, `bank_response`, `payment_type`) VALUES
(1, 'neil', 'neil', '2018-03-28 00:00:00', 0, 1000, 'sale', 'pending', 0),
(4, 'amir', 'amir', '2018-04-11 00:00:00', 0, 999, 'commission', 'pending', 1);

-- --------------------------------------------------------

--
-- Table structure for table `finance_sales`
--

CREATE TABLE `finance_sales` (
  `sale_id` bigint(20) NOT NULL,
  `creator_username` varchar(255) DEFAULT NULL,
  `username` varchar(255) CHARACTER SET latin1 NOT NULL,
  `amount` double NOT NULL,
  `date_time` datetime DEFAULT NULL,
  `payment_method` int(5) DEFAULT NULL,
  `currency` text,
  `status` int(5) DEFAULT NULL,
  `bank_response` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `finance_sales`
--

INSERT INTO `finance_sales` (`sale_id`, `creator_username`, `username`, `amount`, `date_time`, `payment_method`, `currency`, `status`, `bank_response`) VALUES
(1, '', 'amir', 1000, '2018-03-28 00:00:00', 0, 'rand', 0, 'pending'),
(2, '', 'neil', 1000, '2018-03-28 00:00:00', 0, 'rand', 0, 'pending'),
(3, '', 'test', 1000, '2018-03-28 00:00:00', 0, 'rand', 0, 'pending'),
(4, NULL, 'neil', 999, '2018-04-08 00:00:00', 0, 'dollar', 0, 'pending'),
(5, NULL, 'neil', 123, '2018-04-08 00:00:00', 3, 'rand', 0, 'pending'),
(6, NULL, 'neil', 123, '2018-04-08 00:00:00', 3, 'rand', 0, 'pending'),
(7, NULL, 'neil', 123, '2018-04-08 00:00:00', 3, 'rand', 0, 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `game`
--

CREATE TABLE `game` (
  `game_id` bigint(255) NOT NULL,
  `players_left` int(11) NOT NULL,
  `game_type` varchar(15) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `is_started` tinyint(1) NOT NULL,
  `current_hand_id` bigint(254) DEFAULT NULL,
  `game_structure_id` bigint(255) DEFAULT NULL,
  `btn_player_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `game`
--

INSERT INTO `game` (`game_id`, `players_left`, `game_type`, `name`, `is_started`, `current_hand_id`, `game_structure_id`, `btn_player_id`) VALUES
(1, 0, 'CASH', 'Game 1', 0, NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `game_blind`
--

CREATE TABLE `game_blind` (
  `game_structure_id` int(11) NOT NULL DEFAULT '0',
  `blind` varchar(25) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `game_structure`
--

CREATE TABLE `game_structure` (
  `game_structure_id` int(11) NOT NULL,
  `current_blind_level` varchar(25) DEFAULT NULL,
  `blind_length` int(11) DEFAULT NULL,
  `current_blind_ends` datetime DEFAULT NULL,
  `pause_start_time` datetime DEFAULT NULL,
  `starting_chips` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `game_structure`
--

INSERT INTO `game_structure` (`game_structure_id`, `current_blind_level`, `blind_length`, `current_blind_ends`, `pause_start_time`, `starting_chips`) VALUES
(1, 'BLIND_10_20', 0, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `hand`
--

CREATE TABLE `hand` (
  `hand_id` int(11) NOT NULL,
  `board_id` int(11) DEFAULT NULL,
  `game_id` int(11) NOT NULL,
  `player_to_act_id` varchar(255) DEFAULT NULL,
  `blind_level` varchar(25) NOT NULL,
  `pot` int(11) DEFAULT NULL,
  `bet_amount` int(11) DEFAULT NULL,
  `total_bet_amount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hand_deck`
--

CREATE TABLE `hand_deck` (
  `hand_id` int(11) NOT NULL,
  `card` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequences`
--

CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `username` varchar(255) NOT NULL DEFAULT '',
  `game_id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `chips` int(11) DEFAULT NULL,
  `total_chips` int(255) NOT NULL DEFAULT '2000',
  `game_position` int(11) NOT NULL,
  `finished_place` int(11) DEFAULT NULL,
  `sitting_out` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL,
  `public_key` varchar(255) DEFAULT NULL,
  `registeration_date` varchar(33) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `player`
--

INSERT INTO `player` (`username`, `game_id`, `name`, `chips`, `total_chips`, `game_position`, `finished_place`, `sitting_out`, `password`, `public_key`, `registeration_date`) VALUES
('amir', 0, 'amir', 0, 2000, 0, 0, 0, '202cb962ac59075b964b07152d234b70', NULL, 'Sat Mar 17 01:37:11 CAT 2018'),
('neil', 0, 'neil', 0, 2000, 0, 0, 0, '202cb962ac59075b964b07152d234b70', NULL, 'Sat Mar 17 01:37:18 CAT 2018'),
('test', 0, 'tes', 0, 2000, 0, 0, 0, '202cb962ac59075b964b07152d234b70', NULL, 'Sat Mar 17 01:37:26 CAT 2018'),
('test2', 0, 'test2', 0, 2000, 0, 0, 0, '202cb962ac59075b964b07152d234b70', NULL, 'Sat Mar 17 01:37:36 CAT 2018'),
('test3', 0, 'someName', 0, 2000, 0, 0, 0, '202cb962ac59075b964b07152d234b70', NULL, 'Sat Mar 17 01:37:44 CAT 2018');

-- --------------------------------------------------------

--
-- Table structure for table `player_hand`
--

CREATE TABLE `player_hand` (
  `player_hand_id` int(11) NOT NULL,
  `player_id` varchar(255) NOT NULL,
  `hand_id` int(11) NOT NULL,
  `card1` varchar(25) DEFAULT NULL,
  `card2` varchar(25) DEFAULT NULL,
  `bet_amount` int(11) DEFAULT NULL,
  `round_bet_amount` int(11) DEFAULT NULL,
  `action_status` int(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `player_roles`
--

CREATE TABLE `player_roles` (
  `username` varchar(22) NOT NULL,
  `role_name` varchar(22) NOT NULL,
  `player_role_id` bigint(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `player_roles`
--

INSERT INTO `player_roles` (`username`, `role_name`, `player_role_id`) VALUES
('amir', 'Player', 1),
('neil', 'Player', 2),
('test', 'Player', 3),
('test2', 'Player', 4),
('test3', 'Player', 5);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_name` varchar(22) NOT NULL DEFAULT 'NULL_VAL',
  `comment` varchar(10000) CHARACTER SET latin1 NOT NULL,
  `category_role` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_name`, `comment`, `category_role`) VALUES
('AddUser', 'Can add a user', 'User Management'),
('EditUser', 'Can edit a user', 'User Management'),
('RemoveUser', 'Can remove a user', 'User Management'),
('RoleManagement', 'Super Admin role, to be able to add/remove/edit/view a high-level security management role.', 'TFC Administration'),
('SuperAdmin', 'Determine all access levels for TFC admins. It should be included in all use-cases.', 'TFC Administration'),
('Player', 'Normal Cash or Tournament Players', 'Players');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `board`
--
ALTER TABLE `board`
  ADD PRIMARY KEY (`board_id`);

--
-- Indexes for table `finance_payments`
--
ALTER TABLE `finance_payments`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `finance_sales`
--
ALTER TABLE `finance_sales`
  ADD PRIMARY KEY (`sale_id`),
  ADD KEY `username` (`username`);

--
-- Indexes for table `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`game_id`);

--
-- Indexes for table `game_blind`
--
ALTER TABLE `game_blind`
  ADD PRIMARY KEY (`game_structure_id`,`blind`);

--
-- Indexes for table `game_structure`
--
ALTER TABLE `game_structure`
  ADD PRIMARY KEY (`game_structure_id`);

--
-- Indexes for table `hand`
--
ALTER TABLE `hand`
  ADD PRIMARY KEY (`hand_id`);

--
-- Indexes for table `hand_deck`
--
ALTER TABLE `hand_deck`
  ADD PRIMARY KEY (`hand_id`,`card`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `player_hand`
--
ALTER TABLE `player_hand`
  ADD PRIMARY KEY (`player_hand_id`);

--
-- Indexes for table `player_roles`
--
ALTER TABLE `player_roles`
  ADD PRIMARY KEY (`player_role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `board`
--
ALTER TABLE `board`
  MODIFY `board_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `finance_payments`
--
ALTER TABLE `finance_payments`
  MODIFY `payment_id` bigint(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `finance_sales`
--
ALTER TABLE `finance_sales`
  MODIFY `sale_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `game_id` bigint(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `game_structure`
--
ALTER TABLE `game_structure`
  MODIFY `game_structure_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `hand`
--
ALTER TABLE `hand`
  MODIFY `hand_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `player_hand`
--
ALTER TABLE `player_hand`
  MODIFY `player_hand_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `player_roles`
--
ALTER TABLE `player_roles`
  MODIFY `player_role_id` bigint(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `finance_payments`
--
ALTER TABLE `finance_payments`
  ADD CONSTRAINT `finance_payments_ibfk_1` FOREIGN KEY (`username`) REFERENCES `player` (`username`);

--
-- Constraints for table `finance_sales`
--
ALTER TABLE `finance_sales`
  ADD CONSTRAINT `finance_sales_ibfk_1` FOREIGN KEY (`username`) REFERENCES `player` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
