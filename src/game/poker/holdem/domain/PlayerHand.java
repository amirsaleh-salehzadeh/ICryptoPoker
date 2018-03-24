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

import game.poker.holdem.Card;
import game.poker.holdem.holder.Hand;

public class PlayerHand implements Comparable<PlayerHand> {

	private long id;
	private Player player;
	private long handId;
	private Card card1;
	private Card card2;
	private String card1S;
	private String card2S;
	private int betAmount;
	private int roundBetAmount;
	private int status;


	/**
	 * @return the removed
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getCard1S() {
		return card1S;
	}

	public void setCard1S(String card1s) {
		card1S = card1s;
	}

	public String getCard2S() {
		return card2S;
	}

	public void setCard2S(String card2s) {
		card2S = card2s;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the handId
	 */
	public long getHandId() {
		return handId;
	}

	/**
	 * @param handId the handId to set
	 */
	public void setHandId(long handId) {
		this.handId = handId;
	}

	public Card getCard1() {
		return card1;
	}

	public void setCard1(Card card1) {
		this.card1S = card1.toString();
		this.card1 = card1;
	}

	public Card getCard2() {
		return card2;
	}

	public void setCard2(Card card2) {
		this.card2S = card2.toString();
		this.card2 = card2;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}

	public int getRoundBetAmount() {
		return roundBetAmount;
	}

	public void setRoundBetAmount(int roundBetAmount) {
		this.roundBetAmount = roundBetAmount;
	}

	public Hand getHand() {
		if (card1 == null || card2 == null) {
			return null;
		}
		return new Hand(card1, card2);
	}

	@Override
	public int compareTo(PlayerHand o) {
		return this.getPlayer().compareTo(o.getPlayer());
	}
}
