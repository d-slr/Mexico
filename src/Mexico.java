
import java.util.SplittableRandom;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
		// test(); // <----------------- UNCOMMENT to testa
		int pot = 0; // What the winner will get
		Player current; // Current player for round
		Player leader; // Player starting the round
		int playedAmt = 0; // Amount of players who made their turn this round
		boolean roundDone = false;

		Player[] players = shufflePlayers(getPlayers(askNumberOfPlayers)); // The players (array of Player objects)
		current = players[playedAmt];
		leader = current; // uhsssssshshhhhh

		out.println("Mexico Game Started");
		statusMsg(players);

	while (players.length > 1) { // Game over when only one player left
//uncommented this for testing
		// ----- In ----------
		String cmd = getPlayerChoice(current);
		if ("r".equals(cmd)) {

			// --- Process ------

			// ---- Out --------
			current.fstDice = rand.nextInt(1,7);
			current.secDice = rand.nextInt(1,7);
			roundMsg(current);

		} else if ("n".equals(cmd)) {
			playedAmt++;
			current = players[playedAmt];
		} else {
			out.println("?");
		}

		if (roundDone) { //aughhhhhh
			roundDone = false;
			playedAmt = 0;
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

	Player[] shufflePlayers(Player[] players) {
		List<Player> playerList = new ArrayList<>(Arrays.asList(players));
		Collections.shuffle(playerList);
		return playerList.toArray(new Player[0]);
	}

	// ---------- IO methods (nothing to do here) -----------------------

	//returns the number of players
	int askNumberOfPlayers() {
		int answer;
		while (true) {
			out.println("How many players? > ");
			try {
				answer = sc.nextInt();
				if (answer < 0) {
					break;
				}
			} catch (Exception e) {
				out.println("Enter a valid int!!!");
			}
		}
		return answer;
	}


	//asks the name for every player, return a array of all the players
	Player[] getPlayers(int numPs) {
		Player[] players = new Player[numPs];
		for (int i = 0; i < numPs; i++) {
			String name;
			while (true) {
				out.println("Give the name for player " + i + "> ");
				name = sc.nextLine();
				if ((name == null || name.isEmpty() || name.trim().isEmpty())) {
					break;
				}
				out.println("Enter a valid non-empty name!!! ");

			}
			players[i] = new Player(name);
		}
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
