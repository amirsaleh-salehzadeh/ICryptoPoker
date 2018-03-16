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

import java.util.Set;


public class Game {

	private long id = 0;
	private int playersRemaining;
	private Player playerInBTN;
	private GameType gameType;
	private String name;
	private boolean isStarted;
	private Set<Player> players;
	private HandEntity currentHand;
	private GameStructure gameStructure;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getPlayersRemaining() {
		return playersRemaining;
	}
	public void setPlayersRemaining(int playersRemaining) {
		this.playersRemaining = playersRemaining;
	}
	
	public Player getPlayerInBTN(){
		if (playerInBTN == null) {
			playerInBTN = new Player();
		}
		return playerInBTN;
	}
	public void setPlayerInBTN(Player playerInBTN){
		this.playerInBTN = playerInBTN;
	}
	
	public GameType getGameType() {
		return gameType;
	}
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public HandEntity getCurrentHand() {
		return currentHand;
	}
	public void setCurrentHand(HandEntity currentHand) {
		this.currentHand = currentHand;
	}
	
	public GameStructure getGameStructure() {
		return gameStructure;
	}
	public void setGameStructure(GameStructure gameStructure) {
		this.gameStructure = gameStructure;
	}
}
