//Pradeepaa 23011101095

package lib_mgmt_sys;

// The Journal class represents a specific type of LibraryItem, inheriting its basic properties and adding specialized functionality
public class Journal extends LibraryItem {
    
    // Constructor for creating a Journal object with the specified title, author, ISBN, and number of copies
    public Journal(String title, String author, String isbn, int noOfCopies) {
        // Calls the superclass (LibraryItem) constructor to initialize the common attributes
        super(title, author, isbn, noOfCopies);
    }

    // Method to display detailed information about the journal
    public void displayDetails() {
        System.out.println("Journal Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Available Copies: " + noOfCopies);
    }
}