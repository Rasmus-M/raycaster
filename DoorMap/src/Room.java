import java.util.ArrayList;

public class Room {

    private final ArrayList<Square> squares;
    private final ArrayList<Sprite> sprites;

    public Room() {
        squares = new ArrayList<>();
        sprites = new ArrayList<>();
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void addSquare(Square square) {
        squares.add(square);
    }

    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    public boolean contains(Square otherSquare) {
        for (Square square : squares) {
            if (square.equals(otherSquare)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNextTo(Square otherSquare) {
        for (Square square : squares) {
            if (square.isNextTo(otherSquare)) {
                return true;
            }
        }
        return false;
    }
}
