package src.gui;
import java.util.Stack;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class PageNavigation {

    private static Stack<String> cardStack;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private static String currentCard;

    // Initialize the CardNavigation with the cardStack, cardLayout, and cardPanel
    public static void initialize(Stack<String> stack, CardLayout layout, JPanel panel) {
        cardStack = stack;
        cardLayout = layout;
        cardPanel = panel;
        currentCard = null;

    }

    // Navigate to a specified card and push the current card name onto the stack
    public static void navigateTo(String cardName) {
        if (currentCard != null) {
            cardStack.push(currentCard);  // Push the current card name onto the stack
        }
        cardLayout.show(cardPanel, cardName);  // Switch to the specified card
        currentCard = cardName;  // Update the current card name
    }

    // Navigate back to the previous card on the stack
    public static void navigateBack() {
        if (!cardStack.isEmpty()) {
            String previousCard = cardStack.pop();
            cardLayout.show(cardPanel, previousCard);
            currentCard = previousCard;  // Update the current card name
        }
    }
}
