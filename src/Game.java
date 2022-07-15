/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

 

/**
 *
 *
 */

@SuppressWarnings("serial")
public class Game extends javax.swing.JFrame {
	//variables declaration 
    public static Font f =new java.awt.Font("Tahoma", 1, 12) ;
    static Puzzle gamePanel;
    static Timer timer;
    private javax.swing.JButton ScoresJB;
    private JPanel controlPanel;
    private javax.swing.JPanel gamePanelJP;
    static javax.swing.JButton helpJB;
    static javax.swing.JButton playPauseJB;
    static javax.swing.JLabel scoreJL;
    static javax.swing.JButton startStopJB;
    private javax.swing.JTextField timeJL;
    private javax.swing.JPanel timerPanel;
    // End of variables declaration   
    
    //Constructor
    public Game() {
         initComponents();
        gamePanelJP.setVisible(false);
        
    }
    //Initializing components
     private void initComponents(){
        controlPanel = new javax.swing.JPanel();
        startStopJB = new javax.swing.JButton();
        playPauseJB = new javax.swing.JButton();
        scoreJL = new javax.swing.JLabel();
        helpJB = new javax.swing.JButton();
        gamePanelJP = new javax.swing.JPanel();
        timerPanel = new javax.swing.JPanel();
        ScoresJB = new javax.swing.JButton();
        timeJL = new javax.swing.JTextField();
        
      
        //Frame configuration
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Taquin Game");
        setResizable(true);
        
       
        // center on the screen
        setLocationRelativeTo(null);
        setVisible(true);
        
        //ControlPanel configuration
        controlPanel.setBackground(new java.awt.Color(204, 204, 255));
        controlPanel.setFocusable(false);
        controlPanel.setOpaque(false);
        controlPanel.setPreferredSize(new java.awt.Dimension(300, 40));
        controlPanel.setLayout(new java.awt.GridLayout(1, 2));
        //Button start&stop configuration
        startStopJB.setFont(f); 
        startStopJB.setForeground(new java.awt.Color(0, 153, 153));
        startStopJB.setText("START");
        startStopJB.setToolTipText("Start or Stop");
        startStopJB.setBorder(null);
        startStopJB.setBorderPainted(false);
        startStopJB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        startStopJB.setFocusable(false);
        startStopJB.setMaximumSize(new java.awt.Dimension(100, 50));
        startStopJB.setMinimumSize(new java.awt.Dimension(100, 50));
        startStopJB.setPreferredSize(new java.awt.Dimension(100, 50));
        startStopJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopJBActionPerformed(evt);
            }
        });
        //add button to the panel 
        controlPanel.add(startStopJB);
        
        //Button play&pause configuration
        playPauseJB.setFont(f); 
        playPauseJB.setForeground(new java.awt.Color(0, 153, 153));
        playPauseJB.setText("PLAY");
        playPauseJB.setToolTipText("Play or Pause");
        playPauseJB.setBorder(null);
        playPauseJB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playPauseJB.setFocusable(false);
        playPauseJB.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
                playPauseJBActionPerformed(evt);
            }
        });
        // add button to the panel 
        controlPanel.add(playPauseJB);
        
        //score label configuration
        scoreJL.setFont(f) ; 
        scoreJL.setForeground(new java.awt.Color(0, 153, 153));
        scoreJL.setText(String.valueOf(Puzzle.score));
        scoreJL.setToolTipText("Score");
		scoreJL.setBorder(BorderFactory.createLineBorder(Puzzle.FOREGROUND_COLOR, 3)) ;
        scoreJL.setSize(900,900);
        scoreJL.setFont(new Font("SansSerif", Font.BOLD, 40));
        scoreJL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        scoreJL.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
   
        //add label to the panel 
        controlPanel.add(scoreJL,java.awt.BorderLayout.CENTER);
        
        //add controlpanel to the frame
        this.add(controlPanel, java.awt.BorderLayout.SOUTH);
        
        // set panel for the game 
        gamePanel = new Puzzle(4,550, 20);
        
        //add this panel to the frame 
        this.add(gamePanel, java.awt.BorderLayout.CENTER);
        
        // timer panel configuration
        timerPanel.setBackground(new java.awt.Color(204, 204, 255));
        timerPanel.setFocusCycleRoot(true);
        timerPanel.setPreferredSize(new java.awt.Dimension(300, 25));
        timerPanel.setLayout(new java.awt.BorderLayout());

        ScoresJB.setFont(f); 
        ScoresJB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ScoresJB.setText(" Scores ");
        ScoresJB.setToolTipText(" Scores ");
        ScoresJB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ScoresJB.setFocusable(false);
        ScoresJB.addMouseListener(new java.awt.event.MouseAdapter() {
           
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BestScoresJLMouseClicked(evt);
            }
        });
       
        timerPanel.add(ScoresJB, java.awt.BorderLayout.EAST);
        timeJL.setEditable(false);
        timeJL.setBackground(new java.awt.Color(0, 102, 102));
        timeJL.setFont(f); 
        timeJL.setForeground(new java.awt.Color(255, 255, 255));
        timeJL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        timeJL.setText("0:0");
        timeJL.setToolTipText("");
        timeJL.setBorder(null);
        timeJL.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        timeJL.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                timeJLMouseDragged(evt);
            }
        });
        timeJL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                timeJLKeyPressed(evt);
            }
        });
        //add time to the panel
        timerPanel.add(timeJL, java.awt.BorderLayout.CENTER);
        //help boutthon configuration
        helpJB.setFont(f); 
        helpJB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        helpJB.setText(" Help ");
        helpJB.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	    helpJBActionPerformed(evt);
            }
        });
        //add the help boutton to the panel
         timerPanel.add(helpJB, java.awt.BorderLayout.LINE_START);
        //add the panel of timer to the frame
        this.add(timerPanel, java.awt.BorderLayout.PAGE_START);

        pack();
    }
       
     // definition of timeJLKeyPressed method
    private void timeJLKeyPressed(java.awt.event.KeyEvent evt) {                                  
        if(evt.getKeyCode()==KeyEvent.VK_LEFT){
            this.setLocation(this.getLocation().x-10, this.getLocation().y);
        }
        if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
            this.setLocation(this.getLocation().x+10, this.getLocation().y);
        }
        if(evt.getKeyCode()==KeyEvent.VK_UP){
            this.setLocation(this.getLocation().x, this.getLocation().y-10);
        }
        if(evt.getKeyCode()==KeyEvent.VK_DOWN){
            this.setLocation(this.getLocation().x, this.getLocation().y+10);
        }
    }                                 
                        
    // definition of startStopJBActionPerformed method

    private void startStopJBActionPerformed(java.awt.event.ActionEvent evt) {                                            
      
       if(startStopJB.getText().equals("START")){
    	   
       timer=new Timer(timeJL);
       timer.start();
       playPauseJB.setText("PAUSE");
       startStopJB.setText("STOP");
       gamePanelJP.removeAll();
       gamePanel.newGame();
       Game.scoreJL.setText(String.valueOf(Puzzle.score=10000));
       
      }
       else
      {  
    	   FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new File("C:\\Users\\Ranim\\Desktop\\TG.txt"),true);
			PrintWriter printWriter = new PrintWriter(fileWriter);
    	    printWriter.println("***********Round************");
    	    printWriter.printf("Score: %d   Time:%d:%d \n",Puzzle.score,Game.timer.min,Game.timer.sec);
    	    printWriter.println("") ;
    	    printWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
          timer.stopT();
          startStopJB.setText("START");
          playPauseJB.setText("PLAY");
          timeJL.setText("0:0");
          gamePanelJP.setVisible(false);    
      }
  
}                                           
        // definition of playPauseJBActionPerformed method

    private void playPauseJBActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (playPauseJB.getText().equals("PAUSE")) {
            playPauseJB.setText("PLAY");
            timer.pauseT();
        } 
        
        else 
        	
        {
        	
            playPauseJB.setText("PAUSE");
            timer.resumeT();
            
        }

    }    
      private void timeJLMouseDragged(java.awt.event.MouseEvent evt) {    
       setLocation(evt.getXOnScreen()-175,evt.getYOnScreen());

    
    }        
 
    // definition of BestScoresJLMouseClicked method

  private void BestScoresJLMouseClicked (MouseEvent evt) {                                     
	  JFrame frame = new JFrame();
	    String bigList[] = new String[300];
	    try {
	        File myObj = new File("C:\\Users\\Ranim\\Desktop\\TG.txt");
	        Scanner myReader = new Scanner(myObj);
	    	int i=0 ;
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	         bigList[i] = data;
	         i++;
	    }
	        myReader.close();
	        }
	    catch (FileNotFoundException e) {
	            System.out.println("An error occurred.");
	            e.printStackTrace();
	          } 

	    JOptionPane.showInputDialog(frame, "All scores", "Scores", JOptionPane.QUESTION_MESSAGE,
	        null, bigList, "CENTER");

	  }
   // definition of helpJBActionPerformed method

  private void helpJBActionPerformed( ActionEvent evt) {
      Puzzle.score=Puzzle.score-1000 ;
      Game.scoreJL.setText(String.valueOf(Puzzle.score));
      
      if ( ( timer.min == 2 )&& ( timer.sec < 59 )){
         
          Puzzle.helpStep1();
     
  }
      if ( ( timer.min == 3 )&& ( timer.sec < 15 )){
       
          Puzzle.helpStep2();
     
  }
     
       if ( ( timer.min >= 4 )||( ( timer.min == 3 )&& ( timer.sec > 16 ))){
          
          Puzzle.helpStep3();
         
     
             }
  }                         
     
}  

