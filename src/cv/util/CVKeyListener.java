package cv.util;

import cv.enums.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CVKeyListener implements KeyListener {
    private CVKeyListener() {
        super();
    }

    public static CVKeyListener get() {
        return new CVKeyListener();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        KeyStroke keyStroke = KeyStroke.get(e.getKeyChar());
        keyStroke.doAction();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //do nothing
    }
}
