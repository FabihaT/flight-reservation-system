// To compile: javac -cp .:lib/* -d class src/Application.java src/database/*.java src/gui/*.java src/account/*.java src/booking/*.java src/users/*.java
// To run: java -cp class;lib/* src.Application

package src;
import javax.swing.*;

import src.account.Account;
import src.database.DbConnect;

import src.gui.APL;
import src.gui.AgentAccountPage;
import src.gui.AgentHomePage;
import src.gui.AttendantAccountPage;
import src.gui.AttendantHomePage;
import src.gui.ConfirmationPage;
import src.gui.FlightsPage;
import src.gui.LoginPage;
import src.gui.PageNavigation;
import src.gui.PaymentPage;
import src.gui.RegistrationPage;
import src.gui.SeatMapPage;
import src.gui.UserAccountPage;
import src.gui.UserHomePage;
import src.gui.newPage;

import src.users.User;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class Application {

    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private static Stack<String> cardStack;
    private static DbConnect db;
    private static Connection connection;
    String role = "guest";
    static Account mainAccount = null;
    static User mainUser;
    static SeatMapPage seatMapPage;
    static PaymentPage paymentPage;

    static AttendantHomePage attendantHomePage;
    static newPage newpage;
    static APL apl;
    static AgentHomePage agenthomepage;
    static int IDtoBook;

    static FlightsPage flightsPage;
    static UserAccountPage userAccountPage;
    static AttendantAccountPage attendantAccountPage;
    static AgentAccountPage agentAccountPage;


    public Application(){}

    public static SeatMapPage getSeatMapPage () throws ClassNotFoundException, IOException {
        return seatMapPage;
    }
    public static PaymentPage getPaymentPage () throws ClassNotFoundException, IOException {
        return paymentPage;
    }

    public static AttendantHomePage getAttendantHomePage() throws ClassNotFoundException, IOException {
        return attendantHomePage;
    }
    public static newPage getNewPage() throws ClassNotFoundException, IOException {
        return newpage;
    }
    public static APL getAPL() throws ClassNotFoundException, IOException {
        return apl;
    }
    public static AgentHomePage getAgentHomePage() throws ClassNotFoundException, IOException {
        return agenthomepage;
    }
    public static int getIDtoBook () {
        return IDtoBook;
    }
    public static void setIDtoBook (int passedID) {
        IDtoBook = passedID;
    }

    public static FlightsPage getFlightsPage () throws ClassNotFoundException, IOException {
        return flightsPage;
    }

    public static UserAccountPage getUserAccountPage () throws ClassNotFoundException, IOException {
        return userAccountPage;
    }

    public static AttendantAccountPage getAttendantAccountPage () throws ClassNotFoundException, IOException {
        return attendantAccountPage;
    }

    public static AgentAccountPage getAgentAccountPage () throws ClassNotFoundException, IOException {
        return agentAccountPage;
    }


    public static void main(String[] args) throws ClassNotFoundException, IOException {
        


        SwingUtilities.invokeLater(() -> {

            try {
                db = DbConnect.getOnlyDBInstance();
            } catch (ClassNotFoundException | IOException e) {
                
                e.printStackTrace();
            }
            try {
                connection = DbConnect.getConnection();
            } catch (ClassNotFoundException | IOException e) {
                
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("ENSF480 Flight Reservations");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Create a CardLayout and a JPanel to hold the cards
            cardLayout = new CardLayout();
            cardPanel = new JPanel(cardLayout);
            cardStack = new Stack<>();

            // Create LoginPage and add it to the cardPanel with the "loginPage" identifier
            LoginPage loginPage = new LoginPage(cardLayout, cardPanel, cardStack);
            cardPanel.add(loginPage, "loginPage");

            // Create FlightsPage and add it to the cardPanel with the "flightsPage" identifier
            try {
                flightsPage = new FlightsPage(cardLayout, cardPanel, cardStack);
                cardPanel.add(flightsPage, "flightsPage");
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

            // Create RegistrationPage and add it to the cardPanel with the "registerPage" identifier
            RegistrationPage registrationPage = new RegistrationPage(cardLayout, cardPanel, cardStack);
            cardPanel.add(registrationPage, "registerPage");

            // Create an instance of UserHomePage and add it to the cardPanel with a unique identifier
            UserHomePage userHomePage;
            try {
                userHomePage = new UserHomePage(cardPanel, cardLayout, "User", cardStack);
                cardPanel.add(userHomePage, "userHomePage");
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

            // Create an instance of AttendantHomePage and add it to the cardPanel with a unique identifier
            
            try {
                attendantHomePage = new AttendantHomePage(cardPanel, cardLayout, "Flight Attendant", cardStack);
                cardPanel.add(attendantHomePage, "attendantHomePage");
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

            // Create an instance of AgentHomePage and add it to the cardPanel with a unique identifier
            
            try {
                agenthomepage = new AgentHomePage(cardPanel, cardLayout, "Flight Agent", cardStack);
                cardPanel.add(agenthomepage, "agentHomePage");
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

            try {
                newpage = new newPage(cardLayout, cardPanel, cardStack);
                cardPanel.add(newpage, "newPage");
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                apl = new APL(cardLayout, cardPanel, cardStack);
                cardPanel.add(apl, "aPL");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            

            // Create SeatMapPage and add it to the cardPanel with the "seatMapPage" identifier
            // SeatMapPage seatMapPage;
            try {
                seatMapPage = new SeatMapPage(cardLayout, cardPanel, cardStack);
                cardPanel.add(seatMapPage, "seatmapPage");
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            

            // Create PaymentPage and add it to the cardPanel with the "paymentPage" identifier
            paymentPage = new PaymentPage(cardLayout, cardPanel, cardStack);
            cardPanel.add(paymentPage, "paymentPage");

            // Create ConfirmationPage and add it to the cardPanel with the "confirmationPage" identifier
            ConfirmationPage confirmationPage = new ConfirmationPage(cardLayout, cardPanel, cardStack);
            cardPanel.add(confirmationPage, "confirmationPage");

            // Create UserAccountPage and add it to the cardPanel with the "userAccountPage" identifier
            try {
                userAccountPage = new UserAccountPage(cardLayout, cardPanel, cardStack);
                cardPanel.add(userAccountPage, "userAccountPage");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            // Create AttendantAccountPage and add it to the cardPanel with the "attendantAccountPage" identifier
            try {
                attendantAccountPage = new AttendantAccountPage(cardLayout, cardPanel, cardStack);
                cardPanel.add(attendantAccountPage, "attendantAccountPage");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            // Create AgentAccountPage and add it to the cardPanel with the "agentAccountPage" identifier
            try {
                agentAccountPage = new AgentAccountPage(cardLayout, cardPanel, cardStack);
                cardPanel.add(agentAccountPage, "agentAccountPage");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

           
            // Display the loginPage initially
            cardLayout.show(cardPanel, "loginPage");

            PageNavigation.navigateTo("loginPage");

            // Add the cardPanel to the frame
            frame.add(cardPanel);

            frame.setVisible(true);
        });
    }

    public Account getMainAccount() {
        return mainAccount;
    }

    public static User getMainUser() {
        return mainUser;
    }
   


    public void setMainAccount(String email, String password) throws ClassNotFoundException, IOException {
        mainAccount = new Account(email, password);

    }

    public void setMainUser(Account account) throws ClassNotFoundException, IOException {
        mainUser = new User(account);

    }
}
