package src.gui;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class AttendantAccountPage extends AccountPage {

    public AttendantAccountPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        super(cardLayout, cardPanel, cardStack);
    }

    @Override
    protected void setupBottomPanel(JPanel bottomPanel) {}
}
