import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ElectricityBillingSystemUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Map<String, Customer> customers = new HashMap<>();
    private Map<String, String> userCredentials = new HashMap<>();
    private int customerIdCounter = 1;
    private String loggedInUser = null;

    public ElectricityBillingSystemUI() {
        setTitle("Electricity Billing System");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        login();

        setVisible(true);
    }

    private void login() {
        JPanel loginPanel = new JPanel(new GridLayout(4, 1));
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginBtn);
        loginPanel.add(registerBtn);

        add(loginPanel);

        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty!");
                    return;
                }

                if (userCredentials.containsKey(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists! Please choose a different one.");
                    return;
                }

                userCredentials.put(username, password);
                JOptionPane.showMessageDialog(null, "User registered successfully! You can now log in.");
            }
        });

        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                    loggedInUser = username;
                    remove(loginPanel);
                    initializeMainUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                }
            }
        });
    }

    private void initializeMainUI() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addCustomerBtn = new JButton("Add Customer");
        JButton calculateBillBtn = new JButton("Calculate Bill");
        JButton payBillBtn = new JButton("Pay Bill");
        JButton printBillBtn = new JButton("Print Bill");

        addCustomerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        calculateBillBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateElectricityBill();
            }
        });

        payBillBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payElectricityBill();
            }
        });

        printBillBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printBill();
            }
        });

        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(calculateBillBtn);
        buttonPanel.add(payBillBtn);
        buttonPanel.add(printBillBtn);
        mainPanel.add(new JLabel("Welcome, " + loggedInUser + "!"));
        mainPanel.add(buttonPanel);

        add(mainPanel);
        revalidate();
    }

    private void addCustomer() {
        String name = JOptionPane.showInputDialog("Enter customer name:");
        String address = JOptionPane.showInputDialog("Enter customer address:");
        String meterId = JOptionPane.showInputDialog("Enter customer meter ID:");
        int unitsConsumed = Integer.parseInt(JOptionPane.showInputDialog("Enter units consumed:"));
        Customer customer = new Customer(customerIdCounter++, name, address, meterId, unitsConsumed);
        customers.put(meterId, customer);
        JOptionPane.showMessageDialog(null, "Customer added successfully!");
    }

    private void calculateElectricityBill() {
        String meterId = JOptionPane.showInputDialog("Enter customer meter ID:");
        Customer customer = customers.get(meterId);
        if (customer == null) {
            JOptionPane.showMessageDialog(null, "Customer not found!");
            return;
        }
        double billAmount = customer.calculateBillAmount();
        JOptionPane.showMessageDialog(null, "Electricity bill for " + customer.getName() + ": ₹" + billAmount);
    }

    private void payElectricityBill() {
        String meterId = JOptionPane.showInputDialog("Enter customer meter ID:");
        Customer customer = customers.get(meterId);
        if (customer == null) {
            JOptionPane.showMessageDialog(null, "Customer not found!");
            return;
        }
        double billAmount = customer.calculateBillAmount();
        double amountPaid = Double.parseDouble(JOptionPane.showInputDialog("Enter bill amount to pay (₹" + billAmount + "):"));
        if (amountPaid < 0 || amountPaid > billAmount) {
            JOptionPane.showMessageDialog(null, "Invalid amount! Please enter a valid amount.");
            return;
        }
        double remainingBalance = customer.payBill(amountPaid);
        JOptionPane.showMessageDialog(null, "Remaining balance for " + customer.getName() + ": ₹" + remainingBalance);
    }

    private void printBill() {
        String meterId = JOptionPane.showInputDialog("Enter customer meter ID:");
        Customer customer = customers.get(meterId);
        if (customer == null) {
            JOptionPane.showMessageDialog(null, "Customer not found!");
            return;
        }
        StringBuilder bill = new StringBuilder();
        bill.append("Bill for " + customer.getName() + ":\n");
        bill.append("Meter ID: " + customer.getMeterId() + "\n");
        bill.append("Name: " + customer.getName() + "\n");
        bill.append("Address: " + customer.getAddress() + "\n");
        bill.append("Units consumed: " + customer.getUnitsConsumed() + "\n");
        bill.append("Total bill amount: ₹" + customer.getTotalBillAmount() + "\n");
        JOptionPane.showMessageDialog(null, bill.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ElectricityBillingSystemUI();
            }
        });
    }
}

class Customer {
    private int id;
    private String name;
    private String address;
    private String meterId;
    private int unitsConsumed;
    private double ratePerUnit = 6.0; // Rate per unit in rupees
    private double totalBillAmount;

    public Customer(int id, String name, String address, String meterId, int unitsConsumed) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.meterId = meterId;
        this.unitsConsumed = unitsConsumed;
        this.totalBillAmount = unitsConsumed * ratePerUnit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getMeterId() {
        return meterId;
    }

    public int getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(int unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
        this.totalBillAmount = unitsConsumed * ratePerUnit;
    }

    public double getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(double ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
        this.totalBillAmount = unitsConsumed * ratePerUnit;
    }

    public double calculateBillAmount() {
        return totalBillAmount;
    }

    public double payBill(double amountPaid) {
        totalBillAmount -= amountPaid;
        return totalBillAmount;
    }

    public double getTotalBillAmount() {
        return totalBillAmount;
    }
}
