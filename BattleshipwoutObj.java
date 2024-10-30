import java.io.*;
import java.util.*;

public class BattleshipwoutObj {
    public abstract class Utility {

        private static final Random RANDOM = new Random();

        private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

        public static int getRandomInt(final int bound) {
            return Utility.RANDOM.nextInt(bound);
        }

        public static String readStringFromConsole() throws IOException {
            return Utility.READER.readLine();
        }

    }

    //merke, dass das ganze hier eigentlich ohne Objektorientierung gemacht wird. eg nach Lesung...
    static final int SIZE = 10;

    static void ENTER_SHIP_COORDINATE_PROMPT(final int digit, final String startOrEnd) {

        String StringCoordinatePrompt = String.format("Geben Sie die %s koordinate für ein Schiff der länge %d ein", startOrEnd, digit);
    }

    static Coordinate getRandomCoordinate() {
        return new Coordinate(Utility.getRandomInt(BattleshipwoutObj.SIZE), Utility.getRandomInt(BattleshipwoutObj.SIZE));
    }


    static record Coordinate(int column, int row) {

    }

    public enum FieldStatus {
        Free, Ship, Ship_hit, Water_hit;
    }

    static int distanceCalculator(final Coordinate start, final Coordinate end) {
        int distanceRow = Math.abs(start.row - end.row);
        int distanceColumn = Math.abs(start.column - end.column);
        return distanceRow + distanceColumn;
    }

    static boolean onOneLine(final Coordinate start, final Coordinate end) {
        if (start.row == end.row || start.column == end.column) return true;
        else return false;
    }


    static void lineSeperator() {
        System.out.println("   +-+-+-+-+-+-+-+-+-+-+      +-+-+-+-+-+-+-+-+-+-+   ");
    }

    static int getMaxSurroundingColumn(final Coordinate start, final Coordinate end) {
    return Math.min(BattleshipwoutObj.SIZE-1, Math.max(start.column(),end.column())+1);
    }
    static int getMaxSurroundingRow(final Coordinate start, final Coordinate end) {
        return Math.min(BattleshipwoutObj.SIZE-1, Math.max(start.row(),end.row())+1);
    }
    static int getMinSurroundingColumn(final Coordinate start, final Coordinate end) {
        return Math.max(BattleshipwoutObj.SIZE-1, Math.min(start.column(),end.column())-1);
    }
    static int getMinSurroundingRow(final Coordinate start, final Coordinate end) {
        return Math.max(BattleshipwoutObj.SIZE-1, Math.min(start.row(),end.row())-1);
    }

    static void showRowNumber(final int row) {
        int temp = row;
        if (temp >= 10) {
            temp++;
            System.out.print(temp);
        } else if (temp >= 0) {
            temp++;
            System.out.print(" " + temp);
        }

    }

    static Coordinate coordinateConverter(final String input) {
//        String coordinateLast = input;
        String convertUppercase = input.toUpperCase();
        Character[] convertHelper = {' ','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (int i = 1; i < 11; i++) {
            if (convertHelper[i].equals(convertUppercase.charAt(0))) {

                String rowCoords = input.substring(1);
                int lastNumbers = Integer.parseInt(rowCoords);
                lastNumbers--;
                i--;
                return new Coordinate(lastNumbers, i);


            }

        }
        return null;


    }

    static void getStartCoordinatePrompt(final int length, String start) {
        start = "Start";
        ENTER_SHIP_COORDINATE_PROMPT(length, start);
    }

    static void getEndCoordinatePrompt(final int length, String end) {
        end = "End";
        ENTER_SHIP_COORDINATE_PROMPT(length, end);
    }

    static boolean checkValidCoordinate(final String input) {
        String validCoords = "^[A-Ja-j](10|[1-9])$";
        return input.matches(validCoords);
//regex lernen ; einfach ausdrücke
    }


    static Coordinate getRandomEndCoordinate(final Coordinate start, final int distance) {
        Coordinate endCoordinate = null;
        do {
            int randomCoord = Utility.getRandomInt(4);

            if (randomCoord == 0) endCoordinate = new Coordinate(start.column + distance, start.row);
            else if (randomCoord == 1) endCoordinate = new Coordinate(start.column - distance, start.row);
            else if (randomCoord == 2) endCoordinate = new Coordinate(start.column, start.row + distance);
            else if (randomCoord == 3) endCoordinate = new Coordinate(start.column, start.row - distance);
        } while (endCoordinate != null || endCoordinate.column < 1 || endCoordinate.column > 10 || endCoordinate.row < 1 || endCoordinate.row > 10);
        //+x,-x,+y,-y

        if (endCoordinate.column < 1 || endCoordinate.column > 10 || endCoordinate.row < 1 || endCoordinate.row > 10) {

        }
        return endCoordinate;


    }
}





