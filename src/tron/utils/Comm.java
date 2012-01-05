package tron.utils;

// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Comm.java

class Comm {

	private TronBot tb;
	private int playerNum;

	public Comm(Class<?> c, int num) throws Exception {
		playerNum = num;

		try {

			Class<?> cls = c;
			tb = (TronBot) cls.newInstance();

		} catch (Exception exception) {
			throw new Exception((new StringBuilder())
					.append("Problem while starting client program: ")
					.append(exception).toString());
		}
	}

	public Comm(TronBot t, int num) throws Exception {
		playerNum = num;
		tb = t;
	}

	// returns direction to move
	public int getMove(TronMap map, int i) {

		Map mm = new Map();
		mm.setHeight(map.getHeight());
		mm.setWidth(map.getWidth());
		mm.setWalls(map.getWalls());
		if (playerNum == 0) {
			mm.setMyLocation(map.getPlayerOneLocation());
			mm.setOpponentLocation(map.getPlayerTwoLocation());
		} else {
			mm.setMyLocation(map.getPlayerTwoLocation());
			mm.setOpponentLocation(map.getPlayerOneLocation());
		}

		String direction = tb.makeMove(mm);
		int firstChar = (int) direction.substring(0, 1).toUpperCase().charAt(0);
		switch (firstChar) {
		case 'N':
			return 1;
		case 'E':
			return 2;
		case 'S':
			return 3;
		case 'W':
			return 4;
		default:
			System.err
					.println("FATAL ERROR: invalid move string. The string must "
							+ "begin with one of the characters 'N', 'E', 'S', or "
							+ "'W' (not case sensitive).");
			System.exit(1);
			return -1;
		}

	}

}
