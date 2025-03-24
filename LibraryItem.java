//Shravya 23011101135

package lib_mgmt_sys;

public class LibraryItem {
    // Protected attributes accessible within subclasses
    protected String title; // Title of the library item (e.g., book, research paper)
    protected String author; // Author of the library item
    protected String isbn; // ISBN or unique identifier for the item
    protected int noOfCopies; // Number of copies available in the library
    protected int maxBorrowPeriod; // Maximum borrowing period in days for this item
    protected double finePerDay; // Fine to be charged per day for overdue items

    // Constructor to initialize the main details of a library item
    public LibraryItem(String title, String author, String isbn, int noOfCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.noOfCopies = noOfCopies;
    }

    // Getters and Setters for accessing and modifying item details

    public String getTitle() { return title; } // Returns the title of the item
    public String getAuthor() { return author; } // Returns the author of the item
    public String getIsbn() { return isbn; } // Returns the ISBN of the item
    public int getNoOfCopies() { return noOfCopies; } // Returns the number of copies available
    public void setNoOfCopies(int noOfCopies) { this.noOfCopies = noOfCopies; } // Sets the number of copies available

}