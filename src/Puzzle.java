/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author stagiaire
 */
public class Puzzle extends JPanel {
    // Size of our Taquin game puzzle
  private int size;
  // Number of tiles
  private static int nbTiles;
  // Grid UI Dimension
  private int dimension;
  // Foreground Color
  static final Color FOREGROUND_COLOR = new Color(34,182,169); // 
   // Random object to shuffle tiles
  private static final Random RANDOM = new Random();
  // Storing the tiles in a 1D Array of integers
  private static int[] tiles;
  // Size of tile on UI
  private int tileSize;
  // Position of the blank tile
  public static int blankPos;
  // Margin for the grid on the frame
  private int margin;
  // Grid UI Size
  private int gridSize;
  // true if game over, false otherwise
  private boolean gameOver;
  //initial tiles position
  public static int[] init = new int[16] ;
  //stores the score
  public static int score = 10000 ;
  
  public Puzzle(final int size, int dim, int mar) {
    this.size = size;
    dimension = dim;
    margin = mar;
    
    
    // init tiles 
    nbTiles = size * size - 1; // -1 because we don't count blank tile
    tiles = new int[size * size];
    
    // calculate grid size and tile size
    gridSize = (dim - (2* margin));
    tileSize = gridSize / size;
    setPreferredSize(new Dimension(dimension, (dimension+margin)));
    setBackground(Color.WHITE);
    setForeground(FOREGROUND_COLOR);
    setFont(new Font("SansSerif", Font.BOLD, 60));
    
    gameOver = true;
     
   
    // it's used to implement interaction with users to move tiles and solve the game !
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        
        if (gameOver) {
			newGame();    
        } 
        else {
          // get position of the click
          int ex = e.getX() - margin;
          int ey = e.getY() - margin;
          
          // click in the grid ?
          if (ex < 0 || ex > gridSize  || ey < 0  || ey > gridSize)
            return;
          
          // get position in the grid
          int c1 = ex / tileSize;
          int r1 = ey / tileSize;
          
          // get position of the blank cell
          int c2 = blankPos % size;
          int r2 = blankPos / size;
          
          // we convert in the 1D coord 
          int clickPos = r1 * size + c1;
          
          int dir = 0;
          
          // we search direction for multiple tile moves at once
          if (c1 == c2  &&  Math.abs(r1 - r2) > 0)
            dir = (r1 - r2) > 0 ? size : -size;
          else if (r1 == r2 && Math.abs(c1 - c2) > 0)
            dir = (c1 - c2) > 0 ? 1 : -1;
            
          if (dir != 0) {
            // we move tiles in the direction
            do {
              int newBlankPos = blankPos + dir ;
              tiles[blankPos] = tiles[newBlankPos] ;
              blankPos = newBlankPos ; 
              Puzzle.score-- ;
              Game.scoreJL.setText(String.valueOf(Puzzle.score));
            } while(blankPos != clickPos);
            tiles[blankPos] = 0;
          }
          
          // we check if game is solved
          gameOver = isSolved();
        }
        
        // we repaint panel
        repaint();
      }
     
    });
    
    newGame();
  }
 public static void initial() {
	   for (int i = 0; i < tiles.length; i++) {
		   init[i]=tiles[i] ;
	   }
  }
  public void newGame() {
    do {
      reset(); // reset in intial state
      shuffle(); // shuffle
    } while(!isSolvable()); // make it until grid be solvable
    gameOver = false;
   
  }
  
  public void reset() {
    for (int i = 0; i < tiles.length; i++) {
      tiles[i] = (i + 1) % tiles.length;
    }
    
    // we set blank cell at the last
    blankPos = tiles.length - 1;
  }
  
  public void shuffle() {
    // don't include the blank tile in the shuffle, leave in the solved position
    int n = nbTiles;
    
    while (n > 1) {
      int r = RANDOM.nextInt(n--);
      int tmp = tiles[r];
      tiles[r] = tiles[n];
      tiles[n] = tmp;
    }
  }
  
  // Only half permutations o the puzzle are solvable
  // Whenever a tile is preceded by a tile with higher value it counts
  // as an inversion. In our case, with the blank tile in the solved position,
  // the number of inversions must be even for the puzzle to be solvable
  public boolean isSolvable() {
    int countInversions = 0;
    
    for (int i = 0; i < nbTiles; i++) {
      for (int j = 0; j < i; j++) {
        if (tiles[j] > tiles[i])
          countInversions++;
      }
}
    
    return countInversions % 2 == 0;
  }
  
  
  // determine if the game is solvable 
  public boolean isSolved() {
    if (tiles[tiles.length - 1] != 0) // if blank tile is not in the solved position ==> not solved
      return false;
    
    for (int i = nbTiles - 1; i >= 0; i--) {
      if (tiles[i] != i + 1)
        return false;      
    }
  	Game.timer.stopT();
  	Game.startStopJB.setText("START");
    Game.playPauseJB.setText("PLAY");
    Game.helpJB.setText("Help");
	   FileWriter fileWriter;
	try {
		fileWriter = new FileWriter(new File("C:\\Users\\Ranim\\Desktop\\TG.txt"),true);
		PrintWriter printWriter = new PrintWriter(fileWriter);
	    printWriter.println("***********Round************");
	    printWriter.printf("Score: %d   Time:%d:%d   Completed \n",Puzzle.score,Game.timer.min,Game.timer.sec);
	    printWriter.println("");
	    printWriter.close();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
    return true;
  }
  
 //checks if the game is half solved
  public static void isSolved1(){
  
  int test = 1;

for (int i=nbTiles - 8 ; i >= 0 ; i--){
  if (tiles[i] != i + 1)
              test = 0;
}
 if ((test == 0)&&(Game.timer.min == 1 )&&(Game.timer.sec== 59 )){
     
   
     JFrame frame1 = new JFrame();
     JOptionPane.showMessageDialog(frame1,"Click \"Help\" button to get the First Help step \n\n             GOOD LUCK !");
     
}
}
//checks if the game is almost solved (9,13),(10,14) 
public static void  isSolved2(){

int i=nbTiles;
int test = 1;
  if ((tiles[i-7] != 9) || (tiles[i-6] != 10) || (tiles[i-3] != 13) || (tiles[i-2] != 14)){
      
    test = 0;
    
}
  if ( (test == 0) && (Game.timer.min == 2) && ( Game.timer.sec == 59)){
     
     
  
     JFrame frame2 = new JFrame();
     JOptionPane.showMessageDialog(frame2,"Click \"Help\" button to get the Second Help step \n Ps: 1000 pts will be removed from your score \n\n               GOOD LUCK !");
     
     
 }     

}
//checks if the game is almost solved (11,15),(12) 
public static void isSolved3(){
int i=nbTiles;
int test = 1;

  if ((tiles[i-5] !=11 ) || (tiles[i-4] != 12) || (tiles[i-1] != 15) || (tiles[i] != 0)){
      
    test = 0;
    
}
  if ( (test ==0 ) && (Game.timer.min == 3) && ( Game.timer.sec == 15)){

      Game.helpJB.setText("Hints");
      JFrame frame3 = new JFrame();
     
     JOptionPane.showMessageDialog(frame3,"Sorry we're going to reset the Game providing you with hints, "
             + "Try again !\n Click \"Hints\" button for help \n Ps: 1000 pts will be removed from your score \n\n                 GOOD LUCK !");
     
     //Reset the game to its initial case , Always click to refresh 
     Game.gamePanel.tiles[0] = init[0] ;
     Game.gamePanel.tiles[1] = init[1] ;
     Game.gamePanel.tiles[2] = init[2] ;
     Game.gamePanel.tiles[3] = init[3]  ;
     Game.gamePanel.tiles[4] = init[4];
     Game.gamePanel.tiles[5] = init[5];
     Game.gamePanel.tiles[6] = init[6] ;
     Game.gamePanel.tiles[7] = init[7];
     Game.gamePanel.tiles[8]  = init[8];
     Game.gamePanel.tiles[9]  = init[9];
     Game.gamePanel.tiles[10] = init[10] ;
     Game.gamePanel.tiles[11] = init[11];
     Game.gamePanel.tiles[12] = init[12] ;
     Game.gamePanel.tiles[13] = init[13] ;
     Game.gamePanel.tiles[14] = init[14] ;
     Game.gamePanel.tiles[15] = init[15];
     //unusual change just for testing
     for(i=0;i<16;i++) {
    	 if(init[i]==0) {
    		  blankPos=init[i];
    	 }
     }
     blankPos=9 ;


 }

}

//help
static void helpStep1(){
  
         Game.gamePanel.tiles[0] = 1  ;
         Game.gamePanel.tiles[1] = 2 ;
         Game.gamePanel.tiles[2] = 3  ;
         Game.gamePanel.tiles[3] = 4  ;
         Game.gamePanel.tiles[4] = 5 ;
         Game.gamePanel.tiles[5] = 6 ;
         Game.gamePanel.tiles[6] = 7 ;
         Game.gamePanel.tiles[7] = 8 ;
         //we set the others tiles in a solvable position
         Game.gamePanel.tiles[8] = 13 ;
         Game.gamePanel.tiles[9] = 0 ;
         Game.gamePanel.tiles[10] = 11 ;
         Game.gamePanel.tiles[11] = 14 ;
         Game.gamePanel.tiles[12] = 15 ;
         Game.gamePanel.tiles[13] = 12 ;
         Game.gamePanel.tiles[14] = 10 ;
         Game.gamePanel.tiles[15] = 9 ;
         blankPos=9 ;
         Puzzle.initial() ;
}

static void helpStep2(){
    
	  Game.gamePanel.tiles[8] = 9 ;
    Game.gamePanel.tiles[9] = 10 ;
    Game.gamePanel.tiles[12] = 13 ;
    Game.gamePanel.tiles[13] = 14 ;
    Game.gamePanel.tiles[10] = 15 ;
    Game.gamePanel.tiles[11] = 11 ;
    Game.gamePanel.tiles[14] = 12 ;
    Game.gamePanel.tiles[15] = 0 ;
    blankPos=15 ;
   
   
}
static void helpStep3(){
	//this method takes a lot of time to run if we try to solve the initial case of puzzle (more than 30 min) 
    //so we used the position of tiles obtained after step 1 of help to be as an initial 
    //in order to test this method without losing much time 
    //please take a look to the method initial and the init array in helpStep1() (origin place is NewGame() )
	//Still you can test this method efficiency by visiting the class associated 
	  int puzzle[][] = {{init[0] ,init[1],init[2],init[3] },
			            {init[4],init[5],init[6],init[7] },
			            {init[8],init[9],init[10],init[11]},
			            {init[12],init[13],init[14],init[15]}
    };
    
	  PuzzleSolver SP =new PuzzleSolver(puzzle);
    JFrame frame1 = new JFrame();
    JOptionPane.showMessageDialog(frame1,SP.Steps+"end \n GOOD LUCK !");
    SP.Steps ="" ;
}


 public void drawGrid(Graphics2D g) {
    for (int i = 0; i < tiles.length; i++) {
      // we convert 1D coords to 2D coords given the size of the 2D Array
      int r = i / size;
      int c = i % size;
      // we convert in coords on the UI
      int x = margin + c * tileSize;
      int y = margin + r * tileSize;
      
      // check special case for blank tile
      if(tiles[i] == 0) {
        if (gameOver) {
          g.setColor(FOREGROUND_COLOR);
          drawCenteredString(g, "\u2713", x, y);
        }
        
        continue;
        }
      
      // for other tiles
      g.setColor(getForeground());
      g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
      g.setColor(Color.PINK);
      g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
      g.setColor(Color.PINK);
      
      drawCenteredString(g, String.valueOf(tiles[i]), x , y);
    }
  }
  
  
 
  // 
  public void drawCenteredString(Graphics2D g, String s, int x, int y) {
    // center string s for the given tile (x,y)
    FontMetrics fm = g.getFontMetrics();
    int asc = fm.getAscent();
    int desc = fm.getDescent();
    g.drawString(s,  x + (tileSize - fm.stringWidth(s)) / 2, 
        y + (asc + (tileSize - (asc + desc)) / 2));
  }
  
  @Override
 protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    drawGrid(g2D);
  }
 
}
