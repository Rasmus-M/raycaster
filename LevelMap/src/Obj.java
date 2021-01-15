public class Obj {

    private static final String[] OBJECT_TYPES = {
        "object_type_red_potion",
        "object_type_green_potion",
        "object_type_key"
    };

    private int index;
    private Square position;

    public Obj(int index, Square position) {
        this.index = index;
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public String getType() {
        return OBJECT_TYPES[index];
    }
}
