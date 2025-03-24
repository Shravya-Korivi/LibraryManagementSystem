//Vaishnavi 23011101155

package lib_mgmt_sys;

// Custom exception to handle cases where a user tries to borrow more items than allowed
public class BorrowLimitExceededException extends Exception {
    
    // Constructor for BorrowLimitExceededException that accepts a custom error message
    public BorrowLimitExceededException(String message) {
        // Passes the custom error message to the superclass (Exception) constructor
        super(message);
    }
}