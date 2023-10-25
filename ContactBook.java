import java.io.*;
import java.util.HashMap;

public class ContactBook {
    private HashMap<String, Contact> contacts = new HashMap<>();

    public void addContact(String name, String email, String phoneNumber) {
        Contact contact = new Contact(name, email, phoneNumber);
        contacts.put(name, contact);
    }

    public Contact getContact(String name) {
        return contacts.get(name);
    }

    public HashMap<String, Contact> getAllContacts() {
        return contacts;
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(contacts);
        }
    }

    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            contacts = (HashMap<String, Contact>) inputStream.readObject();
        }
    }

    public boolean removeContact(String name) {
        Contact contact = contacts.get(name);
        if (contact != null) {
            contacts.remove(name);
            return true;
        }
        return false;
    }

}
