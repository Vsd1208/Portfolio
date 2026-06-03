package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private final FileHandler fileHandler;
    private List<Book> books;
    private List<Member> members;

    public Library() {
        this.fileHandler = new FileHandler();
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        books = fileHandler.loadBooks();
        members = fileHandler.loadMembers();
        System.out.println("Loaded " + books.size() + " books and " + members.size() + " members.");
    }

    public boolean addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) {
            System.out.println("A book with this ISBN already exists.");
            return false;
        }
        books.add(book);
        saveAll();
        System.out.println("Book added successfully: " + book.getTitle());
        return true;
    }

    public boolean removeBook(String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found with ISBN: " + isbn);
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("Borrowed books cannot be removed until they are returned.");
            return false;
        }
        books.remove(book);
        saveAll();
        System.out.println("Book removed successfully.");
        return true;
    }

    public boolean registerMember(Member member) {
        if (findMemberById(member.getId()) != null) {
            System.out.println("A member with this ID already exists.");
            return false;
        }
        members.add(member);
        saveAll();
        System.out.println("Member registered successfully: " + member.getName());
        return true;
    }

    public boolean borrowBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        if (member == null) {
            System.out.println("Member not found.");
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed. Use reservation if the member wants to wait.");
            return false;
        }

        String nextReservation = book.getNextReservation();
        if (nextReservation != null && !nextReservation.equals(memberId)) {
            System.out.println("This book is reserved for member " + nextReservation + ".");
            return false;
        }

        book.setAvailable(false);
        book.setBorrowedBy(memberId);
        book.setDueDate(LocalDate.now().plusWeeks(2));
        book.removeReservation(memberId);
        member.borrowBook(isbn);
        saveAll();

        System.out.println("Book borrowed successfully.");
        System.out.println("Due date: " + book.getDueDate());
        return true;
    }

    public boolean returnBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        if (member == null) {
            System.out.println("Member not found.");
            return false;
        }
        if (book.isAvailable() || !memberId.equals(book.getBorrowedBy())) {
            System.out.println("This book is not borrowed by that member.");
            return false;
        }

        long fine = book.calculateFine();
        book.setAvailable(true);
        book.setBorrowedBy(null);
        book.setDueDate(null);
        member.returnBook(isbn);
        saveAll();

        System.out.println("Book returned successfully.");
        System.out.println("Fine due: $" + fine);
        String nextReservation = book.getNextReservation();
        if (nextReservation != null) {
            System.out.println("Reservation notice: member " + nextReservation + " is next in line.");
        }
        return true;
    }

    public boolean reserveBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        if (member == null) {
            System.out.println("Member not found.");
            return false;
        }
        if (book.isAvailable()) {
            System.out.println("Book is available now. Borrow it instead of reserving it.");
            return false;
        }
        if (memberId.equals(book.getBorrowedBy())) {
            System.out.println("The borrowing member cannot reserve the same book.");
            return false;
        }
        if (book.hasReservation(memberId)) {
            System.out.println("This member already has a reservation for the book.");
            return false;
        }

        book.reserveFor(memberId);
        saveAll();
        System.out.println("Book reserved successfully.");
        return true;
    }

    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equalsIgnoreCase(isbn))
                .findFirst()
                .orElse(null);
    }

    public Member findMemberById(String id) {
        return members.stream()
                .filter(member -> member.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchBooks(String keyword) {
        String searchText = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getIsbn().toLowerCase().contains(searchText)
                        || book.getTitle().toLowerCase().contains(searchText)
                        || book.getAuthor().toLowerCase().contains(searchText))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public void displayAllBooks() {
        displayBooks("ALL BOOKS", books);
    }

    public void displayBooks(String heading, List<Book> bookList) {
        if (bookList.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("\n=== " + heading + " ===");
        System.out.println("Total books: " + bookList.size());
        System.out.println("-".repeat(100));
        for (int index = 0; index < bookList.size(); index++) {
            System.out.println((index + 1) + ". " + bookList.get(index));
        }
    }

    public void displayAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }

        System.out.println("\n=== ALL MEMBERS ===");
        System.out.println("Total members: " + members.size());
        System.out.println("-".repeat(80));
        for (int index = 0; index < members.size(); index++) {
            System.out.println((index + 1) + ". " + members.get(index));
        }
    }

    public void displayStatistics() {
        long availableBooks = books.stream().filter(Book::isAvailable).count();
        long borrowedBooks = books.size() - availableBooks;
        long overdueBooks = books.stream().filter(book -> !book.isAvailable() && book.isOverdue()).count();
        long reservations = books.stream().mapToLong(book -> book.getReservationMemberIds().size()).sum();
        long totalFines = books.stream().mapToLong(Book::calculateFine).sum();

        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println("Total Books: " + books.size());
        System.out.println("Available Books: " + availableBooks);
        System.out.println("Borrowed Books: " + borrowedBooks);
        System.out.println("Registered Members: " + members.size());
        System.out.println("Overdue Books: " + overdueBooks);
        System.out.println("Active Reservations: " + reservations);
        System.out.println("Estimated Fines: $" + totalFines);
    }

    public void exportCsv() {
        fileHandler.exportCsv(books, members);
        System.out.println("CSV export completed in the data folder.");
    }

    private void saveAll() {
        fileHandler.saveBooks(books);
        fileHandler.saveMembers(members);
    }
}
