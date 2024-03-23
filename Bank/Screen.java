import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JPanel;
import java.text.DecimalFormat;

enum View {
    Admin, Customer
}

enum CustomerState {
    LoggedOut, LoggedIn
}

enum AdminState {
    Add, Search, Found
}

public class Screen extends JPanel implements ActionListener {

    BinaryTree<Account> accounts = new BinaryTree<Account>();

    View currentView = View.Customer;
    CustomerState customerState = CustomerState.LoggedOut;
    Account currentAccount;
    AdminState adminState = AdminState.Add;


    // Tabs
    JButton adminTab = new JButton("Admin");
    JButton customerTab = new JButton("Customer");

    JButton adminAddTab = new JButton("Add");
    JButton adminSearchTab = new JButton("Search");
    JButton adminDeleteButton = new JButton("Delete");

    // Generic Inputs / Submission buttons for different forms

    JButton submitButton = new JButton("Submit");
    JButton submitButton2 = new JButton("Submit");
    JTextField input1 = new JTextField();
    JTextField input2 = new JTextField();
    JTextField input3 = new JTextField();

    JLabel input1_label = new JLabel();
    JLabel input2_label = new JLabel();
    JLabel input3_label = new JLabel();
    JLabel info_label = new JLabel();

    private JTextArea adminTextArea;
    private JScrollPane scrollPane;

    

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        try {

            Scanner scan = new Scanner(new FileReader("names.txt"));
            while (scan.hasNextLine()) {

                String[] line = scan.nextLine().split(",");
                String last_name = line[0];
                String first_name = line[1];

                accounts.add(new Account(first_name, last_name));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Tabs
        adminTab.setBounds(0, 0, 400, 50);
        adminTab.addActionListener(this);
        this.add(adminTab);

        adminAddTab.setBounds(200, 700, 200, 50);
        adminAddTab.addActionListener(this);
        this.add(adminAddTab);

        adminSearchTab.setBounds(400, 700, 200, 50);
        adminSearchTab.addActionListener(this);
        this.add(adminSearchTab);

        adminDeleteButton.setBounds(300, 500, 200, 50);
        adminDeleteButton.addActionListener(this);
        this.add(adminDeleteButton);

        customerTab.setBounds(400, 0, 400, 50);
        customerTab.addActionListener(this);
        this.add(customerTab);

        // Generic Inputs / Submission buttons for different forms
        submitButton.setBounds(200, 600, 400, 50);
        submitButton.addActionListener(this);
        this.add(submitButton);

        submitButton2.setBounds(200, 700, 400, 50);
        submitButton2.addActionListener(this);
        this.add(submitButton2);


        input1.setBounds(200, 500, 400, 50);
        this.add(input1);

        input2.setBounds(200, 400, 400, 50);
        this.add(input2);

        input3.setBounds(200, 300, 400, 50);
        this.add(input3);

        input1_label.setBounds(200, 450, 400, 50);
        this.add(input1_label);

        input2_label.setBounds(200, 350, 400, 50);
        this.add(input2_label);

        input3_label.setBounds(200, 250, 400, 50);
        this.add(input3_label);

        info_label.setBounds(200, 200, 400, 50);
        this.add(info_label);

        adminTextArea = new JTextArea(200,250); //sets the location and size
		adminTextArea.setText("");
		
		//JScrollPane
		scrollPane = new JScrollPane(adminTextArea); 
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(100,50,600,200);

		this.add(scrollPane);
        
        resetComponents();

        
        currentAccount = null;

    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    private void resetComponents() {
        submitButton.setVisible(false);
        input1.setVisible(false);
        input2.setVisible(false);
        input3.setVisible(false);
        input1.setText("");
        input2.setText("");
        input3.setText("");
        input1_label.setText("");
        input2_label.setText("");
        input3_label.setText("");
        submitButton.setText("Submit");
        submitButton2.setVisible(false);
        submitButton2.setText("Submit");
        info_label.setText("");
        customerTab.setText("Customer");
        adminTextArea.setVisible(false);
        scrollPane.setVisible(false);
        adminAddTab.setVisible(false);
        adminSearchTab.setVisible(false);
        adminDeleteButton.setVisible(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (adminState == AdminState.Add) {
            adminAddTab.setEnabled(false);
            adminSearchTab.setEnabled(true);
        } else {
            adminAddTab.setEnabled(true);
            adminSearchTab.setEnabled(false);
        }

        if (currentView == View.Admin) {
            adminTextArea.setVisible(true);
            scrollPane.setVisible(true);
            
            adminAddTab.setVisible(true);
            adminSearchTab.setVisible(true);

            g.drawString("Passes: " + accounts.getPasses(), 375, 770);

            if (adminState == AdminState.Add) {
                input1.setVisible(true);
                input2.setVisible(true);
                input3.setVisible(true);
                submitButton.setVisible(true);
                input1_label.setText("Balance");
                input2_label.setText("Pin");
                input3_label.setText("Name (First Last)");
                submitButton.setText("Add Account");
            } else if (adminState == AdminState.Search){
                input1.setVisible(true);
                submitButton.setVisible(true);
                input1_label.setText("Name (First Last)");
                submitButton.setText("Search Account");
            } else {
                // Display User Data
                g.setColor(Color.BLACK);
                g.drawString(currentAccount.toString() + " $" + currentAccount.getBalance() + " #" + currentAccount.getPin(), 300, 400);
                adminDeleteButton.setVisible(true);
            }

            

            

            String data = accounts.toString();
            adminTextArea.setText(data);

        } else if (currentView == View.Customer) {
            adminTextArea.setVisible(false);
            scrollPane.setVisible(false);
            
            if (customerState == CustomerState.LoggedOut) {
                // Login
                submitButton.setVisible(true);
                input1.setVisible(true);
                input2.setVisible(true);
                input3.setVisible(true);
                submitButton2.setVisible(false);

                input1_label.setText("PIN");
                input2_label.setText("Last Name");
                input3_label.setText("First Name");
                submitButton.setText("Login");

            } else if (customerState == CustomerState.LoggedIn) {
                // Account

                customerTab.setText("Logout Customer");

                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String formattedBalance = decimalFormat.format(currentAccount.getBalance());
                info_label.setText(currentAccount.toString() + " $" + formattedBalance);

                input1_label.setText("Amount");
                input1.setVisible(true);
                submitButton.setVisible(true);
                submitButton.setText("Deposit");
                submitButton2.setVisible(true);
                submitButton2.setText("Withdraw");

                g.drawString("Passes: " + accounts.getPasses(), 375, 770);
                


            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adminTab) {
            currentView = View.Admin;
            customerState = CustomerState.LoggedOut;
            currentAccount = null;
            accounts.resetPasses();
            resetComponents();
        } else if (e.getSource() == adminAddTab) {
            adminState = AdminState.Add;
            resetComponents();
        } else if (e.getSource() == adminSearchTab) {
            adminState = AdminState.Search;
            resetComponents();
        } else if (e.getSource() == customerTab) {
            currentView = View.Customer;
            customerState = CustomerState.LoggedOut;
            currentAccount = null;
            accounts.resetPasses();
            resetComponents();
        } else if (e.getSource() == submitButton) {
            if (currentView == View.Admin) {
                accounts.resetPasses();
               if (adminState == AdminState.Add) {
                    String balance = input1.getText();
                    String pin = input2.getText();
                    String name = input3.getText();

                    try {
                        double balance_double = Double.parseDouble(balance);
                        int pin_int = Integer.parseInt(pin);
                        String[] name_split = name.split(" ");
                        String first_name = name_split[0];
                        String last_name = name_split[1];

                        Account account = new Account(first_name, last_name);
                        account.setPin(pin_int);
                        account.setBalance(balance_double);
                        accounts.add(account);

                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid balance or pin");
                    }
               } else if (adminState == AdminState.Search) {
                    String name = input1.getText();
                    String[] name_split = name.split(" ");
                    String first_name = name_split[0];
                    String last_name = name_split[1];

                    Account account = new Account(first_name, last_name);
                    if (accounts.contains(account)) {
                        currentAccount = accounts.get(account);
                        adminState = AdminState.Found;
                        resetComponents();
                    } else {
                        System.out.println("Not found");
                    }
               }
            } else if (currentView == View.Customer) {
                if (customerState == CustomerState.LoggedOut) {
                    // Login
                    String pin = input1.getText();
                    String last_name = input2.getText();
                    String first_name = input3.getText();

                    login(first_name, last_name, pin);
                } else if (customerState == CustomerState.LoggedIn) {
                    // Account ( Deposit )
                    String amount = input1.getText();
                    if (amount.length() > 0) {
                        try {
                            double amount_double = Double.parseDouble(amount);
                            currentAccount.deposit(amount_double);
                            resetComponents();
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid amount");
                        }
                    }
                }
            }
        } else if (e.getSource() == submitButton2) {
            if (currentView == View.Admin) {
                //
            } else if (currentView == View.Customer) {
                if (customerState == CustomerState.LoggedOut) {
                    // Login
                } else if (customerState == CustomerState.LoggedIn) {
                    // Account ( Withdraw )
                    String amount = input1.getText();
                    if (amount.length() > 0) {
                        try {
                            double amount_double = Double.parseDouble(amount);
                            currentAccount.withdraw(amount_double);
                            resetComponents();
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid amount");
                        }
                    }
                }
            }
        } else if (e.getSource() == adminDeleteButton) {
            accounts.remove(currentAccount);
            currentAccount = null;
            adminState = AdminState.Add;
            resetComponents();
        }

        repaint();
    }

    private void login(String first_name, String last_name, String pin) {
        if (pin.length() == 4 && last_name.length() > 0 && first_name.length() > 0) {
            try {
                int pin_int = Integer.parseInt(pin);
                Account account = new Account(first_name, last_name);
                account.setPin(pin_int);
                if (accounts.contains(account)) {
                    System.out.println("Contains");

                    if (accounts.get(account).checkPin(pin_int)) {
                        System.out.println("Correct PIN");
                        currentAccount = accounts.get(account);
                        customerState = CustomerState.LoggedIn;
                        resetComponents();
                    } else {
                        System.out.println("Incorrect PIN" + accounts.get(account).getPin());
                    }
                    
                } else {
                    System.out.println("Not found");
                }
            } catch (NumberFormatException ex) {
               System.out.println("Invalid PIN");
            }
        }
    }
}