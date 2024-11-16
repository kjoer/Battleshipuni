

import java.io.*;
import java.util.*;

public class BattleShipUni {
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
        return new Coordinate(Utility.getRandomInt(BattleShipUni.SIZE), Utility.getRandomInt(BattleShipUni.SIZE));
    }


    record Coordinate(int column, int row) {

    }

    public enum FieldStatus {
        Free, Ship, Ship_hit, Water_hit
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
        return Math.min(BattleShipUni.SIZE - 1, Math.max(start.column(), end.column()) + 1);
    }

    static int getMaxSurroundingRow(final Coordinate start, final Coordinate end) {
        return Math.min(BattleShipUni.SIZE - 1, Math.max(start.row(), end.row()) + 1);
    }

    static int getMinSurroundingColumn(final Coordinate start, final Coordinate end) {
        return Math.max(BattleShipUni.SIZE - 1, Math.min(start.column(), end.column()) - 1);
    }

    static int getMinSurroundingRow(final Coordinate start, final Coordinate end) {
        return Math.max(BattleShipUni.SIZE - 1, Math.min(start.row(), end.row()) - 1);
    }

    static void showRowNumber(final int row) {
        if (row < 9) {
            System.out.print(" ");
        }
        System.out.print(String.valueOf(row + 1));
    }
