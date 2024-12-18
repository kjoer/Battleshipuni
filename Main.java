import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {
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

    static final int SIZE = 10;
    static final String PROMPT_TEMPLATE = "Gib die %sKoordinate für ein Schiff der länge %d ein";
    static final int ALL_HIT = 14;

    record Coordinate(int column, int row) {

    }

    public enum FieldStatus {
        Free, Ship, Ship_hit, Water_hit
    }

    static int distance(final Coordinate start, final Coordinate end) {
        return (Math.abs(start.row - end.row) + Math.abs(start.column - end.column));
    }

    static Coordinate getRandomCoordinate() {
        return new Coordinate(Utility.getRandomInt(SIZE), Utility.getRandomInt(SIZE));
    }

    static boolean onOneLine(final Coordinate start, final Coordinate end) {
        return (start.row == end.row || start.column == end.column);
    }

    static void showSeperatorLine() {
        System.out.println("   +-+-+-+-+-+-+-+-+-+-+      +-+-+-+-+-+-+-+-+-+-+");
    }

    static int getMaxSurroundingColumn(final Coordinate start, final Coordinate end) {
        return Math.min(Main.SIZE - 1, Math.max(start.column(), end.column()) + 1);
    }

    static int getMaxSurroundingRow(final Coordinate start, final Coordinate end) {
        return Math.min(Main.SIZE - 1, Math.max(start.row(), end.row()) + 1);
    }

    static int getMinSurroundingColumn(final Coordinate start, final Coordinate end) {
        return Math.max(0, Math.min(start.column(), end.column()) - 1);
    }

    static int getMinSurroundingRow(final Coordinate start, final Coordinate end) {
        return Math.max(0, Math.min(start.row(), end.row()) - 1);
    }

    static Coordinate convertToCoordinate(final String input) {
        String convertToUppercase = input.toUpperCase();
        Character[] convertHelper = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (int i = 1; i < convertHelper.length; i++) {
            if (convertHelper[i] == convertToUppercase.charAt(0)) {
                String columnCoords = input.substring(1);
                int rowCoords = Integer.parseInt(columnCoords);
                i--;
                rowCoords--;
                return new Coordinate(i, rowCoords);
            }
        }

        return null;
    }

    static boolean isInputValidCoord(final String input) {
        String validCoords = "^[A-Ja-j](10|[1-9])$";
        return input.matches(validCoords);
    }

    static void showRowNumber(final int row) {
        if (row < 9) {
            System.out.print(" " + (row + 1));
        } else System.out.print((row + 1));
    }

    static Coordinate getRandomEndCoordinate(final Coordinate start, final int distance) {
        Coordinate endCoordinate = null;
        do {
            int randomEndDirection = Utility.getRandomInt(4);
            if (randomEndDirection == 0) {
                endCoordinate = new Coordinate(start.column + distance - 1, start.row);
            } else if (randomEndDirection == 1) {
                endCoordinate = new Coordinate(start.column - distance + 1, start.row);
            } else if (randomEndDirection == 2) {
                endCoordinate = new Coordinate(start.column, start.row - distance + 1);
            } else if (randomEndDirection == 3) {
                endCoordinate = new Coordinate(start.column, start.row + distance - 1);
            }
        } while (endCoordinate != null && (endCoordinate.column < 1 || endCoordinate.column > SIZE || endCoordinate.row < 1 || endCoordinate.row > SIZE));
        return endCoordinate;
    }

    static void showFieldStatus(final FieldStatus field, final boolean showShips) {
        switch (field) {
            case Ship:
                System.out.print(showShips ? "O" : " ");
                break;
            case Ship_hit:
                System.out.print("X");
                break;
            case Water_hit:
                System.out.print("*");
                break;
            case Free:
            default:
                System.out.print(" ");
        }
    }

    static void placeShips(final Coordinate start, final Coordinate end, final FieldStatus[][] field) {
        if (onOneLine(start, end) == false) {
            throw new IllegalArgumentException();
        } else if (onOneLine(start, end)) {
            if (noConflict(start, end, field)) {
                if (start.row < end.row) {
                    for (int i = start.row; i <= end.row; i++) {
                        field[start.column][i] = FieldStatus.Ship;
                    }
                } else if (start.row > end.row) {
                    for (int i = start.row; i >= end.row; i--) {
                        field[start.column][i] = FieldStatus.Ship;
                    }
                } else if (start.column < end.column) {
                    for (int i = start.column; i <= end.column; i++) {
                        field[i][start.row] = FieldStatus.Ship;
                    }
                } else if (start.column > end.column) {
                    for (int i = start.column; i >= end.column; i--) {
                        field[i][start.row] = FieldStatus.Ship;
                    }
                }
            }
        }
    }

    static void showRow(final int row, final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        showRowNumber(row);
        System.out.print(" |");
        for (int i = 0; i < SIZE; i++) {
            showFieldStatus(ownField[i][row], true);
            System.out.print("|");
        }
        System.out.print("  ");
        showRowNumber(row);
        System.out.print("  |");
        for (int i = 0; i < SIZE; i++) {
            showFieldStatus(otherField[i][row], false);
            System.out.print("|");
        }
        System.out.println();
    }


    static void showFields(final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        System.out.println("    A B C D E F G H I J        A B C D E F G H I J");
        showSeperatorLine();
        for (int x = 0; x < SIZE; x++) {
            showRow(x, ownField, otherField);
            showSeperatorLine();
        }
        System.out.println();
    }

    static boolean isShipSunk(final Coordinate shot, final FieldStatus[][] field) {
        int tempRow = shot.row;
        while (tempRow < SIZE - 1 && field[shot.column][tempRow] == FieldStatus.Ship_hit) {
            tempRow++;
            if (field[shot.column][tempRow] == FieldStatus.Ship) {
                return false;
            }
        }
        tempRow = shot.row;
        while (tempRow > 0 && field[shot.column][tempRow] == FieldStatus.Ship) {
            tempRow--;
            if (field[shot.column][tempRow] == FieldStatus.Ship) {
                return false;
            }
        }
        int tempCol = shot.column;
        while (tempCol < SIZE - 1 && field[tempCol][shot.row] == FieldStatus.Ship_hit) {
            tempCol++;
            if (field[tempCol][shot.row] == FieldStatus.Ship) {
                return false;
            }
        }
        tempCol = shot.column;
        while (tempCol > 0 && field[tempCol][shot.row] == FieldStatus.Ship_hit) {
            tempCol--;
            if (field[tempCol][shot.row] == FieldStatus.Ship) {
                return false;
            }
        }


        return true;
    }

    static void setAllFree(final FieldStatus[][] field) {
        for (int i = 0; i < SIZE; i++) {
            for (int d = 0; d < SIZE; d++) {
                field[i][d] = FieldStatus.Free;
            }
        }
    }

    static int countHits(final FieldStatus[][] field) {
        int hit_counter = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int d = 0; d < SIZE; d++) {
                if (field[i][d] == FieldStatus.Ship_hit) {
                    hit_counter++;
                }
            }
        }
        return hit_counter;
    }

    static void fillWaterHits(final Coordinate shot, final FieldStatus[][] field) {
        if (isShipSunk(shot, field)) {
            int columnWalkLeft = shot.column;
            while (columnWalkLeft > 0 && field[columnWalkLeft][shot.row] == FieldStatus.Ship_hit) {
                columnWalkLeft--;
            }
            int columnWalkerRight = shot.column;
            while (columnWalkerRight < SIZE && field[columnWalkerRight][shot.row] == FieldStatus.Ship_hit) {
                columnWalkerRight++;
            }
            int rowWalkerUp = shot.row;
            while (rowWalkerUp > 0 && field[shot.column][rowWalkerUp] == FieldStatus.Ship_hit) {
                rowWalkerUp--;
            }
            int rowWalkerDown = shot.row;
            while (rowWalkerDown < SIZE && field[shot.column][rowWalkerDown] == FieldStatus.Ship_hit) {
                rowWalkerDown++;
            }
            for (int i = columnWalkLeft; i < columnWalkerRight; i++) {
                for (int d = rowWalkerUp; d < rowWalkerDown; d++) {
                    if (field[i][d] == FieldStatus.Free) {
                        field[i][d] = FieldStatus.Water_hit;
                    }
                }
            }


        }
    }

    static boolean noConflict(final Coordinate start, final Coordinate end, final FieldStatus[][] field) {
        if (onOneLine(start, end)) {


            for (
                    int column = getMinSurroundingColumn(start, end);
                    column <= getMaxSurroundingColumn(start, end);
                    column++
            ) {
                for (
                        int row = getMinSurroundingRow(start, end);
                        row <= getMaxSurroundingRow(start, end);
                        row++
                ) {
                    if (field[column][row] != FieldStatus.Free) {
                        return false;
                    }
                }
            }

        }
        return true;
    }


    static Coordinate readCoordinate(final String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                String input = Utility.readStringFromConsole();
                if (input.equals("exit") || input.equals("Exit")) {
                    System.exit(0);
                }
                if (isInputValidCoord(input)) {
                    return convertToCoordinate(input);
                }
            } catch (IOException e) {
                System.out.println("Ungueltige Eingabe" + e);
            }
        }
    }

    static boolean isThereFreeCoord(final FieldStatus[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int d = 0; d < field[i].length; d++) {
                if (field[i][d] == FieldStatus.Free) {
                    return true;
                }
            }
        }
        return false;
    }

    static Coordinate getRandomUnshotCoordinate(final FieldStatus[][] field) {
        if (isThereFreeCoord(field)) {
            while (true) {
                Coordinate randCoord = getRandomCoordinate();
                if (field[randCoord.column][randCoord.row] == FieldStatus.Free || field[randCoord.column][randCoord.row] == FieldStatus.Ship) {
                    return randCoord;
                }
            }
        } else throw new IllegalStateException();

    }

    static Coordinate readEndCoordinate(final int length) {
        String endCoordinatePrompt = String.format(PROMPT_TEMPLATE, "End", length);
        return readCoordinate(endCoordinatePrompt);
    }

    static Coordinate readStartCoordinate(final int length) {
        String startCoordinatePrompt = String.format(PROMPT_TEMPLATE, "Start", length);
        return readCoordinate(startCoordinatePrompt);
    }

    static boolean areAllHit(final FieldStatus[][] field) {
        return countHits(field) == ALL_HIT;
    }

    static boolean endCondition(final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        return (areAllHit(ownField) || areAllHit(otherField));
    }

    static boolean isValidPosition(final Coordinate start, final Coordinate end, final int length, final FieldStatus[][] field) {
        int userProvidedLength = distance(start, end);
        if (userProvidedLength == length - 1) {
            return noConflict(start, end, field);
        }
        return false;
    }

    static void shot(final Coordinate shot, final FieldStatus[][] field) {
        if (field[shot.column][shot.row] == FieldStatus.Ship) {
            field[shot.column][shot.row] = FieldStatus.Ship_hit;
            if (isShipSunk(shot, field)) {
                fillWaterHits(shot, field);
            }
        }
        if (field[shot.column][shot.row] == FieldStatus.Free) {
            field[shot.column][shot.row] = FieldStatus.Water_hit;
        }

    }

    static void outputWinner(final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        showFields(ownField, otherField);
        if (areAllHit(ownField)) {
            System.out.println("Der Computer hat Gewonnen. Schade!");
        }
        if (areAllHit(otherField)) {
            System.out.println("Du hast Gewonnen! Gut gemacht.");
        }
    }

    static void turn(final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        showFields(ownField, otherField);
        shot(readCoordinate("Gib eine Koordinate an, auf welche Geschossen werden soll"), otherField);
        shot(getRandomUnshotCoordinate(ownField), ownField);

    }

    static FieldStatus[][] initOwnField() {
        FieldStatus[][] ownField = new FieldStatus[SIZE][SIZE];
        setAllFree(ownField);
        int shipLength = 5;
        while (shipLength >= 2) {
            showFields(ownField, ownField);
            Coordinate startCoord = readStartCoordinate(shipLength);
            Coordinate endCoord = readEndCoordinate(shipLength);
            if (!isValidPosition(startCoord, endCoord, shipLength, ownField) || !noConflict(startCoord, endCoord, ownField)) {
                System.out.println("Ungueltige Eingabe, das Schiff soll die Laenge " + shipLength + " haben. Koordinaten duerfen nicht diagonal sein");
                do {
                    startCoord = readStartCoordinate(shipLength);
                    endCoord = readEndCoordinate(shipLength);
                } while (!isValidPosition(startCoord, endCoord, shipLength, ownField) || !noConflict(startCoord, endCoord, ownField));


            }
            placeShips(startCoord, endCoord, ownField);
            shipLength--;
        }
        return ownField;
    }

    static FieldStatus[][] initOtherField() {
        FieldStatus[][] otherField = new FieldStatus[SIZE][SIZE];
        setAllFree(otherField);
        int shipLength = 5;
        while (shipLength >= 2) {
            Coordinate startCoord = getRandomCoordinate();
            Coordinate endCoord = getRandomEndCoordinate(startCoord, shipLength);
            if (!noConflict(startCoord, endCoord, otherField) || !isValidPosition(startCoord, endCoord, shipLength, otherField)) {
                do {
                    startCoord = getRandomCoordinate();
                    endCoord = getRandomEndCoordinate(startCoord, shipLength);
                } while (!noConflict(startCoord, endCoord, otherField) || !isValidPosition(startCoord, endCoord, shipLength, otherField));
            }
            placeShips(startCoord, endCoord, otherField);
            shipLength--;
        }
        return otherField;
    }

    public static void main(String[] args) {
        FieldStatus[][] otherField = initOtherField();
        FieldStatus[][] ownField = initOwnField();
        showFields(otherField, otherField);
        do {
            turn(ownField, otherField);

        } while (!endCondition(ownField, otherField));
        outputWinner(ownField, otherField);

    }
}