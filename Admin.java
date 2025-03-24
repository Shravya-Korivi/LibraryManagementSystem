//Shravya 23011101135
//Pradeepaa 23011101095
//Vaishnavi 2301110155
package lib_mgmt_sys;

import java.io.*;
import java.util.*;

public class Admin {
    private String password; // Admin password


    // Method to verify admin password
    public boolean verifyPassword(String inputPassword) throws IOException {
        // Verify admin password from adminverify.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("adminverify.txt"))) {
            String storedPassword = reader.readLine().trim();
            return storedPassword != null && storedPassword.equals(inputPassword);
        }
    }

    // Method to add a new book to the library
    public void addBook(LibraryItem item) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LibraryDetails.txt", true))) {
            writer.write(item.getTitle() + "," + item.getAuthor() + "," + item.getIsbn() + "," + item.getNoOfCopies() + "\n");
            updateLibraryDetails(item.getTitle(), item.getNoOfCopies(), true);
            System.out.println("Book added successfully: " + item.getTitle());
        }
    }

    // Method to remove a book from the library
    public void removeBook(String title) throws IOException {
        List<LibraryItem> items = loadLibraryItems();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LibraryDetails.txt"))) {
            for (LibraryItem item : items) {
                if (item.getTitle().equalsIgnoreCase(title)) {
                    found = true; // Skip writing this item to remove it
                    System.out.println("Book removed successfully: " + title);
                } else {
                    writer.write(item.getTitle() + "," + item.getAuthor() + "," + item.getIsbn() + "," + item.getNoOfCopies() + "\n");
                }
            }
        }

        if (!found) {
            System.out.println("Book not found: " + title);
        }
    }

    // Method to update the LibraryDetails.txt file
    private void updateLibraryDetails(String title, int noOfCopies, boolean isAdd) throws IOException {
        List<LibraryItem> items = loadLibraryItems();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LibraryDetails.txt"))) {
            for (LibraryItem item : items) {
                if (item.getTitle().equalsIgnoreCase(title)) {
                    if (isAdd) {
                        item.setNoOfCopies(item.getNoOfCopies() + noOfCopies); // Increment copy count
                    } else {
                        item.setNoOfCopies(item.getNoOfCopies() - noOfCopies); // Decrement copy count
                    }
                }
                writer.write(item.getTitle() + "," + item.getAuthor() + "," + item.getIsbn() + "," + item.getNoOfCopies() + "\n");
            }
        }
    }

    // Method to load library items from LibraryItems.txt
   public List<LibraryItem> loadLibraryItems() throws IOException {
        List<LibraryItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("LibraryDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                String author = parts[1];
                String isbn = parts[2];
                int noOfCopies = Integer.parseInt(parts[3]);
                items.add(new Book(title, author, isbn, noOfCopies));
            }
        }
        return items;
    }
}
class ThreadedUserOperations {
    public static void main(String[] var0) {
        // Creating User instances
        User var1 = new User("Alice", "password1", "student");
        User var2 = new User("Bob", "password2", "professor");
        User var3 = new User("Charlie", "password3", "student");

        // Thread for User 1 (Alice)
        Thread var4 = new Thread(() -> {
            try {
                var1.borrowBook("Effective Java");
                var1.returnBook("Effective Java");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread for User 2 (Bob)
        Thread var5 = new Thread(() -> {
            try {
                var2.borrowBook("Design Patterns");
                var2.returnBook("Design Patterns");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Thread for User 3 (Charlie)
        Thread var6 = new Thread(() -> {
            try {
                var3.borrowBook("Clean Code");
                var3.returnBook("Clean Code");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Start all threads
        var4.start();
        var5.start();
        var6.start();

        // Wait for all threads to complete
        try {
            var4.join();
            var5.join();
            var6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All borrowing and returning operations completed.");
    }
}
