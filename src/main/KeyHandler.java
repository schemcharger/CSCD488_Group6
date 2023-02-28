package main;

import tile.TileManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed,leftPressed, rightPressed, savePressed,PlaceTile, changeColorPlus, changeColorMinus, deletePressed;


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed=true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed=true;
        }
        if(code == KeyEvent.VK_P){
            savePressed=true;
        }
        if(code == KeyEvent.VK_F){
            PlaceTile=true;
        }
        if(code == KeyEvent.VK_E){
            changeColorPlus=true;
        }
        if(code == KeyEvent.VK_Q){
            changeColorMinus=true;
        }
        if(code == KeyEvent.VK_X){
            deletePressed=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed=false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed=false;
        }
        if(code == KeyEvent.VK_P){
            savePressed=false;
        }
        if(code == KeyEvent.VK_F){
            PlaceTile=false;
        }
        if(code == KeyEvent.VK_E){
            changeColorPlus=false;
        }
        if(code == KeyEvent.VK_Q){
            changeColorMinus=false;
        }
        if(code == KeyEvent.VK_X){
            deletePressed=false;
        }
    }

    public void setUpPressed(){
       upPressed=false;
    }
    public void setDownPressed(){
        downPressed=false;
    }
    public void setLeftPressed(){
        leftPressed=false;
    }
    public void setRightPressed(){
        leftPressed=false;
    }
    public void setSavePressed(){
        savePressed=false;
    }
    public void setPlaceTile(){
        PlaceTile=false;
    }
    public void setChangeColorPlus(){
        changeColorPlus=false;
    }
    public void setChangeColorMinus(){
        changeColorMinus=false;
    }
    public void setDeletePressed(){
        deletePressed=false;
    }

}
