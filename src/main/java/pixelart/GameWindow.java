package pixelart;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import magicitem.MagicItem;

public class GameWindow {



    public static void openEditor(MagicItem item){

        JFrame theWindow = new JFrame();
        theWindow.setTitle("Pixel Editor");
        GamePanel thePanel = new GamePanel(item);
        theWindow.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		thePanel.stop();
        	}
        });
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
