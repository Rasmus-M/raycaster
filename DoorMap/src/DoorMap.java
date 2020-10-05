import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DoorMap {

    public static void main(String... args) throws IOException {
        DoorMap doorMap = new DoorMap();
        byte[] buffer = doorMap.readMapFile("MD0", null);
        int[][] map = doorMap.mapFromBuffer(buffer);
        doorMap.generateDoorsFile(map);
    }

    public void generateDoorsFile(int[][] map) throws IOException {
        StringBuilder sb = new StringBuilder();
        int n = 1;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                int value = map[y][x];
                if (value == 13) {
                    sb.append("door_").append(n).append(":\n");
                    sb.append("       byte ").append(hexByte(x)).append("\n");
                    sb.append("       byte ").append(hexByte(y)).append("\n");
                    sb.append("       data door_").append(n).append("_init\n");
                    n++;
                }
            }
        }
        sb.append("\n");
        sb.append("n_doors:\n");
        sb.append("       data (n_doors - door_1) / door_size\n");
        sb.append("\n");
        for (int i = 1; i < n; i++) {
            sb.append("door_").append(i).append("_init:\n");
            sb.append("       data 0\n");
            sb.append("       data object_type_guard\n");
            sb.append("       byte >00\n");
            sb.append("       byte >00\n");
        }

        File outputFile = new File("doors.a99");
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(sb.toString());
        fileWriter.close();
    }

    public int[][] mapFromBuffer(byte[] buffer) {
        int[][] map = new int[64][64];
        int n = 0;
        for (int y = 0; y < 64; y++) {
            for (int x = 0; x < 64; x++) {
                map[y][x] = buffer[n++];
            }
        }
        return map;
    }

    public byte[] readMapFile(String startMarker, String endMarker) throws IOException {
        File inputFile = new File("../src/map.a99");
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

    String hexByte(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 2) {
            hex = "0" + hex;
        }
        return ">" + hex;
    }

}
