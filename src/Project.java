/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author stagiaire
 */
public class Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      
    	java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            //Please don't forget to click to see any change 
            public void run() {
                new Game().setVisible(true);
               
            }
        });
    }
    
}
