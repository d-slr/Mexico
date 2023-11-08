
import java.util.*;

import java.lang.Math;

import static java.lang.System.*;

/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 */
public class Mexico {

	public static void main(String[] args) {
		new Mexico().program();
	}

	final SplittableRandom rand = new SplittableRandom();
	final Scanner sc = new Scanner(in);
	final int startAmount = 3; // Amount of tokens every player has
	final int mexico = 1000; // A value greater than any other

	void program() {

		Pot pot = new Pot(0); // What the winner will get
		Player current; // The game will await inputs from this player
		Player leader = null; // Player starting the round (loser of previous round)
		int playedAmt = 0; // Amount of players who made their turn this round
		int maxRolls = 3; // Max amount of rolls for every player for the current round

		Player[] players = shufflePlayers(getPlayers(askNumberOfPlayers())); // Shuffle the players
		current = players[playedAmt];// Nobody has made a turn yet, so == first player

		out.println("Mexico Game Started");
		out.println("Controls:\nr - roll\nn - next");
		statusMsg(players); // Output how many tokens are in players' possession

		while (players.length > 1) { // Game over when only one player left
			if (playedAmt == 0) {
				leader = current; //The first player making their turn in a round == leader
			}

			String cmd = getPlayerChoice(current).toLowerCase(); //Get the current players command
			//Checks if the round has ended, if so, resets all values to start a new one
			if ((current.nRolls == maxRolls && (playedAmt + 1) == players.length) ||
				((playedAmt + 1) == players.length && cmd.equals("n") && current.nRolls > 0)) {
				maxRolls = 3;
				playedAmt = 0;
				players = endRound(players, pot); //resets values, handles pot and kicks loser
				current = players[playedAmt];
				statusMsg(players);
				continue; //skip to the next round
			}
			//checks if the received command is applicable in the current game context
			//if not, alerts the player and changes it
			cmd = verifyCmd(cmd, playedAmt, players, current, maxRolls);
			if ("r".equals(cmd)) {
					rollDice(current);
			} else if ("n".equals(cmd)) {
				playedAmt++; //n was received, so a player has played
				maxRolls = leader.nRolls; //maxRolls depends on how many times the leader rolled
				current = next(players, playedAmt); //passes the turn to the next player
			} else {

				out.println("No such command"); //Player answered something else than r or n, repeat the move.

			}
		}
		out.println("Game Over, winner is " + players[0].name + ". Will get " + pot.value + " from pot");
	}

	/*Resettar alla spelares nRolls till 0. Flyttar ett poäng från loser till pot. Ifall loser har 0 poäng
	tas den spelaren bort från players. I båda fallen shufflas ordningen på players, och ifall losern är kvar
	blir det losern som börjar nästa round.
	 */
	Player[] endRound(Player[] players, Pot pot) {
		for (Player player : players) {
			player.nRolls = 0;
		}
		Player loser = getLoser(players);
		out.println("Round ended, " + loser.name + " lost");
		potAdd(loser, pot);

		if(loser.amount < 1) {
			out.println(loser.name + " is eliminated");
			return shufflePlayers(removeLoser(loser, players));
		} else {
			players = shufflePlayers(players);
			List<Player> playerList = Arrays.asList(players);
			Collections.swap(playerList, 0, playerList.indexOf(loser));
			return playerList.toArray(players);
		}
	}

	/*Returnerar playern med index playedAmt.
	Används när playedAMt har inkrementerats och nästa spelare ska spela
	 */
	Player next (Player[] players, int playedAmt) {
		return players[playedAmt];

	}
	/* Tar cmd antalet spelare som har spelat, antalet spelare,
	vem som är current och hur mnga. ggr. man får rolla.
	om man försöker rolla när maxrolls är uppfyllt så tvingar den "n"
	Om man försöker skippa men inte har rollat någon gång så tvingar den en roll.
	 */
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

	//Slumpar två tal för fstDice och sndDice. Inkrementerar nRolls, visar statusmeddelande
	void rollDice (Player current){
		current.fstDice = rand.nextInt(1,7);
		current.secDice = rand.nextInt(1,7);
		current.nRolls++;
		roundMsg(current);
	}

	/*GÖr players-arrayen till en arrayList, gör shuffle på den,
	 returnerar den shufflade arrayen, konverterad tillbaka till en player-array */
	Player[] shufflePlayers(Player[] players) {
		List<Player> playerList = new ArrayList<>(Arrays.asList(players));
		Collections.shuffle(playerList);
		return playerList.toArray(new Player[0]);
	}

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

	//Returns first player with the lowest score
	Player getLoser(Player[] players) {
		Player lowest = players[players.length - 1];
		for (Player p : players) {
			if (getScore(p) < getScore(lowest)) {
				lowest = p;
			}
		} return lowest;
	}


	/* konverterar players till en arrayList, använder metoden remove för att ta bort loser. Funkar för att
	loser är den första i listan om flera har lika låg poäng. */
	Player[] removeLoser(Player loser, Player[] players) {
		List<Player> ps	= new ArrayList<>(Arrays.asList(players));
		ps.remove(loser);
		return ps.toArray(new Player[0]);
	}

	//returns the number of players
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

	//Tar bort ett poäng från en spelare, ökar value i pot med 1.
	void potAdd (Player loser, Pot pot) {
		loser.amount--;
		pot.value++;
	}

	//asks the name for every player, return an array of all the players
	Player[] getPlayers(int numPs) {
		Player[] players = new Player[numPs];
		for (int i = 0; i < numPs; i++) {
			String name;
			while (true) {
				out.print("Give the name for player " + (i+1) + "> ");
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

	//Prints name and score for every player
	void statusMsg(Player[] players) {
		out.print("Status: ");
        for (Player player : players) {
            out.print(player.name + " " + player.amount + " ");
        }
		out.println();
	}

	//Print what player rolled
	void roundMsg(Player current) {
		out.println(current.name
				+ " got " + current.fstDice
				+ " and " + current.secDice);
	}

	//Print who is current player, returns player choice
	String getPlayerChoice(Player player) {
		out.print("Player is " + player.name + " > ");
		return sc.nextLine();
	}

	//Pot är en klass så variablerna i ett pot-objekt kan ändras i olika scope.
	class Pot {
		int value;
		public Pot(int value){
			this.value = value;
		}
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
		Player[] ps = { new Player("Bob"), new Player("Alice"), new Player("Big Brain") };
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
