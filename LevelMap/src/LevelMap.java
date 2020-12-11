import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LevelMap {

    private static final int SPACE = 0;
    private static final int DOOR = 32;
    private static final int DOOR_LOCKED = 33;
    private static final int BUTTON = 36;
    private static final int FIRST_OBJECT = 40;
    private static final int LAST_OBJECT = 45;
    private static final int LAST_BLOCK = 47;
    private static final int ROOM = 128;
    private static final int START = 129;
    private static final int FIRST_SPRITE = 136;
    private static final int SECRET_DOOR_MASK = 1;
    private static final String MAP_LABEL = "MD0";

    public static void main(String... args) throws IOException {
        LevelMap levelMap = new LevelMap();
        levelMap.generate("map4.a99", "..\\src\\level.a99", "..\\src\\map.a99");
    }

    public void generate(String inputMapFilePath, String doorsFilePath, String outputMapFilePath) throws IOException {
        byte[] buffer = readMapFile(inputMapFilePath, MAP_LABEL, null);
        int[][] map = mapFromBuffer(buffer);
        ArrayList<Room> rooms = findRooms(map);
        ArrayList<Door> doors  = findDoors(map, rooms);
        ArrayList<Sprite> initialSprites = findInitialSprites(map);
        ArrayList<Obj> objects = findObjects(map);
        Square start = findStart(map);
        generateLevelFile(doorsFilePath, doors, rooms, initialSprites, objects, start);
        generateMapFile(outputMapFilePath, map);
    }

    private void generateMapFile(String filePath, int[][] map) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(MAP_LABEL).append(":\n");
        for (int[] row : map) {
            for (int x = 0; x < row.length; x++) {
                int value = row[x];
                if (x % 16 == 0) {
                    sb.append("       byte ");
                }
                sb.append(hexByte(value));
                if (x % 16 == 15) {
                    sb.append("\n");
                } else {
                    sb.append(",");
                }
            }
        }
        File outputFile = new File(filePath);
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(sb.toString());
        fileWriter.close();
    }

    private void generateLevelFile(String filePath, ArrayList<Door> doors, ArrayList<Room> rooms, ArrayList<Sprite> initialSprites, ArrayList<Obj> objects, Square start) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("**\n");
        sb.append("* Variables\n");
        sb.append("*\n");
        sb.append("start_x:\n");
        sb.append("       data ").append(hexWord(start.getX() << 8 | 0x80)).append("\n");
        sb.append("start_y:\n");
        sb.append("       data ").append(hexWord(start.getY() << 8 | 0x80)).append("\n");
        sb.append("start_dir:\n");
        sb.append("       data 0\n");
        sb.append("start_dx:\n");
        sb.append("       data >0100\n");
        sb.append("start_dy:\n");
        sb.append("       data >0000\n");
        sb.append("**\n");
        sb.append("* Objects\n");
        sb.append("*\n");
        sb.append("objects:\n");
        for (Obj object : objects) {
            sb.append("       data ").append(object.getType()).append("\n");
            sb.append("       byte ").append(hexByte(object.getPosition().getX())).append("\n");
            sb.append("       byte ").append(hexByte(object.getPosition().getY())).append("\n");
        }
        sb.append("\n");
        sb.append("n_objects:\n");
        sb.append("       data (n_objects - objects) / object_size\n");
        sb.append("\n");
        sb.append("**\n");
        sb.append("* Doors\n");
        sb.append("*\n");
        sb.append("doors:\n");
        sb.append("       equ  $\n");
        int n = 1;
        for (Door door : doors) {
            Room room = door.getRoom();
            sb.append("door_").append(n).append(":\n");
            sb.append("       byte ").append(hexByte(door.getPosition().getX())).append("\n");
            sb.append("       byte ").append(hexByte(door.getPosition().getY())).append("\n");
            sb.append("       data ").append(door.isLocked() ? 1 : 0).append("\n");
            if (room != null && room.getSprites().size() > 0) {
                sb.append("       data room_").append(rooms.indexOf(room) + 1).append("_init\n");
            } else {
                sb.append("       data 0\n");
            }
            n++;
        }
        sb.append("\n");
        sb.append("n_doors:\n");
        sb.append("       data (n_doors - door_1) / door_size\n");
        sb.append("\n");
        sb.append("**\n");
        sb.append("* Sprite init data\n");
        sb.append("*\n");
        sb.append("sprite_init_data:\n");
        writeSpriteList(sb, initialSprites);
        int i = 1;
        for (Room room : rooms) {
            if (room.getSprites().size() > 0) {
                sb.append("room_").append(i).append("_init:\n");
                sb.append("       data 0\n"); // Initialized?
                writeSpriteList(sb, room.getSprites());
            }
            i++;
        }
        File outputFile = new File(filePath);
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(sb.toString());
        fileWriter.close();
    }

    private void writeSpriteList(StringBuilder sb, ArrayList<Sprite> sprites) {
        sb.append("       data ").append(sprites.size()).append("\n");
        for (Sprite sprite : sprites) {
            sb.append("       data ").append(sprite.getType()).append("\n");
            sb.append("       byte ").append(hexByte(sprite.getPosition().getX())).append("\n");
            sb.append("       byte ").append(hexByte(sprite.getPosition().getY())).append("\n");
        }
    }

    private ArrayList<Obj> findObjects(int[][] map) {
        ArrayList<Obj> objects = new ArrayList<>();
        objects.add(new Obj(0, new Square(0, 0)));
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++){
                int value = map[y][x];
                if (value >= FIRST_OBJECT && value <= LAST_OBJECT) {
                    Square square = new Square(x, y);
                    objects.add(new Obj((value - FIRST_OBJECT) / 2, square));
                }
            }
        }
        return objects;
    }

    private Square findStart(int[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++){
                int value = map[y][x];
                if (value == START) {
                    map[y][x] = SPACE;
                    return new Square(x, y);
                }
            }
        }
        return null;
    }

    private ArrayList<Sprite> findInitialSprites(int[][] map) {
        ArrayList<Sprite> sprites = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++){
                int value = map[y][x];
                if (value >= FIRST_SPRITE) {
                    Square square = new Square(x, y);
                    sprites.add(new Sprite(value - FIRST_SPRITE, square));
                    map[y][x] = SPACE;
                }
            }
        }
        return sprites;
    }

    private ArrayList<Door> findDoors(int[][] map, ArrayList<Room> rooms) {
        ArrayList<Door> doors = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++){
                int value = map[y][x];
                if (value == DOOR || value == DOOR_LOCKED || value <= LAST_BLOCK && (value & SECRET_DOOR_MASK) != 0) {
                    Square square = new Square(x, y);
                    Door door = new Door(square);
                    door.setRoom(findRoomForDoor(rooms, door));
                    door.setLocked(value == DOOR_LOCKED);
                    doors.add(door);
                }
            }
        }
        return doors;
    }

    private Room findRoomForDoor(ArrayList<Room> rooms, Door door) {
        for (Room room : rooms) {
            if (room.isNextTo(door.getPosition())) {
                return room;
            }
        }
        return null;
    }

    private ArrayList<Room> findRooms(int[][] map) {
        ArrayList<Room> rooms = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++){
                if (map[y][x] == ROOM) {
                    Square square = new Square(x, y);
                    if (findContainingRoom(square, rooms) == null) {
                        Room room = new Room();
                        measureRoom(room, map, square);
                        rooms.add(room);
                    }
                }
            }
        }
        return rooms;
    }

    private void measureRoom(Room room, int[][] map, Square square) {
        if (square.getY() < 0 || square.getY() >= map.length || square.getX() < 0 || square.getX() >= map[square.getY()].length) {
            return;
        }
        int value = map[square.getY()][square.getX()];
        if (value == ROOM || value >= FIRST_SPRITE) {
            if (value == ROOM) {
                room.addSquare(square);
            } else {
                Sprite sprite = new Sprite(value - FIRST_SPRITE, square);
                room.addSprite(sprite);
            }
            map[square.getY()][square.getX()] = SPACE;
            measureRoom(room, map, new Square(square.getX() + 1, square.getY()));
            measureRoom(room, map, new Square(square.getX() - 1, square.getY()));
            measureRoom(room, map, new Square(square.getX(), square.getY() + 1));
            measureRoom(room, map, new Square(square.getX(), square.getY() - 1));
        }
    }

    private Room findContainingRoom(Square square, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            if (room.contains(square)) {
                return room;
            }
        }
        return null;
    }

    private int[][] mapFromBuffer(byte[] buffer) {
        int[][] map = new int[64][64];
        int n = 0;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                map[y][x] = buffer[n++] & 0xff;
            }
        }
        return map;
    }

    private byte[] readMapFile(String filePath, String startMarker, String endMarker) throws IOException {
        File inputFile = new File(filePath);
        byte[] buffer = new byte[0x1000];
        int n = 0;
        boolean started = startMarker == null;
        boolean ended = false;
        FileReader fileReader = new FileReader(inputFile);
        String token = readToken(fileReader);
        while (token != null && !ended) {
            if (started && token.startsWith(">")) {
                String hexString = "";
                for (int i = 0; i < token.length(); i++) {
                    char c = token.charAt(i);
                    if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) {
                        hexString += Character.toString(c);
                    }
                    if (hexString.length() == 2) {
                        int val = Integer.parseInt(hexString, 16);
                        if (val > 127) {
                            val = val - 256;
                        }
                        buffer[n++] = (byte) val;
                        hexString = "";
                    }
                }
            } else if (!started && token.contains(startMarker)) {
                started = true;
            } else if (started && endMarker != null && token.contains(endMarker)) {
                ended = true;
            }
            token = readToken(fileReader);
        }
        return buffer;
    }

    private static String readToken(FileReader fileReader) throws IOException {
        String token = "";
        int b = fileReader.read();
        while (b != -1 && (Character.isWhitespace(b) || b == ',' || b == ';' || b == ':')) {
            b = fileReader.read();
        }
        while (b != -1 && !Character.isWhitespace(b) && b != ',' && b != ';' && b != ':') {
            token += Character.toString((char) b);
            b = fileReader.read();
        }
        if (token.length() == 0) {
            token = null;
        }
        return token;
    }

    private String hexByte(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 2) {
            hex = "0" + hex;
        }
        return ">" + hex;
    }

    private String hexWord(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return ">" + hex;
    }
}
