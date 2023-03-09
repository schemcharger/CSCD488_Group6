package pixelart;

import javax.swing.JFrame;
import magicitem.MagicItem;

public class GameWindow {



    public static void openEditor(MagicItem item){

        JFrame theWindow = new JFrame();
        theWindow.setTitle("Pixel Editor");
        GamePanel thePanel = new GamePanel();
        theWindow.setResizable(false);
        theWindow.setLocationRelativeTo(null);
        theWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        theWindow.setVisible(true);
        theWindow.add(thePanel);
        theWindow.pack();
        thePanel.startGameThread();
        //thePanel.playGame();

    }

}
