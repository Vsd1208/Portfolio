package library;

import java.time.Year;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner;
    private final Library library;

    public Main() {
        this.scanner = new Scanner(System.in);
        this.library = new Library();
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ", 1, 11);

            switch (choice) {
                case 1 -> addBook();
                case 2 -> library.displayAllBooks();
                case 3 -> searchBooks();
                case 4 -> removeBook();
                case 5 -> registerMember();
                case 6 -> library.displayAllMembers();
                case 7 -> borrowBook();
                case 8 -> returnBook();
                case 9 -> reserveBook();
                case 10 -> library.displayStatistics();
                case 11 -> {
                    library.exportCsv();
                    running = false;
                    System.out.println("Thank you for using the Library Management System.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
        System.out.println("1. Add New Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Books");
        System.out.println("4. Remove Book");
        System.out.println("5. Register Member");
        System.out.println("6. View All Members");
        System.out.println("7. Borrow Book");
        System.out.println("8. Return Book");
        System.out.println("9. Reserve Book");
        System.out.println("10. View Library Statistics");
        System.out.println("11. Export CSV and Exit");
    }

    private void addBook() {
        String isbn = readRequiredText("ISBN: ");
        String title = readRequiredText("Title: ");
        String author = readRequiredText("Author: ");
        int currentYear = Year.now().getValue();
        int year = readInt("Publication year: ", 1000, currentYear);
        library.addBook(new Book(isbn, title, author, year));
    }

    private void removeBook() {
        String isbn = readRequiredText("ISBN to remove: ");
        library.removeBook(isbn);
    }

    private void searchBooks() {
        String keyword = readRequiredText("Search by ISBN, title, or author: ");
        List<Book> results = library.searchBooks(keyword);
        library.displayBooks("SEARCH RESULTS", results);
    }

    private void registerMember() {
        String id = readRequiredText("Member ID: ").toUpperCase();
        String name = readRequiredText("Member name: ");
        library.registerMember(new Member(id, name));
    }

    private void borrowBook() {
        String isbn = readRequiredText("ISBN to borrow: ");
        String memberId = readRequiredText("Member ID: ").toUpperCase();
        library.borrowBook(isbn, memberId);
    }

    private void returnBook() {
        String isbn = readRequiredText("ISBN to return: ");
        String memberId = readRequiredText("Member ID: ").toUpperCase();
        library.returnBook(isbn, memberId);
    }

    private void reserveBook() {
        String isbn = readRequiredText("ISBN to reserve: ");
        String memberId = readRequiredText("Member ID: ").toUpperCase();
        library.reserveBook(isbn, memberId);
    }

    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isBlank() && !value.contains("|")) {
                return value;
            }
            System.out.println("Please enter a non-empty value without the | character.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("Enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}
