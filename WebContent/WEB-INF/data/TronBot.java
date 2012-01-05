package tron.utils;

/**
 * トロンのボットはこのインターフェースを必ず継承しなければなりません。
 * 
 * <b>このファイルは変更しないで下さい</b>
 * 
 * @author shiva
 * 
 */
public interface TronBot {

	/**
	 * ステップにおける移動方向を決定します。
	 * 
	 * @param m
	 *            対戦が行われているステージ
	 * 
	 * @return 移動方向を表す文字列 N-北, E-東, W-西, S-南
	 * 
	 * @see tron.util.Map
	 */
	public String makeMove(Map m);

}
