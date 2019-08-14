import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static List<String> citiesNames = new ArrayList<>();
    private static List<Integer> route = new ArrayList<>();
    private static List<Integer[]> connections = new ArrayList<>();
    private static int[][] connectionMatrix;

    public static void main(String[] args) throws IOException {
        fromFile("dane2.txt");
        buildMatrix();
        buildRoute();
        toScreen();
    }

    private static void toScreen() {
        route.forEach(integer -> System.out.println(citiesNames.get(integer)));
    }

    private static void fromFile(String file) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(file));
        stream.skip(1).forEach(Main::parse);
    }

    static private void parse(String line) {
        Integer[] pair = new Integer[2];
        String[] data = line.split("-");
        for (int i = 0; i <= 1; i++) {
            if (citiesNames.contains(data[i])) {
                pair[i] = citiesNames.indexOf(data[i]);
            } else {
                citiesNames.add(data[i]);
                pair[i] = citiesNames.size() - 1;
            }
        }
        connections.add(pair);
    }

    private static void buildMatrix() {
        connectionMatrix = new int[citiesNames.size()][citiesNames.size()];
        connections.forEach(integers -> {
            connectionMatrix[integers[0]][integers[1]] = 1;
            connectionMatrix[integers[1]][integers[0]] = 1;
        });
    }

    private static Integer[] findEnd(Integer[] skip) {
        Integer[] tmp = new Integer[2];
        for (int x = 0; x <= connectionMatrix.length - 1; x++) {
            int rowSum = 0;
            for (int y = 0; y <= connectionMatrix.length - 1; y++) {
                rowSum = rowSum + connectionMatrix[y][x];
                if (connectionMatrix[x][y] == 1) {
                    tmp[0] = x; tmp[1] = y;
                }
            }
            if (rowSum == 1 && !Arrays.equals(tmp, skip)) break;
        }
        return tmp;
    }

    private static Integer[] findNextConnection(Integer[] pair) {
        Integer[] tmp = new Integer[2];
        for (int y = 0; y <= connectionMatrix.length - 1; y++) {
            tmp[0] = pair[0]; tmp[1] = y;
            if (connectionMatrix[pair[0]][y] == 1 && y != pair[1]) break;
        }
        return tmp;
    }

    private static void buildRoute() {
        Integer[] start = findEnd(null);
        Integer[] stop = findEnd(start);
        Integer[] pair;
        route.add(start[0]);
        pair = start;
        for (int i = 0; i <= citiesNames.size() - 3; i++) {
            int tmp = pair[1];
            pair[1] = pair[0];
            pair[0] = tmp;
            pair = findNextConnection(pair);
            route.add(pair[0]);
        }
        route.add(stop[0]);
    }
}