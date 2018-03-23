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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import game.poker.holdem.Card;

public class HandEntity {

	private long id = 0;
	private Game game;
	private BoardEntity board;
	private Set<PlayerHand> players;
	private Set<PlayerHand> allPlayers;
	private Player currentToAct;
	private BlindLevel blindLevel;
	private List<Card> cardList;
	private int pot;
	private int totalBetAmount;
	private int lastBetAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	// @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	// @JoinColumn(name="board_id")
	public BoardEntity getBoard() {
		return board;
	}

	public void setBoard(BoardEntity board) {
		this.board = board;
	}

	// @OneToMany(fetch=FetchType.EAGER,
	// mappedBy="handEntity",cascade={CascadeType.ALL},orphanRemoval=true)
	public Set<PlayerHand> getPlayers(boolean includeRemovedPlayerHands) {
		players = allPlayers;
		if (!includeRemovedPlayerHands)
			for (PlayerHand ph : allPlayers) {
				if (ph.isRemoved())
					players.remove(ph);
			}
			
		return players;
	}

	public void setPlayers(Set<PlayerHand> players) {
		this.players = players;
		this.allPlayers = players;
	}

	// @ManyToOne(fetch=FetchType.EAGER)
	// @JoinColumn(name="player_to_act_id")
	public Player getCurrentToAct() {
		return currentToAct;
	}

	public void setCurrentToAct(Player currentToAct) {
		this.currentToAct = currentToAct;
	}

	public BlindLevel getBlindLevel() {
		return blindLevel;
	}

	public void setBlindLevel(BlindLevel blindLevel) {
		this.blindLevel = blindLevel;
	}

	public List<Card> getCards() {
		return cardList;
	}

	public void setCards(List<Card> cards) {
		cardList = cards;
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public int getTotalBetAmount() {
		return totalBetAmount;
	}

	public void setTotalBetAmount(int betAmount) {
		this.totalBetAmount = betAmount;
	}

	/**
	 * In No Limit poker, the minimum bet size is twice the previous bet. Use
	 * this field to determine what that amount would be.
	 * 
	 * @return The last bet/raise amount
	 */
	public int getLastBetAmount() {
		return lastBetAmount;
	}

	public void setLastBetAmount(int lastBetAmount) {
		this.lastBetAmount = lastBetAmount;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof HandEntity)) {
			return false;
		}
		return ((HandEntity) o).getId() == this.getId();
	}

	@Override
	public int hashCode() {
		return (int) this.getId();
	}
}
