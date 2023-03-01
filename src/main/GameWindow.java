package main;

import magicitem.MagicItem;

import javax.swing.*;

public class GameWindow {



 /*   public static void main(String[] args){

        JFrame theWindow = new JFrame();
        theWindow.setTitle("Pixel Editor");
        GamePanel thePanel = new GamePanel();
        theWindow.setResizable(false);
        theWindow.setLocationRelativeTo(null);
        theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theWindow.setVisible(true);
        theWindow.add(thePanel);
        theWindow.pack();
        thePanel.startGameThread();
        //thePanel.playGame();

    }*/

    public static void run(MagicItem item){
        JFrame theWindow = new JFrame();
        theWindow.setTitle("Pixel Editor");
        GamePanel thePanel = new GamePanel(item);
        theWindow.setResizable(false);
        theWindow.setLocationRelativeTo(null);
        theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theWindow.setVisible(true);
        theWindow.add(thePanel);
        theWindow.pack();
        thePanel.startGameThread();
        //thePanel.playGame();
    }

}