//        int temp = row;
//        if (temp >= 10) {
//            temp++;
//            System.out.print(temp);
//        } else if (temp >= 0) {
//            temp++;
//            System.out.print(" " + temp);
//
//    }

    static Coordinate coordinateConverter(final String input) {
//        String coordinateLast = input;
        String convertUppercase = input.toUpperCase();
        Character[] convertHelper = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
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

    static boolean checkInputValidCoordinate(final String input) {
        String validCoords = "^[A-Ja-j](10|[1-9])$";
        return input.matches(validCoords);
//regex lernen ; einfach ausdrücke
    }


    static Coordinate getRandomEndCoordinate(final Coordinate start, final int distance) {
        Coordinate endCoordinate = null;
        do {
            int randomDirection = Utility.getRandomInt(4);

            if (randomDirection == 0) endCoordinate = new Coordinate(start.column + distance, start.row);
            else if (randomDirection == 1) endCoordinate = new Coordinate(start.column - distance, start.row);
            else if (randomDirection == 2) endCoordinate = new Coordinate(start.column, start.row + distance);
            else if (randomDirection == 3) endCoordinate = new Coordinate(start.column, start.row - distance);
        } while (endCoordinate != null && (endCoordinate.column < 1 || endCoordinate.column > 10 || endCoordinate.row < 1 || endCoordinate.row > 10));
        //+x,-x,+y,-y

//        if (endCoordinate.column < 1 || endCoordinate.column > 10 || endCoordinate.row < 1 || endCoordinate.row > 10) {
//
//        }
        return endCoordinate;


    }

    static void showFieldStatus(final FieldStatus field, final boolean showShips) {

        switch (field) {

            case Ship:
                System.out.print(showShips ? "O" : " ");
                break;
            case Ship_hit:
                System.out.print("*");
                break;
            case Water_hit:
                System.out.print("X");
                break;
            case Free:
            default:
                System.out.print(" ");
        }

    }

    static void placeShip(final Coordinate start, final Coordinate end, final FieldStatus[][] field) {
        if (start.column < end.column || start.row < end.row) {
            for (int i = start.column(); i < end.column(); i++) {
                field[start.row()][i] = FieldStatus.Ship;

            }
            for (int d = start.row(); d < end.row(); d++) {
                field[d][start.column()] = FieldStatus.Ship;
            }

        } else if (start.column > end.column || start.row > end.row) {
            for (int e = start.column(); e > end.column; e--) {
                field[start.row()][e] = FieldStatus.Ship;

            }
        }
        for (int f = start.row(); f > end.row(); f--) {
            field[f][start.column()] = FieldStatus.Ship;
        }
    }


    static void gameBoardLooper(FieldStatus[][] showField) {
        for (int row = 0; row < showField.length; row++) {
            for (int column = 0; column < showField[row].length; column++) {
                showField[row][column] = FieldStatus.Free;
            }


        }
    }

    static void showRow(final int row, final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        if (row < 9) {
            System.out.print(" " + (row + 1) + " |");
        } else {
            System.out.print(row + "|");
        }
        for (int r = 0; r < ownField.length; r++) {
            showFieldStatus(ownField[row][r], true);
        }
        if (row < 9) {
            System.out.print(" " + (row + 1) + " |");
        } else {
            System.out.print(row + "|");
        }
        for (int r = 0; r < otherField.length; r++) {
            showFieldStatus(otherField[row][r], false);
        }
        System.out.print("|");
    }

    static void showFields(final FieldStatus[][] ownField, final FieldStatus[][] otherField) {
        System.out.println("    A B C D E F G H I J        A B C D E F G H I J");
        lineSeperator();
        for (int x = 0; x < SIZE; x++) {
            showRow(x, ownField, otherField);
            lineSeperator();
        }
        System.out.println();
    }

    static boolean isShipSunk(final Coordinate shot, final FieldStatus[][] field) {
        if (field[shot.row][shot.column] == FieldStatus.Ship_hit && field[shot.row][shot.column - 1] == FieldStatus.Free && field[shot.row][shot.column + 1] == FieldStatus.Free) {
//            if(field[shot.column][shot.column]==FieldStatus.Ship){
            for (int rowWalkerDown = shot.row; rowWalkerDown < SIZE; rowWalkerDown++) {
                if (field[rowWalkerDown][shot.column] == FieldStatus.Free) {
                    for (int columnWalkerUp = shot.column; columnWalkerUp > 0; columnWalkerUp--) { //Jochen fragen
                        if (field[columnWalkerUp][shot.row] == FieldStatus.Free) {
                            return true;
                        } else if ((rowWalkerDown == 9 && field[(rowWalkerDown - 1)][shot.column] == FieldStatus.Ship_hit) || (columnWalkerUp == 1 && field[columnWalkerUp + 1][shot.column] == FieldStatus.Ship_hit)) {
                            return true;

                        }
                    }
                }
            }
        } else if (field[shot.row][shot.column] == FieldStatus.Ship_hit && field[shot.row + 1][shot.column] == FieldStatus.Free && field[shot.row + 1][shot.column] == FieldStatus.Free) {
            for (int columnWalkerRight = shot.column; columnWalkerRight < SIZE; columnWalkerRight++) {
                if (field[shot.row][columnWalkerRight] == FieldStatus.Free) {
                    for (int columnWalkerLeft = shot.column; columnWalkerLeft > 0; columnWalkerLeft--) {
                        if (field[shot.row][columnWalkerLeft] == FieldStatus.Free) {

                            return true;
                        }
                    }
                }
            }
        } else return false;
    }

    static void setAllFree(final FieldStatus[][] field) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < field[row].length; column++) {
                field[row][column] = FieldStatus.Free;
            }
        }
    }

    static int countHits(final FieldStatus[][] field) {
        int hitCounter = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < field[row].length; column++) {
                if (field[row][column] == FieldStatus.Ship_hit) {
                    hitCounter++;

                }
            }
        }
        return hitCounter;
    }

    static void fillWaterHits(final Coordinate shot, final FieldStatus[][] field) {
        if (isShipSunk(shot, field)) {
            for (int columnWalkerRight = shot.column; columnWalkerRight < SIZE; columnWalkerRight++) {
                for (int columnWalkerLeft = shot.column; columnWalkerLeft > 0; columnWalkerLeft--) {
                    if (field[shot.row][columnWalkerLeft] == FieldStatus.Ship_hit && field[shot.row][columnWalkerRight] == FieldStatus.Ship_hit) {
                        field[shot.row + 1][columnWalkerRight] = FieldStatus.Water_hit;
                        field[shot.row - 1][columnWalkerLeft] = FieldStatus.Water_hit;
                    }

                }
            }
            for (int rowWalkerDown = shot.row; rowWalkerDown < SIZE; rowWalkerDown++) {
                for (int rowWalkerUp = shot.row; rowWalkerUp > 0; rowWalkerUp--) {
                    if (field[rowWalkerUp][shot.column] == FieldStatus.Ship_hit && field[rowWalkerDown][shot.column] == FieldStatus.Ship_hit) {
                        field[rowWalkerUp][shot.column + 1] = FieldStatus.Water_hit;
                        field[rowWalkerDown][shot.column - 1] = FieldStatus.Water_hit;
                    }
                }
            }

        }
    }

    static boolean isNoConflict(final Coordinate start, final Coordinate end, final FieldStatus[][] field) {
        if (start.column < end.column) {
            if (end.column == 9) {
                for (int columnWalkerRight = start.column; columnWalkerRight < end.column; columnWalkerRight++) {
                    if (myCheck(start, field, columnWalkerRight)) {
                        return true;
                    }
                }

            } else if (end.column == 10) {
                for (int columnWalkerRight = start.column; columnWalkerRight < end.column; columnWalkerRight++) {
                    if (myCheck(start, field, columnWalkerRight)) {
                        return true;
                    }
                }

            }
        }
        if (start.column > end.column) {
            for (int columnWalkerLeft = start.column; columnWalkerLeft > end.column; columnWalkerLeft--) {
                if (myCheck(start, field, columnWalkerLeft)) {
                    return true;

                }
            }
        }
        if (start.row < end.row) {
            for (int rowWalkerUp = start.row; rowWalkerUp > 0; rowWalkerUp--) {
                if (field[rowWalkerUp][end.row + 1] == FieldStatus.Free && field[rowWalkerUp][end.row - 1] == FieldStatus.Free) {
                    return true;
                }

            }

        }
        if (start.row > end.row) {
            for (int rowWalkerDown = start.row; rowWalkerDown > end.row; rowWalkerDown++) {
                if (end.row < SIZE) {
                    if (field[rowWalkerDown][end.row + 1] == FieldStatus.Free && field[rowWalkerDown][end.row - 1] == FieldStatus.Free) {
                        return true;
                    }
                } else if (end.row == SIZE) {
                    if (field[rowWalkerDown][end.row] == FieldStatus.Free && field[rowWalkerDown][end.row - 1] == FieldStatus.Free) {
                        return true;

                    }
                }

            }


        }
        return false;
    }

    private static boolean myCheck(Coordinate start, FieldStatus[][] field, int columnWalkerLeft) {
        return field[start.row + 1][columnWalkerLeft] == FieldStatus.Free && field[start.row - 1][columnWalkerLeft] == FieldStatus.Free;
    }


    public static void main(String[] args) {
        FieldStatus[][] fieldInitializer = new FieldStatus[SIZE][SIZE];
        setAllFree(fieldInitializer);


    }
}


//    double[] noten = new double[25]; //Deklaration und Initialisiserung
//    // die Maschine belegt im Speicher einen block mit 25 doubles (bi) und deswegen performanter und schneller als 25 einzelne doubles
//    int laenge = noten.length; //Arrays haben eine vorher festgelegte länge, die laenge kann ueber .length abgefragt werden
//    //Man kann mit dem Index effizient auf jedes Element eines Arrays zugreifen, das ist nicht selbstverständlich -> GdI
//    // Arrays können mehrdimensional sein
//    double[][] matrix = new double[52][52];
//    double[] firstRow = matrix[0]; //Zugriff auf erste Dimension liefert zweite Dimension
//    double upperLeftElement = firstRow[0];
//    double upperLeftElement1 = matrix[0][0];
//Größe der Dimensionen
//nur nion primitive Datatypes können null sein








