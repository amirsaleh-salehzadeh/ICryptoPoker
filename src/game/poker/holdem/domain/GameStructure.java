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

import java.util.Date;
import java.util.List;

public class GameStructure {

	private long id;
	private BlindLevel currentBlindLevel;
	private int blindLength;
	private Date currentBlindEndTime;
	private Date puaseStartTime;
	private List<BlindLevel> blindLevels;
	private int startingChips;

	
	/**
	 * @return the currentBlindEndTime
	 */
	public Date getCurrentBlindEndTime() {
		return currentBlindEndTime;
	}

	/**
	 * @param currentBlindEndTime the currentBlindEndTime to set
	 */
	public void setCurrentBlindEndTime(Date currentBlindEndTime) {
		this.currentBlindEndTime = currentBlindEndTime;
	}

	/**
	 * @return the puaseStartTime
	 */
	public Date getPuaseStartTime() {
		return puaseStartTime;
	}

	/**
	 * @param puaseStartTime the puaseStartTime to set
	 */
	public void setPuaseStartTime(Date puaseStartTime) {
		this.puaseStartTime = puaseStartTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BlindLevel getCurrentBlindLevel() {
		return currentBlindLevel;
	}

	public void setCurrentBlindLevel(BlindLevel currentBlindLevel) {
		this.currentBlindLevel = currentBlindLevel;
	}

	public int getBlindLength() {
		return blindLength;
	}

	public void setBlindLength(int blindLength) {
		this.blindLength = blindLength;
	}

	public int getStartingChips() {
		return startingChips;
	}

	public void setStartingChips(int startingChips) {
		this.startingChips = startingChips;
	}

	public List<BlindLevel> getBlindLevels() {
		return blindLevels;
	}

	public void setBlindLevels(List<BlindLevel> blindLevels) {
		this.blindLevels = blindLevels;
	}
}
