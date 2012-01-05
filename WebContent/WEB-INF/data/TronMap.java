package tron.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.Reader;

public class TronMap {

	private int width;
	private int height;
	private boolean walls[][];
	private Point playerOneLocation;
	private Point playerTwoLocation;

	public TronMap() {
	}

	public TronMap(String s) {
		readFromFile(s);
	}

	public TronMap(Reader r) {
		read(r);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isWall(int i, int j) {
		if (i < 0 || j < 0 || i >= width || j >= height)
			return true;
		else
			return getWalls()[i][j];
	}

	public int playerOneX() {
		return (int) getPlayerOneLocation().getX();
	}

	public int playerOneY() {
		return (int) getPlayerOneLocation().getY();
	}

	public int playerTwoX() {
		return (int) getPlayerTwoLocation().getX();
	}

	public int playerTwoY() {
		return (int) getPlayerTwoLocation().getY();
	}

	public void writeToStream(OutputStream outputstream) throws Exception {
		try {
			String s = (new StringBuilder()).append("").append(width)
					.append(" ").append(height).append("\n").toString();
			for (int i = 0; i < s.length(); i++)
				outputstream.write(s.charAt(i));

			for (int j = 0; j < height; j++) {
				for (int k = 0; k < width; k++) {
					if (playerOneX() == k && playerOneY() == j) {
						outputstream.write(49);
						continue;
					}
					if (playerTwoX() == k && playerTwoY() == j)
						outputstream.write(50);
					else
						outputstream.write(getWalls()[k][j] ? 35 : 32);
				}

				outputstream.write(10);
			}

			outputstream.flush();
		} catch (Exception exception) {
			System.err.println((new StringBuilder())
					.append("FATAL ERROR: failed to write to stream: ")
					.append(exception).toString());
			throw new Exception("One of the programs crashed!");
		}
	}

	public void print() {
		System.out.println((new StringBuilder()).append("").append(width)
				.append(" ").append(height).toString());
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (playerOneX() == j && playerOneY() == i) {
					System.out.write(49);
					continue;
				}
				if (playerTwoX() == j && playerTwoY() == i)
					System.out.write(50);
				else
					System.out.write(getWalls()[j][i] ? 35 : 32);
			}

			System.out.write(10);
		}

