//Pradeepaa 23011101095

package lib_mgmt_sys;

// Represents a book item in the library, extending the LibraryItem class
public class Book extends LibraryItem {
    
    // Constructor to initialize a Book object with title, author, ISBN, and number of copies
    public Book(String title, String author, String isbn, int noOfCopies) {
        // Calls the superclass (LibraryItem) constructor to set the common attributes
        super(title, author, isbn, noOfCopies);
    }

    // Method to display details of the book including title, author, ISBN, and available copies
    public void displayDetails() {
        System.out.println("Book Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Available Copies: " + noOfCopies);
    }
}