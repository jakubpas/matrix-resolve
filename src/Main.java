import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static List<String> cities = new ArrayList<>();
    private static List<Integer> orderedCities = new ArrayList<>();
    private static List<Integer[]> pairs = new ArrayList<>();
    private static int[][] matrix;

    public static void main(String[] args) throws IOException {
        fromFile("dane1.txt");
        System.out.println(cities);
        pairs.forEach(integers -> System.out.println(cities.get(integers[0]) + " " + cities.get(integers[1])));
        matrix();
        algorithm();
        orderedCities.forEach(integer -> System.out.println(cities.get(integer)));

    }

    private static void fromFile(String file) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(file));
        stream.skip(1).forEach(Main::parse);
    }

    static private void parse(String line) {
        Integer[] pair = new Integer[2];
        String[] data = line.split("-");
        for (int i = 0; i <= 1; i++) {
            if (cities.contains(data[i])) {
                pair[i] = cities.indexOf(data[i]);
            } else {
                cities.add(data[i]);
                pair[i] = cities.size() - 1;
            }
        }
        pairs.add(pair);
    }

    private static void matrix() {
        matrix = new int[cities.size()][cities.size()];
        pairs.forEach(integers -> {
            matrix[integers[0]][integers[1]] = 1;
            matrix[integers[1]][integers[0]] = 1;
        });

        for (int x = 0; x <= matrix.length - 1; x++) {
            for (int y = 0; y <= matrix.length - 1; y++) {
                System.out.print(matrix[x][y]);
            }
            System.out.print("\n");
        }
    }

    private static Integer[] findOne(Integer[] skip) {
        Integer[] tmp = new Integer[2];
        for (int x = 0; x <= matrix.length - 1; x++) {
            int rowSum = 0;
            for (int y = 0; y <= matrix.length - 1; y++) {
                rowSum = rowSum + matrix[y][x];
                if (matrix[x][y] == 1) {
                    tmp[0] = x;
                    tmp[1] = y;
                }
            }
            if (skip!=null) {
                System.out.println(tmp[0] + ","+tmp[1] + " " + skip[0] + "," + skip[1]);
                System.out.println(Arrays.equals(tmp, skip));
                System.out.println();
            }
            if (rowSum == 1 && !Arrays.equals(tmp, skip)) {
                return tmp;
            }
        }
        System.out.println("dupa");
        return tmp;
    }

    private static Integer[] reverse(Integer[] pair) {
        int tmp = pair[1];
        pair[1] = pair[0];
        pair[0] = tmp;
        return pair;
    }

    private static Integer[] findOtherFromColumn(Integer[] pair) {
        Integer[] tmp = new Integer[2];
            for (int y = 0; y <= matrix.length - 1; y++) {
                tmp[0] = pair[0];
                tmp[1] = y;
                if (matrix[pair[0]][y] == 1 && y!=pair[1]) {
                    return tmp;
                }
        }
        return tmp;
    }

    private static void algorithm() {
        Integer[] start = findOne(null);
        Integer[] stop = findOne(start);
        Integer[] pair;
        orderedCities.add(start[0]);
        pair = start;
        for (int i = 0; i <= cities.size()-3; i++) {
            pair = findOtherFromColumn(reverse(pair));
            orderedCities.add(pair[0]);
        }
        orderedCities.add(stop[0]);
    }
}
