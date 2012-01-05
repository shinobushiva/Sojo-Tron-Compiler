package tron.utils;

class TronUtils {

    public static Map createMyMap(TronMap map, int num) {
        Map mm = new Map();
        mm.setHeight(map.getHeight());
        mm.setWidth(map.getWidth());
        mm.setWalls(map.getWalls());
        if (num == 0) {
            mm.setMyLocation(map.getPlayerOneLocation());
            mm.setOpponentLocation(map.getPlayerTwoLocation());
        } else {
            mm.setMyLocation(map.getPlayerTwoLocation());
            mm.setOpponentLocation(map.getPlayerOneLocation());

        }
        return mm;
    }
}
