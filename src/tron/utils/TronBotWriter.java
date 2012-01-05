package tron.utils;

import java.io.BufferedWriter;
import java.io.Writer;

class TronBotWriter {
    private int width;
    private int height;
    private boolean[][] walls;
    private Point my;
    private Point op;

    private BufferedWriter w = null;

    public TronBotWriter(Writer os) {

        try {
            w = new BufferedWriter(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callback(Map m) {

        width = m.getWidth();
        height = m.getHeight();
        walls = m.getWalls();
        my = m.getMyLocation();
        op = m.getOpponentLocation();
        try {
            w.write("" + width);
            w.write(" ");
            w.write("" + height);
            w.newLine();
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    if (my.getX() == i && my.getY() == j) {
                        w.write('1');
                    } else if (op.getX() == i && op.getY() == j) {
                        w.write('2');
                    } else if (walls[i][j]) {
                        w.write('#');
                    } else {
                        w.write(' ');
                    }
                }
                w.newLine();
            }
            w.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
