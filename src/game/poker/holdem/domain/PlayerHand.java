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

import java.io.Serializable;

import game.poker.holdem.Card;
import game.poker.holdem.holder.Hand;

public class PlayerHand implements Comparable<PlayerHand>, Serializable{

	private static final long serialVersionUID = -5499451283824674842L;
	private long id;
	private Player player;
	private HandEntity handEntity;
	private Card card1;
	private Card card2;
	private int betAmount;
	private int roundBetAmount;
	
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
	
	public HandEntity getHandEntity() {
		return handEntity;
	}
	public void setHandEntity(HandEntity hand) {
		this.handEntity = hand;
	}
	
	protected Card getCard1() {
		return card1;
	}
	public void setCard1(Card card1) {
		this.card1 = card1;
	}
	
	protected Card getCard2() {
		return card2;
	}
	public void setCard2(Card card2) {
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
	
	public Hand getHand(){
		if(card1 == null || card2 == null){
			return null;
		}
		return new Hand(card1, card2);
	}
	
	@Override
	public int compareTo(PlayerHand o) {
		return this.getPlayer().compareTo(o.getPlayer());
	}
}
