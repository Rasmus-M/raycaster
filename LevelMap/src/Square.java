public class Square {

    private int x;
    private int y;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isNextTo(Square square) {
        return
            (square.x == x + 1 && square.y == y) ||
            (square.x == x - 1 && square.y == y) ||
            (square.y == y + 1 && square.x == x) ||
            (square.y == y - 1 && square.x == x);
    }

    public boolean equals(Square square) {
        return square.x == x && square.y == y;
    }
}
