-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 02, 2018 at 05:23 PM
-- Server version: 10.1.16-MariaDB
-- PHP Version: 7.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
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
(1, 3, 'TOURNAMENT', 'name', 1, NULL, NULL, NULL);

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
  `game_position` int(11) NOT NULL,
  `finished_place` int(11) DEFAULT NULL,
  `sitting_out` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL,
  `gender` tinyint(4) NOT NULL,
  `dob` date NOT NULL,
  `surname` varchar(255) NOT NULL,
  `registeration_date` varchar(33) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `player`
--

INSERT INTO `player` (`username`, `game_id`, `name`, `chips`, `game_position`, `finished_place`, `sitting_out`, `password`, `gender`, `dob`, `surname`, `registeration_date`) VALUES
('amir', 0, 'amir', NULL, 0, NULL, 0, 'asd', 1, '2018-02-28', 'saleh', 'now');

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
  `round_bet_amount` int(11) DEFAULT NULL
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
('SuperAdmin', 'Determine all access levels for TFC admins. It should be included in all use-cases.', 'TFC Administration');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `board`
--
ALTER TABLE `board`
  ADD PRIMARY KEY (`board_id`);

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
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `board`
--
ALTER TABLE `board`
  MODIFY `board_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `game_id` bigint(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `game_structure`
--
ALTER TABLE `game_structure`
  MODIFY `game_structure_id` int(11) NOT NULL AUTO_INCREMENT;
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
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
