public class Sprite {

    private static final String[] SPRITE_TYPES = {
            "sprite_type_box",
            "sprite_type_barrel",
            "sprite_type_pillar",
            "sprite_type_pillar_broken",
            "sprite_type_grate",
            "sprite_type_stump",
            "sprite_type_red_potion",
            "sprite_type_green_potion",
            // Monsters
            "sprite_type_eye",
            "sprite_type_blob",
            "sprite_type_mon2",
            "sprite_type_mon3",
            "sprite_type_mon4",
            "sprite_type_mon5",
            "sprite_type_mon6",
            "sprite_type_mon7",
            "sprite_type_mon8",
            "sprite_type_mon9",
            "sprite_type_mon10",
            "sprite_type_mon11",
            "sprite_type_mon12",
            "sprite_type_mon13",
            "sprite_type_mon14",
            "sprite_type_mon15",
            "sprite_type_mon16",
            "sprite_type_mon17",
            "sprite_type_mon18",
            "sprite_type_mon19",
            "sprite_type_mon20",
            "sprite_type_mon21",
            "sprite_type_mon22",
            "sprite_type_mon23",
            "sprite_type_mon24",
            "sprite_type_mon25",
            "sprite_type_mon26",
            "sprite_type_mon27",
            "sprite_type_mon28",
            "sprite_type_mon29",
            "sprite_type_mon30",
            "sprite_type_mon31",
    };

    private int index;
    private Square position;

    public Sprite(int index, Square position) {
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
        return SPRITE_TYPES[index];
    }
}
