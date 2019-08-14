import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    private static List<String> cities = new ArrayList<>();
    private static List<Integer> orderedCities = new ArrayList<>();
    private static List<Integer[]> pairs = new ArrayList<>();
    private static int[][] matrix = new int[cities.size()][cities.size()];


    public static void main(String[] args) throws IOException {
        fromFile("dane1.txt");
//        System.out.println(cities);
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
        for (int x = 0; x < matrix.length; x++) {
            int rowSum = 0;
            for (int y = 0; y <= matrix.length; y++) {
                rowSum = rowSum + matrix[x][y];
                if (matrix[x][y] == 1) {
                    tmp[0] = x;
                    tmp[1] = y;
                }
                if (rowSum == 1 && !Arrays.equals(tmp, skip)) {
                    return tmp;
                }
            }
        }
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
        for (int x = 0; x < matrix.length; x++) {
            int rowSum = 0;
            for (int y = 0; y <= matrix.length; y++) {
                rowSum = rowSum + matrix[x][y];
                if (matrix[x][y] == 2) {
                    tmp[0] = x;
                    tmp[1] = y;
                }
                if (rowSum == 2 && pair[0] == x) {
                    return tmp;
                }
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
        for (int i = 0; i <= cities.size(); i++) {
            pair = findOtherFromColumn(pair);
            orderedCities.add(pair[0]);
            pair = reverse(pair);

        }
        orderedCities.add(stop[1]);
    }
}
