public class BattleshipwoutObj {
    //merke, dass das ganze hier eigentlich ohne Objektorientierung gemacht wird. eg nach Lesung...
   class Coordinate{
        int column,row;
    }
    public enum Field{
       Free,Ship,Ship_hit,Water_hit;
    }
    static int distance(final Coordinate start, final Coordinate end) {
      int distanceRow = Math.abs(start.row - end.row);
      int distanceColumn = Math.abs(start.column - end.column);
      return distanceRow+distanceColumn;
    }
    static Coordinate getRandomCoordinate() {
       // computerMove = Math.random();
//       return  computerMove;
//        hier mit math random eine zufällige Coordinate für ein Schiff geben lassen, außer in Lesung kommt was anderes
//        ggf. später eingrenzen eg. wenn es einen Treffer gab dann soll nur in einem umkreis von 3x3 oder so geschossen werden
    }
}
