package sk.uniba.fmph.dcs.terra_futura.datatypes;

public record GridPosition(int x, int y) {
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
