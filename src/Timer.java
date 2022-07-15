/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JTextField;

public class Timer extends Thread{
    
    boolean  pause;
    boolean  exit;
    JTextField tf;
    int min=0,sec=0;
    public Timer(JTextField tf){
        this.tf=tf;
    }
    
    @Override
    public void run(){
        min=0;sec=0;
        while (!exit) {
          if(!pause){
              timeLoop();
          }else{
              
          }
        }
    }
 
    public void timeLoop(){
                    try {
                tf.setText("" + min + ":" + sec);
                //for time precision we chose to put these  methods calls here  
                Puzzle.isSolved1();
                Puzzle.isSolved2();
                Puzzle.isSolved3();
           
                if (sec == 59) {
                    min += 1;
                    sec = 0;
                }
                Thread.sleep(1000);
                sec++;
               
              
 
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
    }
   public synchronized void resumeT(){
       pause=false;
   } 
   public synchronized void pauseT(){
       pause=true;
   }
   public synchronized void stopT(){
       exit=true;
   }
    
}
