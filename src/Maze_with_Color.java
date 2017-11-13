import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze_with_Color
{
    private Cell[][] board;
    private final int DELAY = 100;

    public Maze_with_Color(int rows, int cols, int[][] map){
        StdDraw.setXscale(0, cols);
        StdDraw.setYscale(0, rows);
        board = new Cell[rows][cols];
        //grab number of rows to invert grid system with StdDraw (lower-left, instead of top-left)
        int height = board.length - 1;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                board[r][c] = map[r][c] == 1 ? new Cell(c , height - r, 0.5, false) : new Cell(c, height - r, 0.5, true);
            }
    }

    public void draw()
    {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[r].length; c++){
                Cell cell = board[r][c];
                StdDraw.setPenColor(cell.getColor());
                StdDraw.filledSquare(cell.getX(), cell.getY(), cell.getRadius());
            }
        StdDraw.show();
    }

    public boolean findPath(int row, int col) {
        boolean fin = false;
        if(isValid(row,col)){
            board[row][col].setColor(Color.blue);
            draw();
            StdDraw.pause(DELAY);
            board[row][col].visitCell();
            StdDraw.pause(DELAY);
            if(isExit(row,col)){
                fin = true;
            }
            else{
                fin = findPath(row,(col+1));
                if(!fin)
                    fin = findPath((row+1),col);
                if(!fin)
                    fin = findPath(row,(col-1));
                if(!fin)
                    fin = findPath((row-1),col);
            }
            if(fin)
                board[row][col].becomePath();

            draw();
            StdDraw.pause(DELAY);
            return fin;
        }
        return fin;
    }



    private boolean isValid(int row, int col) {
        if (row>= 0 && row<board.length && col>=0 && col<board[0].length){
            if(!board[row][col].isWall() && !board[row][col].isVisited()){
                return true;
            }
        }
        return false;
    }

    private boolean isExit(int row, int col)
    {
        if(row==board.length-1 && col == board[0].length-1){
            return true;
        }
        else return false;
    }

    private static int[][] boardMaker(int rows, int cols, double p){
        int[][] bord = new int[rows][cols];
        for(int i=0;i<rows;i++){
            bord[rows-1][0] = 0;
        }
        for(int i=0;i<cols;i++){
            bord[0][i] = 0;
            bord[rows-1][i] = 0;
        }
        for(int i=0;i<rows;i++){
            bord[i][cols-1]=0;
        }
        for(int i=1;i<rows-1;i++){
            for(int x=1;x<cols-1;x++){
                if(Math.random()<p)
                    bord[i][x] = 1;
            }
        }
        if(Math.random()>.5)
            bord[1][0] = 1;
        else
            bord[0][1] = 1;

        bord[0][0] = 1;
        bord[rows-1][cols-1] = 1;
        bord[rows-1][cols-2] = 1;
        return bord;
    }

    private static int[][] importBoard(String fileName) throws Exception{
        File file = new File(fileName);
        Scanner inputObject = new Scanner(file);
        ArrayList<String[]> lines = new ArrayList<>();
        while(inputObject.hasNextLine()){
            String[] row = inputObject.nextLine().split(",");
            lines.add(row);
        }
        int[][] maze = new int[lines.size()][lines.get(0).length];
        for(int r=0;r<maze.length;r++){
           for(int c=0; c<maze[0].length;c++){
               maze[r][c] = Integer.parseInt(lines.get(r)[c]);
           }
       }
        return maze;
    }

    public static void randomBoardMaker(double p){
        StdDraw.enableDoubleBuffering();
        int[][] myMaze = boardMaker(20,20,p);
        Maze_with_Color mase = new Maze_with_Color(myMaze.length,myMaze[0].length,myMaze);
        mase.draw();
        mase.findPath(0,0);
        mase.draw();
    }

    public static void csvMaker(String fileName) throws Exception{
        int[][] csvMaze = importBoard(fileName);
        Maze_with_Color mase = new Maze_with_Color(csvMaze.length, csvMaze[0].length,csvMaze);
        mase.draw();
        mase.findPath(0,0);
        mase.draw();

    }


    public static void main(String[] args) throws Exception{
       /* StdDraw.enableDoubleBuffering();
        int[][] maze = {{1,1,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,0,1,1,1,0},
                {0,1,1,1,1,0,1,1,0,0},
                {0,1,0,1,1,1,1,1,1,0},
                {0,1,0,0,0,0,0,1,1,0},
                {0,1,1,0,1,1,1,1,1,0},
                {0,0,1,0,0,1,0,1,0,0},
                {0,1,1,0,1,1,0,1,1,0},
                {0,1,1,0,1,1,0,1,1,0},
                {0,0,0,0,0,0,0,0,1,1}};
        int[][] myMaze = boardMaker(20,20,.9);
        int[][] csvMaze = importBoard("board.csv");
        Maze_with_Color geerid = new Maze_with_Color(myMaze.length, myMaze[0].length, myMaze);
        geerid.draw();
        geerid.findPath(0, 0);
        geerid.draw();*/
       randomBoardMaker(.9);
    }
}