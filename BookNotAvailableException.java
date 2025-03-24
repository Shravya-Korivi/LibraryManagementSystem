//Shravya 23011101135

package lib_mgmt_sys;

// Custom exception class to handle cases when a requested book is not available in the library
public class BookNotAvailableException extends Exception {
    
    // Constructor for BookNotAvailableException that accepts a custom error message
    public BookNotAvailableException(String message) {
        // Passes the custom error message to the superclass (Exception) constructor
        super(message);
    }
    
}