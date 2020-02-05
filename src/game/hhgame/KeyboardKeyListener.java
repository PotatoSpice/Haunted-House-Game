package game.hhgame;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.unordered.ArrayUnorderedList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardKeyListener implements KeyListener {

    // Keyboard
    private final ArrayUnorderedList<Integer> keysPressed = new ArrayUnorderedList<>();
    private final ArrayUnorderedList<Integer> keysDown = new ArrayUnorderedList<>();
    private final ArrayUnorderedList<Integer> keysRemove = new ArrayUnorderedList<>();

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public final void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (!keysDown.contains(key)) {
            keysDown.addToRear(key);
            keysPressed.addToRear(key);
        }

        System.out.println("KEYS DOWN: " + keysDown.toString());
        e.consume();
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Fix bugged PRINTSCREEN key event, only fires on keyReleased
        if (key == java.awt.event.KeyEvent.VK_PRINTSCREEN) {
            keysPressed.addToRear(key);
        }

        keysRemove.addToRear(key);
        e.consume();
    }

    public final void clearKeys() {
        try {
            for (Integer key : keysRemove) {
                keysDown.remove(key);
                if (keysPressed.contains(key)) {
                    keysPressed.remove(key);
                }
            }
            // keys .clear()
            for (int i = 0, size = keysRemove.size(); i < size; i++) {
                keysRemove.removeLast();
            }
            for (int i = 0, size = keysPressed.size(); i < size; i++) {
                keysPressed.removeLast();
            }
        } catch (EmptyCollectionException | ElementNotFoundException ex) { }
    }

    public final boolean keyDown(int key) {
        return keysDown.contains(key);
    }

    public final boolean keyPressed(int key) {
        return keysPressed.contains(key);
    }

}