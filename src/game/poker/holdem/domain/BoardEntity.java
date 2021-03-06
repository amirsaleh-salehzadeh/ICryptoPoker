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

import java.util.ArrayList;
import java.util.List;

import game.poker.holdem.Card;

public class BoardEntity  {
	
	private long id;
	private Card flop1;
	private Card flop2;
	private Card flop3;
	private Card turn;
	private Card river;
	
	/**
	 * Get all of the cards on the board in list form.
	 * The list is ordered with flop first, then turn, then river.
	 * @return
	 */
	public List<Card> getBoardCards(){
		List<Card> cards = new ArrayList<Card>();
		if(flop1 != null){
			cards.add(flop1);
			cards.add(flop2);
			cards.add(flop3);
		}
		if(turn != null){
			cards.add(turn);
		}
		if(river != null){
			cards.add(river);
		}
		return cards;
	}
	
	public List<String> getBoardCardsString(){
		List<String> cards = new ArrayList<String>();
		if(flop1 != null){
			cards.add(flop1.toString());
			cards.add(flop2.toString());
			cards.add(flop3.toString());
		}
		if(turn != null){
			cards.add(turn.toString());
		}
		if(river != null){
			cards.add(river.toString());
		}
		return cards;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Card getFlop1() {
		return flop1;
	}
	public void setFlop1(Card flop1) {
		this.flop1 = flop1;
	}
	
	public Card getFlop2() {
		return flop2;
	}
	public void setFlop2(Card flop2) {
		this.flop2 = flop2;
	}
	
	public Card getFlop3() {
		return flop3;
	}
	public void setFlop3(Card flop3) {
		this.flop3 = flop3;
	}
	
	public Card getTurn() {
		return turn;
	}
	public void setTurn(Card turn) {
		this.turn = turn;
	}
	
	public Card getRiver() {
		return river;
	}
	public void setRiver(Card river) {
		this.river = river;
	}
}
