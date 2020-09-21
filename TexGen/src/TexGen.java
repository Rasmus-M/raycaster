import java.io.FileWriter;
import java.io.IOException;

public class TexGen {

    static final int screenHeight = 192;
    static final int textureHeight = 64;

    static int[] heights = {
        0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60,
        62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100, 102, 104, 106, 108, 110,
        112, 114, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150,
        152, 154, 156, 158, 160, 162, 164, 166, 168, 170, 172, 174, 176, 178, 180, 182, 184, 186, 188, 190, 192,
        200, 208, 216, 224, 232, 240, 248, 256, 264, 272, 280, 288, 296, 304, 312, 320, 328, 336, 344, 352,
        360, 368, 376, 384, 392, 400, 408, 416, 424, 432, 440, 448, 456, 464, 472, 480, 488, 496, 504, 512,
        520, 528
    };

    static final String indent = "       ";

    public static void main(String... args) throws IOException {
        TexGen texGen = new TexGen();
        texGen.generate();
    }

    void generate() throws IOException {
        int bankNo = 7;
        int bankOffset = 0;
        StringBuilder bankAssembly = new StringBuilder();
        StringBuilder indexAssembly = new StringBuilder();
        boolean even = true;
        for (int run = 0; run < 2; run++) {
            for (int height : heights) {
                int[] sequence = generateSequence(height);
                StringBuilder assembly = generateAssembly(height, sequence, even);
                int size = getAssemblySize(height, sequence);
                if (bankOffset + size > 0x2000) {
                    endBank(bankNo, bankOffset, bankAssembly);
                    bankNo++;
                    bankOffset = 0;
                    bankAssembly = new StringBuilder();
                }
                bankAssembly.append(assembly);
                addIndexEntry(bankNo, bankOffset, height, even, indexAssembly);
                bankOffset += size;
            }
            even = !even;
        }
        endBank(bankNo, bankOffset, bankAssembly);
        FileWriter fileWriter = new FileWriter("col-index.a99");
        fileWriter.write(indexAssembly.toString());
        fileWriter.close();
    }

    void endBank(int bankNo, int bankOffset, StringBuilder bankAssembly) throws IOException {
        bankAssembly.append("\n").append(indent).append("bss  ").append(0x2000 - bankOffset - 2).append("\n");
        bankAssembly.append("\n").append(indent).append("data -1").append("\n");
        FileWriter fileWriter = new FileWriter("col-bank" + bankNo  + ".a99");
        fileWriter.write(bankAssembly.toString());
        fileWriter.close();
    }

    void addIndexEntry(int bankNo, int bankOffset, int height, boolean even, StringBuilder indexAssembly) {
        indexAssembly.append(even ? "even" : "odd").append("_col_vector_").append(Integer.toHexString(height)).append(":\n");
        indexAssembly.append(indent).append("data ").append(hexWord(0x6000 + bankNo * 2)).append("\n");
        indexAssembly.append(indent).append("data ").append(hexWord(0x6000 + bankOffset)).append("\n");
    }

    StringBuilder generateAssembly(int height, int[] sequence, boolean even) {
        StringBuilder s = new StringBuilder();
        s.append(even ? "even" : "odd").append("_col_").append(Integer.toHexString(height)).append(":\n");
        if (height > screenHeight) {
            int offset = (int) (((double) textureHeight / height) * (height - screenHeight) / 2);
            s.append(indent).append("ai   r1,").append(offset).append("\n");
        }
        String instr = even ? "movb" : "socb";
        for (int dy : sequence) {
            if (dy == 0) {
                s.append(indent).append(instr).append(" *r1,*r3+\n");
            } else if (dy == 1) {
                s.append(indent).append(instr).append(" *r1+,*r3+\n");
            } else if (dy == 2) {
                s.append(indent).append(instr).append(" *r1,*r3+\n");
                s.append(indent).append("inct r1\n");
            } else if (dy == 3) {
                s.append(indent).append(instr).append(" *r1+,*r3+\n");
                s.append(indent).append("inct r1\n");
            } else {
                s.append(indent).append(instr).append(" *r1,*r3+\n");
                s.append(indent).append("ai   r1,").append(dy).append("\n");
            }
        }
        s.append(indent).append("rt\n");
        return s;
    }

    int getAssemblySize(int height, int[] sequence) {
        int size = 0;
        if (height > screenHeight) {
            size += 4;
        }
        for (int dy : sequence) {
            if (dy == 0) {
                size += 2;
            } else if (dy == 1) {
                size += 2;
            } else if (dy == 2) {
                size += 4;
            } else if (dy == 3) {
                size += 4;
            } else {
                size += 6;
            }
        }
        size += 2;
        return size;
    }

    int[] generateSequence(int height) {
        int[] result = new int[Math.min(height, screenHeight)];
        if (height >= textureHeight) {
            int n = 0;
            int dy;
            for (int i = 0; i < Math.min(height, screenHeight) - 1; i++) {
                n += textureHeight;
                if (n >= height) {
                    dy = 1;
                    n -= height;
                } else {
                    dy = 0;
                }
                result[i] = dy;
            }
        } else {
            int n = 0;
            int dy;
            for (int i = 0; i < height - 1; i++) {
                dy = 0;
                while (n < textureHeight) {
                    n += height;
                    dy++;
                }
                n -= textureHeight;
                result[i] = dy;
            }
        }
        return result;
    }

    void printSequence(int[] sequence) {
        for (int i : sequence) {
            System.out.print(i + " ");
        }
    }

    void findPeriods(int[] sequence) {
        int max = 0;
        int iMax = 0;
        for (int j = 1; j < sequence.length; j++) {
            int len = 0;
            for (int k = j; k < sequence.length && sequence[k - j] == sequence[k]; k++) {
                len++;
            }
            if (len > max) {
                max = len;
                iMax = j;
            }
        }
        System.out.println("Max period is " + max + " starting at pos " + iMax);
        if (max + iMax + 1 == sequence.length) {
            System.out.println("This is periodic");
        }
    }

    String hexWord(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return ">" + hex;
    }
}
