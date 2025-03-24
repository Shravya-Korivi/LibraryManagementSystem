//Vaishnavi 23011101155

package lib_mgmt_sys;

// Exception class to indicate that all copies of a book have been borrowed
public class AllCopiesBorrowedException extends Exception {
    
    // Constructor to create an instance of AllCopiesBorrowedException with a custom message
    public AllCopiesBorrowedException(String message) {
        super(message); // Passes the message to the superclass (Exception) constructor
    }
}