//Pradeepaa 23011101095
//Vaishnavi 2301110155
//Shravya 23011101135

package lib_mgmt_sys;

import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.lang.Exception;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;

public class User {
    private String username;
    private String password;
    private String userType; // "student" or "professor"
    private List<BorrowedBook> borrowedBooks;
    private static final int MAX_BOOKS_STUDENT = 4;
    private static final int MAX_BOOKS_PROFESSOR = 6; // Adjusted for staff

    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.borrowedBooks = new ArrayList<>();
        try {
            loadBorrowedBooks();
        } catch (Exception e) {
            System.out.println("Error loading borrowed books: " + e.getMessage());
        }
    }

    // Verify user credentials
    public boolean verifyUser(String username, String password) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("UserBorrow.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)){
                    if(parts[1].equals(password)) {
                        this.userType = parts[2];
                        return true; // User verified
                    } else {
                        System.out.println("Invalid password");
                        return false;
                    }
                }
            }
            System.out.println("User not found. Would you like to add this user? (Y/N): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("Y")) {
                addNewUser(username, password, userType); // Call method to add new user
                System.out.println("User added successfully.");
            } else {
                return false;
            }
        } catch(Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // Method to borrow a book if the user has not exceeded their borrow limit
    public void borrowBook(String title) throws Exception {
        if (canBorrowMoreBooks()) {
            // Check if the book is available
            if (isBookAvailable(title)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                BorrowedBook borrowedBook = new BorrowedBook(title, currentDate); // Set check-in date
                borrowedBooks.add(borrowedBook); // Add to the list of borrowed books
                updateLibraryDetailsOnBorrow(title);
                updateUserBorrowDetailsOnBorrow(borrowedBook);
                System.out.println("Book borrowed successfully: " + title);
            }
        } else {
            throw new BorrowLimitExceededException("Borrow limit exceeded for user type: " + userType);
        }
    }

    // Method to display all the books borrowed by the user
    public void listBorrowedBooks() throws Exception {
        System.out.println("The books borrowed are: ");
        for (BorrowedBook book : borrowedBooks) {
            System.out.println("- " + book.getTitle());
        }
    }

    // Method to return a borrowed book and calculate any overdue fine
    public void returnBook(String title) throws Exception {
        boolean found = false;
        for (BorrowedBook book : borrowedBooks) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                double fine = calculateFine(book);
                borrowedBooks.remove(book); // Remove from borrowed books list
                updateLibraryDetailsOnReturn(title); // Update library inventory
                updateUserBorrowDetailsOnReturn(title); // Update user record
                System.out.println("Book returned successfully: " + title + ". Fine: " + fine + " INR");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("You have not borrowed this book: " + title);
        }
    }

    // Method to calculate any overdue fine based on user type and borrowing period
    private double calculateFine(BorrowedBook book) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate borrowedDate = LocalDate.parse(book.getCheckin(), formatter);
        LocalDate currentDate = LocalDate.now(); // Get the current date
        Period period = Period.between(borrowedDate, currentDate);
        long daysBorrowed = period.toTotalMonths() * 30 + period.getDays(); // Convert period to days

        int maxBorrowDays = userType.equalsIgnoreCase("student") ? 30 : 45;

        if (daysBorrowed > maxBorrowDays) {
            int overdueDays = (int) (daysBorrowed - maxBorrowDays + 1);
            return userType.equalsIgnoreCase("student") ? (10 * overdueDays) : (15 * overdueDays);
        }
        
        return 0; // No fine
    }

    // Method to update library details when a book is returned
    private void updateLibraryDetailsOnReturn(String title) throws Exception {
        List<LibraryItem> items = loadLibraryItems();
        boolean bookFound = false;

        List<LibraryItem> updatedItems = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.setNoOfCopies(item.getNoOfCopies() + 1); // Increase the copy count
                bookFound = true;
            }
            updatedItems.add(item);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LibraryDetails.txt"))) {
            for (LibraryItem item : updatedItems) {
                writer.write(item.getTitle() + "," + item.getAuthor() + "," + item.getIsbn() + "," + item.getNoOfCopies() + "\n");
            }
        }

        if (!bookFound) {
            System.out.println("No record found for the book: " + title);
        }
    }

    // Method to check if the user can borrow more books based on their user type
    private boolean canBorrowMoreBooks() {
        int maxBooks = userType.equalsIgnoreCase("student") ? MAX_BOOKS_STUDENT : MAX_BOOKS_PROFESSOR;
        System.out.println("Max Books Allowed: " + maxBooks);
        System.out.println("Currently Borrowed Books: " + borrowedBooks.size());
        return borrowedBooks.size() < maxBooks;
    }

    // Method to check if a specific book is available for borrowing
    private boolean isBookAvailable(String title) throws Exception {
        List<LibraryItem> items = loadLibraryItems();
        boolean found = false, borrow = false;
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) { 
                found = true;
                if(item.getNoOfCopies() > 0) {
                    borrow = true;
                } else {
                    throw new AllCopiesBorrowedException("All copies of this book are currently borrowed.");
                }
            }
        }
        if(!found) throw new BookNotAvailableException("Book not in library. Will add it soon.");

        for (BorrowedBook book : borrowedBooks) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                throw new Exception("Already borrowed!");
            }
        }
        return borrow;
    }
     
    // Method to load borrowed books of a user from a file
    private void loadBorrowedBooks() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("UserBorrow.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    for (int i = 3; i < parts.length; i += 2) {
                        if(i + 1 < parts.length) {
                            String bookTitle = parts[i];
                            String borrowedDate = parts[i + 1];
                            borrowedBooks.add(new BorrowedBook(bookTitle, borrowedDate));
                        }
                    }
                    break;
                }
            }
        }
    }

    // Method to update user borrowing details when a book is borrowed
    private void updateUserBorrowDetailsOnBorrow(BorrowedBook borrowedBook) throws Exception {
        List<String> updatedBorrowDetails = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("UserBorrow.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    found = true;
                    StringBuilder updatedLine = new StringBuilder(parts[0] + "," + parts[1] + "," + parts[2]);
                    for (int i = 3; i <= parts.length; i += 2) {
                        if (i + 1 < parts.length) {
                            updatedLine.append(",").append(parts[i]).append(",").append(parts[i + 1]);
                        }
                    }
                    updatedLine.append(",").append(borrowedBook.getTitle()).append(",").append(borrowedBook.getCheckin()).append(",");
                    updatedBorrowDetails.add(updatedLine.toString());
                } else {
                    updatedBorrowDetails.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserBorrow.txt"))) {
            for (String detail : updatedBorrowDetails) {
                writer.write(detail + "\n");
            }
            if (!found) {
                writer.write(username + "," + password + "," + borrowedBook.getTitle() + "," + borrowedBook.getCheckin() + "\n");
            }
        }
    }

    // Method to update user borrowing details when a book is returned
    private void updateUserBorrowDetailsOnReturn(String title) throws Exception {
        List<String> updatedBorrowDetails = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("UserBorrow.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    found = true;
                    StringBuilder updatedLine = new StringBuilder(parts[0] + "," + parts[1] + "," + parts[2]);
                    for (int i = 3; i < parts.length; i += 2) {
                        if (i + 1 < parts.length && !parts[i].equalsIgnoreCase(title)) {
                            updatedLine.append(",").append(parts[i]).append(",").append(parts[i + 1]);
                        }
                    }
                    updatedBorrowDetails.add(updatedLine.toString());
                } else {
                    updatedBorrowDetails.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserBorrow.txt"))) {
            for (String detail : updatedBorrowDetails) {
                writer.write(detail + "\n");
            }
            if (!found) {
                writer.write(username + "," + password + "," + userType + "\n");
            }
        }
    }

     // Helper method to load library items from the file
     private List<LibraryItem> loadLibraryItems() throws Exception {
        List<LibraryItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("LibraryDetails.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    items.add(new LibraryItem(parts[0], parts[1], parts[2], Integer.parseInt(parts[3])));
                }
            }
        }
        return items;
    }

    // Helper method to update library details when a book is borrowed
    private void updateLibraryDetailsOnBorrow(String title) throws Exception {
        List<LibraryItem> items = loadLibraryItems();
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.setNoOfCopies(item.getNoOfCopies() - 1); // Decrease the copy count
                break; // Book found and updated
            }
        }

        // Write back updated details to the library file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("LibraryDetails.txt"))) {
            for (LibraryItem item : items) {
                writer.write(item.getTitle() + "," + item.getAuthor() + "," + item.getIsbn() + "," + item.getNoOfCopies() + "\n");
            }
        }
    }
    
    public void addNewUser(String username, String password, String userType) throws Exception {
        // Validate input parameters
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (userType == null || !(userType.equalsIgnoreCase("student") || 
            userType.equalsIgnoreCase("professor"))) {
            throw new IllegalArgumentException("User type must be either 'student' or 'professor'");
        }

        // Check if user already exists
        try (BufferedReader reader = new BufferedReader(new FileReader("UserBorrow.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    throw new IllegalArgumentException("Username already exists");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        // Add new user to UserBorrow.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserBorrow.txt", true))) {
            // Write the new user details (username and password only initially, no borrowed books)
            writer.write("\n"+username + "," + password + ","+ userType);
        }

    }
    

    // Getters
    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    // Inner class to represent a borrowed book
    private class BorrowedBook {
        private String title;
        private String checkin; // Date of borrowing

        public BorrowedBook(String title, String checkin) {
            this.title = title;
            this.checkin = checkin;
        }

        public String getTitle() {
            return title;
        }

        public String getCheckin() {
            return checkin;
        }

    }

}
