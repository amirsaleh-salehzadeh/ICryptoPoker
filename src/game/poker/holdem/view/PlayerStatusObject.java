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
package game.poker.holdem.view;

import game.poker.holdem.domain.PlayerStatus;

/**
 * Data Transfer Object Used specifically to store the information relevant to a
 * Player Status API Request.
 * 
 * @author jacobhyphenated
 */
public class PlayerStatusObject {

	private PlayerStatus status;
	private int chips;
	private int smallBlind;
	private int bigBlind;
	private String card1 = "";
	private String card2 = "";
	private int amountBetRound;
	private int amountToCall;
	private String id;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PlayerStatusObject)) {
			return false;
		}
		PlayerStatusObject p = (PlayerStatusObject) o;
		return this.status == p.getStatus() && this.chips == p.getChips()
				&& this.card1.equals(p.getCard1())
				&& this.card2.equals(p.getCard2());
	}

	@Override
	public int hashCode() {
		int res = 0;
		if(this.card1.equalsIgnoreCase(""))
			res = status.hashCode() + this.chips + this.id.hashCode();
		else
			res = status.hashCode() + this.chips + this.card1.hashCode()
					+ this.card2.hashCode() + this.id.hashCode();
		return res;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public int getChips() {
		return chips;
	}

	public void setChips(int chips) {
		this.chips = chips;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(int bigBlind) {
		this.bigBlind = bigBlind;
	}

	public String getCard1() {
		return card1;
	}

	public void setCard1(String card1) {
		this.card1 = card1;
	}

	public String getCard2() {
		return card2;
	}

	public void setCard2(String card2) {
		this.card2 = card2;
	}

	public int getAmountBetRound() {
		return amountBetRound;
	}

	public void setAmountBetRound(int amountBetRound) {
		this.amountBetRound = amountBetRound;
	}

	public int getAmountToCall() {
		return amountToCall;
	}

	public void setAmountToCall(int amountToCall) {
		this.amountToCall = amountToCall;
	}
}
