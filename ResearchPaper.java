//Shravya 23011101135
//Pradeepaa 23011101095
//Vaishnavi 2301110155
package lib_mgmt_sys;

// The ResearchPaper class represents a research paper item in the library management system.
// It extends the LibraryItem class, meaning it inherits its attributes and behaviors.
public class ResearchPaper extends LibraryItem {

    // Constructor to initialize a ResearchPaper object with its title, author, ISBN, and the number of copies available.
    public ResearchPaper(String title, String author, String isbn, int noOfCopies) {
        // Calls the superclass (LibraryItem) constructor to set common library item attributes.
        super(title, author, isbn, noOfCopies);
    }

    // Method to display details of the research paper item.
    // This method overrides any default display behavior to show specific information about research papers.
    public void displayDetails() {
        System.out.println("Research Paper Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Available Copies: " + noOfCopies);
    }
}