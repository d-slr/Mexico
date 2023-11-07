
import java.util.*;

import java.lang.Math;

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
	final int startAmount = 3; // Money for a player. Select any
	final int mexico = 1000; // A value greater than any other

	void program() {
		// test(); // <----------------- UNCOMMENT to testa
		int pot = 0; // What the winner will get
		Player current; // Current player for round
		Player leader = null; // Player starting the round
		int playedAmt = 0; // Amount of players who made their turn this round
		boolean roundDone = false;
		int maxRolls = 3;


		Player[] players = shufflePlayers(getPlayers(askNumberOfPlayers())); // The players (array of Player objects)
		current = players[playedAmt];

		out.println("Mexico Game Started");
		statusMsg(players);

		while (players.length > 1) { // Game over when only one player left

			if (playedAmt == 0) {
				leader = current;
			}

			String cmd = getPlayerChoice(current);

			if ((current.nRolls == maxRolls && (playedAmt + 1) == players.length) ||
				((playedAmt + 1) == players.length && cmd.equals("n") && current.nRolls > 0)) {
				endRound();
			}

			cmd = verifyCmd(cmd, playedAmt, players, current, maxRolls);

			if ("r".equals(cmd)) {

					rollDice(current);

			} else if ("n".equals(cmd)) {

				playedAmt++;
				maxRolls = leader.nRolls;
				current = next(players, playedAmt);

			} else {

				out.println("life goes onandonandon");

			}

			if (allRolled(players, playedAmt)) {
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
	void endRound() {
		out.println("Round ended");
	}
	Player next (Player[] players, int playedAmt) {

		return players[playedAmt];

	}
	String verifyCmd(String cmd, int playedAmt, Player[] players, Player current, int maxRolls) {
		if (cmd.equals("r") && current.nRolls == maxRolls) {
			cmd = "n";
			out.println("You have already rolled the maximum " +
						"amount of times for this round, you skip instead");
		} else if (cmd.equals("n") && current.nRolls == 0) {
			cmd = "r";
			out.println("Unable to skip yet, you roll instead");
		}

		return cmd;
	}
	void rollDice (Player current){
		current.fstDice = rand.nextInt(1,7);
		current.secDice = rand.nextInt(1,7);
		current.nRolls++;
		roundMsg(current);
	}
	int p = 0;

	Player[] shufflePlayers(Player[] players) {
		List<Player> playerList = new ArrayList<>(Arrays.asList(players));
		Collections.shuffle(playerList);
		return playerList.toArray(new Player[0]);
	}

	// ---------- IO methods (nothing to do here) -----------------------

	//Return value of a players roll, accounting for doublettes and mexico
	int getScore(Player player) {
		int a = player.fstDice;
		int b = player.secDice;
		if (a == b) {
			return a * 100;
		} else if (a+b == 3) {
			return mexico;
		} else {
			return (Math.max(a, b) * 10) + Math.min (a, b);
		}

	}

	//Returns first player with lowest score
	Player getLoser(Player[] players) {
		Player lowest = players[players.length - 1];
		for (Player p : players) {
			if (getScore(p) < getScore(lowest)) {
				lowest = p;
			}
		}
		return lowest;
	}

	Player[] removeLoser(Player loser, Player[] players) {
		List<Player> ps = Arrays.asList(players);
		ps.remove(loser);
		return ps.toArray(new Player[0]);
	}

	//returns the number of players

	boolean allRolled(Player[] players, int playedAmt){
		return players.length == playedAmt;
	}
	int askNumberOfPlayers() {
		int answer;
		while (true) {
			out.print("How many players? > ");
			try {
				answer = sc.nextInt();
				if (answer > 1) {
					sc.nextLine();
					break;
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				sc.nextLine();
				out.println("Enter a valid int!!!");
			}
		}
		return answer;
	}

	int pot = 0;
	int potAdd (Player loser, int pot){
		loser.amount--;
		pot++;
	return pot;}

	//asks the name for every player, return a array of all the players
	Player[] getPlayers(int numPs) {
		Player[] players = new Player[numPs];
		for (int i = 0; i < numPs; i++) {
			String name;
			while (true) {
				out.println("Give the name for player " + (i+1) + "> ");
				name = sc.nextLine();
				if ((name == null || name.isEmpty() || name.trim().isEmpty())) {
					out.println("Enter a valid non-empty name!!! ");
				} else {
					break;
				}
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
		out.println(current.name
				+ " got " + current.fstDice
				+ " and " + current.secDice);
	}

	String getPlayerChoice(Player player) {
		out.print("Player is " + player.name + " > ");
		return sc.nextLine();
	}

	// Possibly useful utility during development
	String toString(Player p) {
		return p.name + ", "
				+ p.amount + ", "
				+ p.fstDice + ", "
				+ p.secDice + ", "
				+ p.nRolls;
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
			this.nRolls = 0;
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
