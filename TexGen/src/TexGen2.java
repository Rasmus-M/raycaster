import java.io.FileWriter;
import java.io.IOException;

public class TexGen2 {

    static final int textureHeight = 64;

    static int[] heights = {
        0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62,
        64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100, 102, 104, 106, 108, 110,
        112, 114, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150,
        152, 154, 156, 158, 160
    };

    static final String indent = "       ";

    public static void main(String... args) throws IOException {
        TexGen2 texGen = new TexGen2();
        texGen.generate();
    }

    void generate() throws IOException {
        int bankNo = 12;
        int bankOffset = 0;
        StringBuilder bankAssembly = new StringBuilder();
        StringBuilder indexAssembly = new StringBuilder();
        for (int height : heights) {
            int[] sequence = generateSequenceFloat(height);
            StringBuilder assembly = generateAssembly(height, sequence);
            int size = getAssemblySize(height, sequence);
            if (bankOffset + size > 0x2000) {
                endBank(bankNo, bankOffset, bankAssembly);
                bankNo++;
                bankOffset = 0;
                bankAssembly = new StringBuilder();
            }
            bankAssembly.append(assembly);
            addIndexEntry(bankNo, bankOffset, height, indexAssembly);
            bankOffset += size;
        }
        endBank(bankNo, bankOffset, bankAssembly);
        FileWriter fileWriter = new FileWriter("sprite-index.a99");
        fileWriter.write(indexAssembly.toString());
        fileWriter.close();
    }

    void endBank(int bankNo, int bankOffset, StringBuilder bankAssembly) throws IOException {
        bankAssembly.append("\n").append(indent).append("bss  ").append(0x2000 - bankOffset - 2).append("\n");
        bankAssembly.append("\n").append(indent).append("data -1").append("\n");
        FileWriter fileWriter = new FileWriter("sprite-bank" + bankNo  + ".a99");
        fileWriter.write(bankAssembly.toString());
        fileWriter.close();
    }

    void addIndexEntry(int bankNo, int bankOffset, int height, StringBuilder indexAssembly) {
        indexAssembly.append("sprite_vector_").append(Integer.toHexString(height)).append(":\n");
        indexAssembly.append(indent).append("data ").append(hexWord(0x6000 + bankNo * 2)).append("\n");
        indexAssembly.append(indent).append("data ").append(hexWord(0x6000 + bankOffset)).append("\n");
    }

    StringBuilder generateAssembly(int height, int[] sequence) {
        StringBuilder s = new StringBuilder();
        s.append("sprite_").append(Integer.toHexString(height)).append(":\n");
        s.append(indent).append("equ  $\n");
        int r0inc = 0;
        int r1inc = 0;
        for (int i = 0; i < sequence.length; i++) {
            int dy = sequence[i];
            if (dy == 0) {
                int zeroCount = 1;
                while (dy == 0 && i < sequence.length - 1) {
                    zeroCount++;
                    i++;
                    dy = sequence[i];
                }
                s.append("!      movb *r0+,r13\n");
                s.append("       jeq  !\n");
                for (int n = 0; n < zeroCount; n++) {
                    s.append("       szcb r9,*r1\n");
                    s.append("       socb r13,*r1+\n");
                    r1inc++;
                }
                s.append("       jmp  !!\n");
                if (zeroCount == 1) {
                    s.append("!      inc  r1\n");
                    r1inc++;
                } else if (zeroCount == 2) {
                    s.append("!      inct r1\n");
                    r1inc += 2;
                } else {
                    s.append("!      ai   r1,").append(zeroCount).append("\n");
                    r1inc += zeroCount;
                }
                r0inc++;
            } else if (dy == 1) {
                s.append("!      movb *r0+,r13\n");
                s.append("       jeq  !\n");
                s.append("       szcb r9,*r1\n");
                s.append("       socb r13,*r1+\n");
                r1inc++;
                s.append("       jmp  !!\n");
                s.append("!      inc  r1\n");
                r0inc++;
                r1inc++;
            } else if (dy == 2) {
                s.append("!      movb *r0,r13\n");
                s.append("       jeq  !\n");
                s.append("       szcb r9,*r1\n");
                s.append("       socb r13,*r1+\n");
                s.append("       jmp  !!\n");
                s.append("!      inc  r1\n");
                s.append("!      inct r0\n");
                r0inc += 2;
                r1inc += 2;
            } else if (dy == 3) {
                s.append("!      movb *r0+,r13\n");
                s.append("       jeq  !\n");
                s.append("       szcb r9,*r1\n");
                s.append("       socb r13,*r1+\n");
                s.append("       jmp  !!\n");
                s.append("!      inc  r1\n");
                s.append("!      inct r0\n");
                r0inc += 3;
                r1inc += 2;
            } else {
                s.append("!      movb *r0,r13\n");
                s.append("       jeq  !\n");
                s.append("       szcb r9,*r1\n");
                s.append("       socb r13,*r1+\n");
                s.append("       jmp  !!\n");
                s.append("!      inc  r1\n");
                s.append("!      ai   r0,").append(dy).append("\n");
                r0inc += dy;
                r1inc += 2;
            }
        }
        s.append("!      rt\n");
        return s;
    }

    int getAssemblySize(int height, int[] sequence) {
        int size = 0;
        for (int i = 0; i < sequence.length; i++) {
            int dy = sequence[i];
            if (dy == 0) {
                int zeroCount = 1;
                while (dy == 0 && i < sequence.length - 1) {
                    zeroCount++;
                    i++;
                    dy = sequence[i];
                }
                size += 6 + 4 * zeroCount + (zeroCount <= 2 ? 2 : 4);
            } else if (dy == 1) {
                size += 12;
            } else if (dy == 2) {
                size += 14;
            } else if (dy == 3) {
                size += 14;
            } else {
                size += 16;
            }
        }
        size += 2;
        return size;
    }

    int[] generateSequenceFloat(int height) {
        int[] result = new int[height];
        double dy = (double) textureHeight / height;
        double y = 0;
        double oldY = y;
        for (int i = 0; i < height; i++) {
            y += dy;
            if (y - oldY >= 1.0) {
                result[i] = (int) Math.floor(y - oldY);
                oldY = Math.floor(y);
            } else {
                result[i] = 0;
            }
        }
        return result;
    }

    String hexWord(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return ">" + hex;
    }

    String toString(int[] sequence) {
        StringBuilder sb = new StringBuilder();
        for (int dy : sequence) {
            sb.append(dy).append(",");
        }
        return sb.toString();
    }
}
