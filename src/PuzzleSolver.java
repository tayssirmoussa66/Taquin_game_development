


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Comparator;



class Node {

    int puzzle[][];
    int blankX, blankY;
    Node previous;
    int f, h, g;

    //constructor
    public Node(Node previous, int puzzle[][]) {
        this.previous = previous;
        this.puzzle = puzzle;

        // init start node with blankX, blankY and manhattan distance
        if(previous == null) {
            for (int x = 0; x < PuzzleSolver.size; x++) {
                for (int y = 0; y < PuzzleSolver.size; y++) {

                    int n = puzzle[x][y];

                    if (n == 0) {
                        blankX = x;
                        blankY = y;
                    }else {
                        int x1 = (n - 1) / PuzzleSolver.size;
                        int y1 = (n - 1) % PuzzleSolver.size;
                        this.h += Math.abs(x - x1) + Math.abs(y - y1);
                    }

                }
            }
        }
    }
     //check if in board 
    private boolean inBoard(int x, int y) {
        return (x >= 0 && y >= 0 && x < PuzzleSolver.size && y < PuzzleSolver.size);
    }
     //swap two numbers
    private void swapNumbers(int x1, int y1, int x2, int y2) {
        int tmp = puzzle[x1][y1];
        puzzle[x1][y1] = puzzle[x2][y2];
        puzzle[x2][y2] = tmp;
       
    }

    /*
        from a boardstate to a childstate, manhattan distance can only change by
        +1 or -1, so we don't need to calculate the manhattan distance for the whole board.
     */
    //
    public ArrayList<Node> getChildren() {
        ArrayList<Node> children = new ArrayList<Node>();

        for (int i = 0; i < PuzzleSolver.l_moves; i++) {

            if (inBoard(blankX + PuzzleSolver.moves[i][0], blankY + PuzzleSolver.moves[i][1])) {

                int n1 = puzzle[blankX + PuzzleSolver.moves[i][0]][blankY + PuzzleSolver.moves[i][1]];
                int m1 = calcManhattan(blankX + PuzzleSolver.moves[i][0], (n1 - 1) / PuzzleSolver.size, blankY + PuzzleSolver.moves[i][1],  (n1 - 1) % PuzzleSolver.size);

                swapNumbers(blankX, blankY, blankX + PuzzleSolver.moves[i][0], blankY + PuzzleSolver.moves[i][1]);
               
                int copy[][] = new int[PuzzleSolver.size][PuzzleSolver.size];
                for (int j = 0; j < PuzzleSolver.size; j++) {
                    copy[j] = puzzle[j].clone();
                }

                int n2 = copy[blankX][blankY];
                int x2 = (n2 - 1) / PuzzleSolver.size;
                int y2 = (n2 - 1) % PuzzleSolver.size;

                Node child = new Node(this, copy);
                child.blankX = this.blankX + PuzzleSolver.moves[i][0];
                child.blankY = this.blankY + PuzzleSolver.moves[i][1];

                int m2 = calcManhattan(blankX, x2, blankY, y2);
                int nm;

                if(m1 < m2) {
                    nm = 1;
                }else {
                    nm = -1;
                }

                child.h = this.h + nm;
                children.add(child);

                swapNumbers(blankX, blankY, blankX + PuzzleSolver.moves[i][0], blankY + PuzzleSolver.moves[i][1]);
            }
        }
        return children;
    }
//Calculate Manhattan distance
    public int calcManhattan(int x1, int x2, int y1, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 -y2);
    }
//check if equal
    @Override
    public boolean equals(Object o) {
        Node node = (Node) o;
        return Arrays.deepEquals(puzzle, node.puzzle);
    }
}
//compare two nodes
class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node n1, Node n2) {
        return Integer.compare(n1.f, n2.f);
    }
}


public class PuzzleSolver {

    static int size;
    public int puzzle[][];
    static final int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    static final int l_moves = moves.length;
    public String Steps="";
    public PuzzleSolver(int puzzle[][]) {
        this.puzzle = puzzle;
        this.size = puzzle.length;
        aStar();
    }

    
//Print the node's puzzle
    private void printBoard(int[][] board) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                String l = "";
                if (board[i][j] < 10) {
                    l = " ";
                }
                System.out.print(l + board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    //We added this method to determine the moves made by the solver algorithm
    public int compare (int[][]puzzle1,int[][]puzzle2) {
        int move=0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            	if( (puzzle1[i][j] != puzzle2[i][j])&&(puzzle1[i][j]!=0)) {
            	         
                   move=puzzle1[i][j];
            		 
            	}
            	
            }
        }
         Steps+=move+"-->" ;
        return (move);
    }
   //backtracking
    private void reconstructPath(Node current) {
   
    	Node store = null;
    	Stack<Node> path = new Stack<Node>();
        path.push(current);

        int l = 0;
        while (current.previous != null) {
            current = current.previous;
            path.push(current);
            l++;
        }
        int i=0 ;
        //We're going to use compare() in the coming steps 
        while (!path.isEmpty()) {
            Node n = path.pop();
            printBoard(n.puzzle);
         
          if(i>=1) {
          System.out.println( compare(n.puzzle,store.puzzle));
          
           }
          i++;
          store = new Node (n.previous ,n.puzzle); 
        }

        System.out.println("Solution length: " + l);
    }

    //A* Algorithm
    private void aStar() {

        Node start = new Node(null, puzzle);
        Comparator<Node> nodeComparator = new NodeComparator();

        ArrayList<Node> closedList = new ArrayList<Node>();
        PriorityQueue<Node> openList = new PriorityQueue<Node>(nodeComparator);

        openList.add(start);

        while (!openList.isEmpty()) {

            Node current = openList.poll();
            closedList.add(current);

            if (current.h == 0) {
                reconstructPath(current);
                break;
            }

            ArrayList<Node> children = current.getChildren();

            for (Node child : children) {

                if (closedList.contains(child) || openList.contains(child) && child.g >= current.g) {
                    continue;
                }

                child.g = current.g + 1;
                child.f = child.g + child.h;

                if(openList.contains(child)){
                    openList.remove(child);
                    openList.add(child);
                }else {
                    openList.add(child);
                }
            }
        }

    }

    
   //Please use the following main method to verify if the Puzzle Solver is working correctly 
  
    
    
  /*   public static void main(String[] args) {

        int puzzle[][] =   {{1, 2, 3,4},
                            {5, 6, 7,8},
                            {9, 10, 0,12},
                            {14,13,15,11}};
        PuzzleSolver PS=new PuzzleSolver(puzzle);
        System.out.println(PS.Steps+"end");
        PS.Steps="";
    }
 */   
}