package ca.mcgill.ecse223.block.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayListener implements KeyListener{
  private volatile String inputString = "";

  public synchronized String takeInputs() {
    String result = inputString;
    inputString = "";
    return result;
  }

  // listener methods
  public synchronized void keyPressed(KeyEvent e){

    int code = e.getKeyCode();
    String inputChar;
    switch (code) {
      case KeyEvent.VK_KP_LEFT:
        inputChar = "l";
        break;
      case KeyEvent.VK_LEFT:
        inputChar = "l";
        break;
      case KeyEvent.VK_KP_RIGHT:
        inputChar = "r";
        break;
      case KeyEvent.VK_RIGHT:
        inputChar = "r";
        break;
      case KeyEvent.VK_SPACE:
        inputChar = " ";
        break;
      default :
        inputChar = "";
        break;
    }
    inputString = inputString + inputChar;
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    // do nothing

  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    // do nothing

  }
}
