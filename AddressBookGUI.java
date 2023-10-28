import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.awt.image.BufferedImage;

public class AddressBookGUI extends JFrame {
    private ContactBook contactBook = new ContactBook();
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField searchField;
    private JTextArea displayArea;

    public AddressBookGUI() {
        setTitle("Contact Book");
        setSize(1920, 786);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Code for Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        // Code for setting up the application icon
        try {
            BufferedImage iconImage = ImageIO.read(getClass().getResource("add-friend.png"));
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Code for ADD CONTACT button
        JButton addButton = new JButton("Add Contact");
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                contactBook.addContact(name, email, phone);
                updateDisplay("");
                // Clear the input fields after adding the desired contact
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");

            }
        });

        // Code for DELETE CONTACT button
        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = JOptionPane.showInputDialog("Enter the name to delete:");
                if (selectedName != null && !selectedName.isEmpty()) {
                    if (contactBook.removeContact(selectedName)) {
                        JOptionPane.showMessageDialog(null, "Contact deleted.");
                        updateDisplay("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Contact not found.");
                    }
                }
            }
        });

        // Code for the SEARCH PANEL
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().toLowerCase();
                updateDisplay(searchTerm);
            }
        });

        displayArea = new JTextArea();
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        displayArea.setRows(15);
        displayArea.setColumns(40);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(createColumnHeadings(), BorderLayout.SOUTH); // Add the column headings

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadContactsFromFile();
        updateDisplay("");
    }

    private JPanel createColumnHeadings() {
        JPanel headingsPanel = new JPanel(new GridLayout(1, 3));
        headingsPanel.add(new JLabel("Name"));
        headingsPanel.add(new JLabel("Email"));
        headingsPanel.add(new JLabel("Phone Number"));
        return headingsPanel;
    }

    private void updateDisplay(String searchTerm) {
        displayArea.setText("");
        HashMap<String, Contact> contacts = contactBook.getAllContacts();
        for (Contact contact : contacts.values()) {
            if (searchTerm.isEmpty() || contact.getName().toLowerCase().contains(searchTerm)) {
                displayArea
                        .append(contact.getName() + "   \t\t\t\t\t" + contact.getEmail() + "\t\t\t\t\t   "
                                + contact.getPhoneNumber()
                                + "\n");
            }
        }
        saveContactsToFile();
    }

    private void saveContactsToFile() {
        try {
            contactBook.saveToFile("contacts.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContactsFromFile() {
        try {
            contactBook.loadFromFile("contacts.dat");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddressBookGUI gui = new AddressBookGUI();
            gui.setVisible(true);
        });
    }
}
