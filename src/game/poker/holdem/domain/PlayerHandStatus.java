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

/**
 * Enum type to track the player's current status in the game/hand
 * 
 * @author jacobhyphenated
 */
public class PlayerHandStatus {
	public static final int NORMAL = 0;
	public static final int CALLED = 1;
	public static final int CHECKED = 2;
	public static final int FOLDED = 3;
	public static final int RAISED = 4;
	public static final int BET = 5;
	public static final int PREFLOPEND = 6;
	public static final int FLOPEND = 7;
	public static final int TURNEND = 8;
	public static final int RIVEREND = 9;
	public static final int HANDEND = 10;
	public static final int SITOUT = 11;
}
