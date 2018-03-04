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
package game.poker.holdem.service;

import game.poker.holdem.dao.HandDao;
import game.poker.holdem.dao.PlayerDao;
import game.poker.holdem.domain.Game;
import game.poker.holdem.domain.HandEntity;
import game.poker.holdem.domain.Player;
import game.poker.holdem.domain.PlayerHand;
import game.poker.holdem.domain.PlayerStatus;
import game.poker.holdem.util.PlayerUtil;

import java.util.Map;


public class PlayerActionServiceImpl implements PlayerActionService {

	private PlayerDao playerDao;
	private HandDao handDao;
	
	public Player getPlayerById(String playerId){
		return playerDao.findById(playerId, null);
	}
	
	public boolean fold(Player player, HandEntity hand) {
		hand = handDao.merge(hand, null);
		
		//Cannot fold out of turn
		if(!player.equals(hand.getCurrentToAct())){
			return false;
		}
		
		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		if(!PlayerUtil.removePlayerFromHand(player, hand)){
			return false;
		}
		hand.setCurrentToAct(next);
		
		handDao.save(hand, null);
		return true;
	}

	public boolean check(Player player, HandEntity hand) {
		hand = handDao.merge(hand, null);
		
		//Cannot check out of turn
		if(!player.equals(hand.getCurrentToAct())){
			return false;
		}
		
		//Checking is not currently an option
		if(getPlayerStatus(player) != PlayerStatus.ACTION_TO_CHECK){
			return false;
		}
		
		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		hand.setCurrentToAct(next);
		handDao.merge(hand, null);
		return true;
	}

	public boolean bet(Player player, HandEntity hand, int betAmount) {
		hand = handDao.merge(hand, null);
		if(!player.equals(hand.getCurrentToAct())){
			return false;
		}
		
		//Bet must meet the minimum of twice the previous bet.  Call bet amount and raise exactly that amount or more
		//Alternatively, if there is no previous bet, the first bet must be at least the big blind
		if(betAmount < hand.getLastBetAmount() || betAmount < hand.getBlindLevel().getBigBlind()){
			return false;
		}
		
		PlayerHand playerHand = null;
		for(PlayerHand ph : hand.getPlayers()){
			if(ph.getPlayer().equals(player)){
				playerHand = ph;
				break;
			}
		}
		
		int toCall = hand.getTotalBetAmount() - playerHand.getRoundBetAmount();
		int total = betAmount + toCall;
		if(total > player.getChips()){
			total = player.getChips();
			betAmount = total - toCall;
		}
		
		playerHand.setRoundBetAmount(playerHand.getRoundBetAmount() + total);
		playerHand.setBetAmount(playerHand.getBetAmount() + total);
		player.setChips(player.getChips() - total);
		hand.setPot(hand.getPot() + total);
		hand.setLastBetAmount(betAmount);
		hand.setTotalBetAmount(hand.getTotalBetAmount() + betAmount);
		
		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		hand.setCurrentToAct(next);
		handDao.merge(hand, null);
		playerDao.merge(player, null);
		return true;
	}

	public boolean call(Player player, HandEntity hand) {
		hand = handDao.merge(hand, null);
		//Cannot call out of turn
		if(!player.equals(hand.getCurrentToAct())){
			return false;
		}
		PlayerHand playerHand = null;
		for(PlayerHand ph : hand.getPlayers()){
			if(ph.getPlayer().equals(player)){
				playerHand = ph;
				break;
			}
		}
		
		int toCall = hand.getTotalBetAmount() - playerHand.getRoundBetAmount();
		toCall = Math.min(toCall, player.getChips());
		if(toCall <= 0){
			return false;
		}
		
		playerHand.setRoundBetAmount(playerHand.getRoundBetAmount() + toCall);
		playerHand.setBetAmount(playerHand.getBetAmount() + toCall);
		player.setChips(player.getChips() - toCall);
		hand.setPot(hand.getPot() + toCall);
		
		Player next = PlayerUtil.getNextPlayerToAct(hand, player);
		hand.setCurrentToAct(next);
		handDao.merge(hand, null);
		playerDao.merge(player, null);
		return true;
	}
	
	public void sitIn(Player player){
		player.setSittingOut(false);
		playerDao.save(player, null);
	}

	public PlayerStatus getPlayerStatus(Player player) {
		player = playerDao.merge(player, null);
		if(player == null){
			return PlayerStatus.ELIMINATED;
		}
		
		if(player.isSittingOut()){
			return PlayerStatus.SIT_OUT_GAME;
		}
		
		Game game = player.getGame();
		if(!game.isStarted()){
			return PlayerStatus.NOT_STARTED;
		}
		
		HandEntity hand = game.getCurrentHand();
		if(hand == null){
			return PlayerStatus.SEATING;
		}
		PlayerHand playerHand = null;
		for(PlayerHand ph : hand.getPlayers()){
			if(ph.getPlayer().equals(player)){
				playerHand = ph;
				break;
			}
		}
		
		if(!hand.getPlayers().contains(playerHand)){
			if( player.getChips() <= 0){
				return PlayerStatus.ELIMINATED;
			}
			return PlayerStatus.SIT_OUT;
		}
		
		if(hand.getCurrentToAct() == null){
			//Only one player, everyone else folded, player is the winner
			if(hand.getPlayers().size() == 1){
				return PlayerStatus.WON_HAND;
			}
			//Get the list of players who won at least some amount of chips at showdown
			Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
			if(winners != null && winners.containsKey(player)){
				//Player is contained in this collection, so the player was a winner in the hand
				return PlayerStatus.WON_HAND;
			}
			else{
				//Hand is over but player lost at showdown.
				return PlayerStatus.LOST_HAND;
			}
		}
		
		if(player.getChips() <= 0){
			return PlayerStatus.ALL_IN;
		}
		
		if(!player.equals(hand.getCurrentToAct())){
			//Small and Big Blind to be determined later?
			//Let controller handle that status
			return PlayerStatus.WAITING;
		}
		
		if(hand.getTotalBetAmount() > playerHand.getRoundBetAmount()){
			return PlayerStatus.ACTION_TO_CALL;
		}
		else if(playerHand.getRoundBetAmount() > 0){
			//We have placed a bet but now our action is check?  This means the round of betting is over
			//TODO still problem when every player checks or BB.  Need additional info to solve this
			return PlayerStatus.ACTION_TO_CHECK;
		}
		else{		
			return PlayerStatus.ACTION_TO_CHECK;
		}

	}
}
