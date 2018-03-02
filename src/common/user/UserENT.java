package common.user;

import game.poker.holdem.domain.Player;

import java.util.ArrayList;

import common.security.RoleENT;

public class UserENT {
	String userName;
	String password;
	String registerationDate;
	String name;
	String surName;
	String dateOfBirth;
	private Player player;
	boolean gender;
	ArrayList<RoleENT> roleENTs = new ArrayList<RoleENT>();

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserENT(String userName) {
		super();
		this.userName = userName;
	}

	public UserENT(String userName, int clientID, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public UserENT() {
	}

	public void setRoleENTs(ArrayList<RoleENT> roleENTs) {
		this.roleENTs = roleENTs;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegisterationDate() {
		return registerationDate;
	}

	public void setRegisterationDate(String registerationDate) {
		this.registerationDate = registerationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String sureName) {
		this.surName = sureName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public ArrayList<RoleENT> getRoleENTs() {
		return roleENTs;
	}

}
