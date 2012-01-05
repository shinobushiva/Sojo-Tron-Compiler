package tron.utils;

/**
 * マップ上の位置を表すクラスです。
 * 
 * <b>このファイルは変更しないで下さい</b>
 * 
 * @author shiva
 * 
 */
public class Point {
	/**
	 * X座標を表します
	 */
	private int x;
	/**
	 * Y座標を表します
	 */
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

}
