/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package game.poker.holdem.domain;

public class Player implements Comparable<Player> {

	private String id;
	private long gameId;
	private String name;
	private int chips;
	private int gamePosition;
	private int finishPosition;
	private int totalChips = 0;
	private boolean sittingOut;
	private String registrationDate;
	private String password;
    private String sessionId ;
	// private int gender ;
	// private Date dob ;
	// private String surname ;

	/**
	 * @return the totalChips
	 */
	public int getTotalChips() {
		return totalChips;
	}

	/**
	 * @param totalChips
	 *            the totalChips to set
	 */
	public void setTotalChips(int totalChips) {
		this.totalChips = totalChips;
	}

	// public String getSurname() {
	// return surname;
	// }
	// public void setSurname(String surname) {
	// this.surname = surname;
	// }
	// public Date getDob() {
	// return dob;
	// }
	// public void setDob(Date dob) {
	// this.dob = dob;
	// }
	// public int getGender() {
	// return gender;
	// }
	// public void setGender(int gender) {
	// this.gender = gender;
	// }
	/**
	 * @return the registrationDate
	 */
	public String getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate
	 *            the registrationDate to set
	 */
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the gameId
	 */
	public long getGameId() {
		return gameId;
	}

	/**
	 * @param gameId
	 *            the gameId to set
	 */
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getChips() {
		return chips;
	}

	public void setChips(int chips) {
		this.chips = chips;
	}

	public int getGamePosition() {
		return gamePosition;
	}

	public void setGamePosition(int gamePosition) {
		this.gamePosition = gamePosition;
	}

	public int getFinishPosition() {
		return finishPosition;
	}

	public void setFinishPosition(int finishPosition) {
		this.finishPosition = finishPosition;
	}

	public boolean isSittingOut() {
		return sittingOut;
	}

	public void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Player)) {
			return false;
		}
		Player p = (Player) o;
		if (this.getId() == null) {
			return this.getName().equals(p.getName());
		}
		return this.getId().equals(p.getId());
	}

	@Override
	public int hashCode() {
		if (id == null) {
			return name.hashCode();
		}
		return id.hashCode();
	}

	@Override
	public int compareTo(Player p) {
		return this.getGamePosition() - p.getGamePosition();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
