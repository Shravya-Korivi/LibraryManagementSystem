//Vaishnavi 2301110155
//Pradeepaa 23011101095
//Shravya 23011101135
package lib_mgmt_sys;

import java.io.IOException;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Library Management System!");
        System.out.print("Are you an Admin or User? (A/U): ");
        String role = scanner.nextLine();

        // Admin section
        if (role.equalsIgnoreCase("A")) {
            Admin admin = new Admin(); // Create an instance of Admin
            System.out.print("Enter admin password: ");
            String inputPassword = scanner.nextLine();

            try {
                // Verify the admin password
                if (admin.verifyPassword(inputPassword)) {
                    System.out.println("Admin logged in successfully.");
                    boolean continueAdmin = true;
                    while (continueAdmin) {
                        // Admin options menu
                        System.out.println("\nAdmin Options:");
                        System.out.println("1. Add Book");
                        System.out.println("2. Remove Book");
                        System.out.println("3. Exit");
                        System.out.print("Choose an option: ");
                        int option = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Switch for admin actions
                        switch (option) {
                            case 1: // Add a book
                                System.out.print("Enter book title: ");
                                String title = scanner.nextLine();
                                System.out.print("Enter book author: ");
                                String author = scanner.nextLine();
                                System.out.print("Enter book ISBN: ");
                                String isbn = scanner.nextLine();
                                System.out.print("Enter number of copies: ");
                                int noOfCopies = scanner.nextInt();
                                LibraryItem l1 = new Book(title, author, isbn, noOfCopies);
                                admin.addBook(l1); // Call method to add book
                                break;
                            case 2: // Remove a book
                                System.out.print("Enter book title to remove: ");
                                String removeTitle = scanner.nextLine();
                                try {
                                    admin.removeBook(removeTitle); // Call method to remove book
                                } catch (Exception e) {
                                    System.out.println("Book not Found!");
                                }                                
                                break;
                            case 3: // Exit admin mode
                                continueAdmin = false;
                                break;
                            default:
                                System.out.println("Invalid option. Please try again.");
                        }
                    }
                } else {
                    System.out.println("Invalid password.");
                }
            } catch (IOException e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        } 
        // User section
        else if (role.equalsIgnoreCase("U")) {
            // User login process
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter user type (student/professor): ");
            String userType = scanner.nextLine();

            User user = new User(username, password, userType); // Create a user instance

            try {
                // Verify the user
                if (user.verifyUser(username, password)) {
                    System.out.println("User logged in successfully.");
                    boolean continueUser = true;
                    while (continueUser) {
                        // User options menu
                        System.out.println("\nUser Options:");
                        System.out.println("1. Borrow Book");
                        System.out.println("2. Return Book");
                        System.out.println("3. View borrowed books");
                        System.out.println("4. Exit");
                        System.out.print("Choose an option: ");
                        int option = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Switch for user actions
                        switch (option) {
                            case 1: // Borrow a book
                                System.out.print("Enter the title of the book to borrow: ");
                                String bookTitle = scanner.nextLine();
                                try {
                                    user.borrowBook(bookTitle); // Call method to borrow book
                                } catch (IOException e) {
                                    System.out.println("Database Error: Unable to access library records.");
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());}
                                break;
                            case 2: // Return a book
                                System.out.print("Enter the title of the book to return: ");
                                String returnTitle = scanner.nextLine();
                                user.returnBook(returnTitle); // Call method to return book
                                break;
                            case 3:// Dislay all borrowed books by the user
                                try{
                                    user.listBorrowedBooks();
                                }
                                catch(Exception e){
                                    System.out.println("Error: " + e.getMessage());}
                                    break;
                                
                            case 4: // Exit user mode
                                continueUser = false;
                                break;
                            default:
                                System.out.println("Invalid option. Please try again.");
                        }
                    }
                }else {
                    System.out.println("Login cancelled");
                }
        }
            catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
        else {
            System.out.println("Invalid role. Please enter 'A' for Admin or 'U' for User.");
        }

        scanner.close();
        System.out.println("Thank you for using the SNUC Library!");
    }
}
