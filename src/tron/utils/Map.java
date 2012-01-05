package tron.utils;

/**
 * トロンのステージを表すクラスです。
 * 
 * 壁の情報や、自分、敵の位置を知る関数などが定義されています。
 * 
 * <b>このファイルは変更しないで下さい</b>
 * 
 * @author shiva
 */
public class Map {
	/**
	 * ステージの幅を保持します
	 */
	private int width;

	/**
	 * ステージの高さを保持します
	 */
	private int height;

	/**
	 * ステージの壁情報を保持します
	 */
	private boolean[][] walls;

	/**
	 * 自分のボットの位置を保持します
	 */
	private Point myLocation;

	/**
	 * 敵のボットの位置を保持します
	 */
	private Point opponentLocation;

	/**
	 * 指定した座標が壁であるかどうかをチェックします。
	 * 
	 * @param x
	 *            X座標
	 * @param y
	 *            Y座標
	 * @return 壁であればtrue、それ以外はfalse
	 */
	public boolean isWall(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return true;
		} else {
			return walls[x][y];
		}
	}

	/**
	 * 自分のボットのX座標を取得します。
	 */
	public int myX() {
		return (int) myLocation.getX();
	}

	/**
	 * 自分のボットのY座標を取得します。
	 */
	public int myY() {
		return (int) myLocation.getY();
	}

	/**
	 * 敵のボットのX座標を取得します。
	 */
	public int opponentX() {
		return (int) opponentLocation.getX();
	}

	/**
	 * 敵のボットのY座標を取得します。
	 */
	public int opponentY() {
		return (int) opponentLocation.getY();
	}

	/**
	 * ステージの幅を取得します
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ステージの幅を設定します
	 * 
	 * @deprecated ボット作成時には使用しても意味がありません
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * ステージの高さを取得します
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * ステージの高さを設定します
	 * 
	 * @deprecated ボット作成時には使用しても意味がありません
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * ステージの壁情報を２次元配列で取得します
	 * 
	 * @return ステージの壁を保持する２次元配列
	 */
	public boolean[][] getWalls() {
		return walls;
	}

	/**
	 * ステージの壁情報を２次元配列で設定します。
	 * 
	 * @deprecated ボット作成時には使用しても意味がありません
	 * 
	 * @param walls
	 *            　ステージの壁を保持する２次元配列
	 */
	public void setWalls(boolean[][] walls) {
		this.walls = walls;
	}

	public Point getMyLocation() {
		return myLocation;
	}

	public void setMyLocation(Point myLocation) {
		this.myLocation = myLocation;
	}

	public Point getOpponentLocation() {
		return opponentLocation;
	}

	public void setOpponentLocation(Point opponentLocation) {
		this.opponentLocation = opponentLocation;
	}
}