		System.out.flush();
	}

	public void readFromFile(String s) {
		try {
			read(new FileReader(s));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void read(Reader s) {
		label0: {
			BufferedReader bufferedreader = null;
			try {
				bufferedreader = new BufferedReader(s);
				String s1 = "";
				int i;
				try {
					while ((i = bufferedreader.read()) >= 0 && i != 10)
						s1 = (new StringBuilder()).append(s1).append((char) i)
								.toString();
				} catch (Exception exception3) {
					System.err.println("Could not read from stdin.");
					System.exit(1);
				}
				String as[] = s1.split(" ");
				if (as.length != 2) {
					System.err
							.println((new StringBuilder())
									.append("FATAL ERROR: the first line of input should be two integers separated by a space. Instead, got: ")
									.append(s1).toString());
					System.exit(1);
				}
				try {
					width = Integer.parseInt(as[0]);
					height = Integer.parseInt(as[1]);
				} catch (Exception exception4) {
					System.err.println((new StringBuilder())
							.append("FATAL ERROR: invalid map dimensions: ")
							.append(s1).toString());
					System.exit(1);
				}
				setWalls(new boolean[width][height]);
				boolean flag = false;
				boolean flag1 = false;
				int j = 0;
				int k = 0;
				int l = 0;
				do {
					if (k >= width || l >= height)
						break;
					int i1 = 0;
					try {
						i1 = bufferedreader.read();
					} catch (Exception exception5) {
						System.err
								.println("FATAL ERROR: exception while reading from stdin.");
						System.exit(1);
					}
					if (i1 < 0)
						break;
					switch (i1) {
					case '\n': // '\n'
					case '\r': // '\r'
						continue;

					case ' ': // ' '
						getWalls()[k][l] = false;
						break;

					case '#': // '#'
						getWalls()[k][l] = true;
						break;

					case '1': // '1'
						if (flag) {
							System.err
									.println((new StringBuilder())
											.append("FATAL ERROR: found two locations for player 1 in the map! First location is (")
											.append(getPlayerOneLocation()
													.getX())
											.append(",")
											.append(getPlayerOneLocation()
													.getY())
											.append("), second location is (")
											.append(k).append(",").append(l)
											.append(").").toString());
							System.exit(1);
						}
						getWalls()[k][l] = true;
						setPlayerOneLocation(new Point(k, l));
						flag = true;
						break;

					case '2': // '2'
						if (flag1) {
							System.err
									.println((new StringBuilder())
											.append("FATAL ERROR: found two locations for player 2 in the map! First location is (")
											.append(getPlayerTwoLocation()
													.getX())
											.append(",")
											.append(getPlayerTwoLocation()
													.getY()).append("), ")
											.append("second location is (")
											.append(k).append(",").append(l)
											.append(").").toString());
							System.exit(1);
						}
						getWalls()[k][l] = true;
						setPlayerTwoLocation(new Point(k, l));
						flag1 = true;
						break;

					default:
						System.err
								.println((new StringBuilder())
										.append("FATAL ERROR: invalid character received. ASCII value = ")
										.append(i1).toString());
						System.exit(1);
						break;
					}
					k++;
					j++;
					if (k >= width) {
						l++;
						k = 0;
					}
				} while (true);
				if (j != width * height) {
					System.err
							.println((new StringBuilder())
									.append("FATAL ERROR: wrong number of spaces in the map. Should be ")
									.append(width * height)
									.append(", but only found ").append(j)
									.append(" spaces before end of stream.")
									.toString());
					System.exit(1);
				}
				if (!flag) {
					System.err
							.println("FATAL ERROR: did not find a location for player 1!");
					System.exit(1);
				}
				if (!flag1) {
					System.err
							.println("FATAL ERROR: did not find a location for player 2!");
					System.exit(1);
				}
			} catch (Exception exception1) {
				if (bufferedreader != null)
					try {
						bufferedreader.close();
					} catch (Exception exception2) {
					}
				break label0;
			} finally {
				if (bufferedreader != null)
					try {
						bufferedreader.close();
					} catch (Exception exception7) {
					}
				// throw exception6; //XXX:I have no idea why it's here.
			}
			if (bufferedreader != null)
				try {
					bufferedreader.close();
				} catch (Exception exception) {
				}
			break label0;
		}
	}

	public int movePlayerOne(int i) {
		int j = playerOneX();
		int k = playerOneY();
		getWalls()[j][k] = true;

		switch (i) {
		case 1: // '\001'
			setPlayerOneLocation(new Point(j, k - 1));
			break;

		case 2: // '\002'
			setPlayerOneLocation(new Point(j + 1, k));
			break;

		case 3: // '\003'
			setPlayerOneLocation(new Point(j, k + 1));
			break;

		case 4: // '\004'
			setPlayerOneLocation(new Point(j - 1, k));
			break;

		default:
			return -1;
		}

		j = playerOneX();
		k = playerOneY();

		return !isWall(j, k) ? 0 : -1;
	}

	public int movePlayerTwo(int i) {
		int j = playerTwoX();
		int k = playerTwoY();
		getWalls()[j][k] = true;
		switch (i) {
		case 1: // '\001'
			setPlayerTwoLocation(new Point(j, k - 1));
			break;

		case 2: // '\002'
			setPlayerTwoLocation(new Point(j + 1, k));
			break;

		case 3: // '\003'
			setPlayerTwoLocation(new Point(j, k + 1));
			break;

		case 4: // '\004'
			setPlayerTwoLocation(new Point(j - 1, k));
			break;

		default:
			return -1;
		}
		j = playerTwoX();
		k = playerTwoY();
		return !isWall(j, k) ? 0 : -1;
	}

	public void setWalls(boolean walls[][]) {
		this.walls = walls;
	}

	public boolean[][] getWalls() {
		return walls;
	}

	public void setPlayerOneLocation(Point playerOneLocation) {
		this.playerOneLocation = playerOneLocation;
	}

	public Point getPlayerOneLocation() {
		return playerOneLocation;
	}

	public void setPlayerTwoLocation(Point playerTwoLocation) {
		this.playerTwoLocation = playerTwoLocation;
	}

	public Point getPlayerTwoLocation() {
		return playerTwoLocation;
	}

}
