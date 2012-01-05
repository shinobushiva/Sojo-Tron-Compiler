package tron.utils;

import java.io.StringWriter;

public class Tron {

	public String[] tron(TronMap tm, TronBot p1, TronBot p2,
			int delayBetweenTurns, int maxMoveTime) {

		// System.out.println("tron");

		StringWriter sw = new StringWriter();
		TronBotWriter writer = new TronBotWriter(sw);

		TronMap map = tm;
		// System.out.println(map);
		// map.print();

		Comm comPlayer1 = null;
		Comm comPlayer2 = null;
		try {
			comPlayer1 = new Comm(p1, 0);
		} catch (Exception exception) {
			System.err.println((new StringBuilder())
					.append("Problem while starting program 1: ")
					.append(exception).toString());
			return null;
		}
		try {
			comPlayer2 = new Comm(p2, 1);
		} catch (Exception exception1) {
			System.err.println((new StringBuilder())
					.append("Problem while starting program 2: ")
					.append(exception1).toString());
			return null;
		}

		int result = 0;

		do {

			Map m = TronUtils.createMyMap(map, 0);
			writer.callback(m);

			try {
				Thread.sleep(delayBetweenTurns);
			} catch (Exception exception2) {
			}
			int k = comPlayer1.getMove(map, maxMoveTime);
			int l = comPlayer2.getMove(map, maxMoveTime);

			k = map.movePlayerOne(k);
			l = map.movePlayerTwo(l);

			if (map.playerOneX() == map.playerTwoX()
					&& map.playerOneY() == map.playerTwoY()) {
				System.out.println("Players collided. Draw!");
				result = 0;
				break;
			}
			if (k < 0 && l < 0) {
				System.out.println("Both players crashed. Draw!");
				result = 0;
				break;
			}
			if (k < 0) {
				System.out.println("Player Two Wins!");
				result = 2;
				break;
			}
			if (l < 0) {
				System.out.println("Player One Wins!");
				result = 1;
				break;
			}

		} while (true);

		sw.flush();
		return new String[] { "" + result, sw.toString() };
	}
}
