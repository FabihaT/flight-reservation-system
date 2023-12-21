package src.gui;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class AgentAccountPage extends AccountPage {

    public AgentAccountPage(CardLayout cardLayout, JPanel cardPanel, Stack<String> cardStack) throws ClassNotFoundException, IOException {
        super(cardLayout, cardPanel, cardStack);
    }

    @Override
    protected void setupBottomPanel(JPanel bottomPanel) {}
}
