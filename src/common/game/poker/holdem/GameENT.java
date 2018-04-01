package common.game.poker.holdem;

import java.util.Set;

import game.poker.holdem.domain.GameStructure;
import game.poker.holdem.domain.GameType;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;


public class GameENT {

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
