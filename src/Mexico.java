
import java.util.SplittableRandom;
import java.util.Scanner;

import static java.lang.System.*;

/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 *
 */
public class Mexico {

	public static void main(String[] args) {
		new Mexico().program();
	}

	final SplittableRandom rand = new SplittableRandom();
	final Scanner sc = new Scanner(in);
	final int maxRolls = 3; // No player may exceed this
	final int startAmount = 3; // Money for a player. Select any
	final int mexico = 1000; // A value greater than any other

	void program() {
		// test(); // <----------------- UNCOMMENT to test

		int pot = 0; // What the winner will get
		Player[] players; // The players (array of Player objects)
		Player current; // Current player for round
		Player leader; // Player starting the round
		Player secPlayer = null; // Player playing after the leader
		Player thrPlayer = null; // Player playing after the second player
		int playedAmt = 0; // Amount of players who made their turn this round

		players = getPlayers();
		current = getRandomPlayer(players);
		leader = current;

		out.println("Mexico Game Started");
		statusMsg(players);

	while (players.length > 1) { // Game over when only one player left
//uncommented this for testing
		// ----- In ----------
		String cmd = getPlayerChoice(current);
		if ("r".equals(cmd)) {

			// --- Process ------

			// ---- Out --------
			roundMsg(current);
			current.fstDice = rand.nextInt(1,7);
			current.secDice = rand.nextInt(1,7);

		} else if ("n".equals(cmd)) {
			// Process
			playedAmt++; //A player finished their turn
			//a way of making n choose a different player each time within a round
			//(suboptimal since rand for loop leads to unpredictable calculation times)
			for (boolean played = true; played;){
				current = players[rand.nextInt(3)];
				if (playedAmt == 1) {//if 1 player has played
					played = current == leader;//if the chosen player is leader, then he has already played
					secPlayer = current; //choosing second player
				} else if (playedAmt == 2) {//if 2 players have played
					played = current == leader || current == secPlayer;//both the leader and the second player played
					thrPlayer = current; //choosing third player
				}
			}
		} else {
			out.println("?");
		}

		if (players.length < 3) { //removed the comment from the code and prevented it from running (spam)
		// --- Process -----

		// ----- Out --------------------
		out.println("Round done ... lost!");
		out.println("Next to roll is " + current.name);

		statusMsg(players);
		}
		}
		out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
	}

	// ---- Game logic methods --------------

	// TODO implement and test methods (one at the time)

	int indexOf(Player[] players, Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == player) {
				return i;
			}
		}
		return -1;
	}

	Player getRandomPlayer(Player[] players) {
		return players[rand.nextInt(players.length)];
	}

	// ---------- IO methods (nothing to do here) -----------------------

	Player[] getPlayers() {
		Player[] players = new Player[3];
		players[0] = new Player("Olle");
		players[1] = new Player("Fia");
		players[2] = new Player("Lisa");
		return players;
	}

	void statusMsg(Player[] players) {
		out.print("Status: ");
		for (int i = 0; i < players.length; i++) {
			out.print(players[i].name + " " + players[i].amount + " ");
		}
		out.println();
	}

	void roundMsg(Player current) {
		out.println(current.name + " got " + current.fstDice + " and " + current.secDice);
	}

	String getPlayerChoice(Player player) {
		out.print("Player is " + player.name + " > ");
		return sc.nextLine();
	}

	// Possibly useful utility during development
	String toString(Player p) {
		return p.name + ", " + p.amount + ", " + p.fstDice + ", " + p.secDice + ", " + p.nRolls;
	}

	// Class for a player
	class Player {
		String name;
		int amount; // Start amount (money)
		int fstDice; // Result of first dice
		int secDice; // Result of second dice
		int nRolls; // Current number of rolls
		public Player(String name){
			this.name = name;
			this.amount = startAmount;
			this.fstDice = rand.nextInt(1,7);
			this.secDice = rand.nextInt(1,7);
			this.nRolls = 1;
		}
	}

	/**************************************************
	 * Testing
	 *
	 * Test are logical expressions that should evaluate to true (and then be
	 * written out) No testing of IO methods Uncomment in program() to run test
	 * (only)
	 ***************************************************/
	void test() {
		// A few hard coded player to use for test
		// NOTE: Possible to debug tests from here, very efficient!
		Player[] ps = { new Player("Allah"), new Player("Jesus"), new Player("Big Chungus") };
		ps[0].fstDice = 2;
		ps[0].secDice = 6;
		ps[1].fstDice = 6;
		ps[1].secDice = 5;
		ps[2].fstDice = 1;
		ps[2].secDice = 1;

		// out.println(getScore(ps[0]) == 62);
		// out.println(getScore(ps[1]) == 65);
		// out.println(next(ps, ps[0]) == ps[1]);
		// out.println(getLoser(ps) == ps[0]);

		exit(0);
	}

}
