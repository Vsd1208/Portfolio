package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int year;
    private boolean available;
    private String borrowedBy;
    private LocalDate dueDate;
    private final List<String> reservationMemberIds;

    public Book(String isbn, String title, String author, int year) {
        this(isbn, title, author, year, true, null, null, new ArrayList<>());
    }

    public Book(String isbn, String title, String author, int year, boolean available,
                String borrowedBy, LocalDate dueDate, List<String> reservationMemberIds) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = available;
        this.borrowedBy = borrowedBy;
        this.dueDate = dueDate;
        this.reservationMemberIds = new ArrayList<>(reservationMemberIds);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getReservationMemberIds() {
        return Collections.unmodifiableList(reservationMemberIds);
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public boolean hasReservation(String memberId) {
        return reservationMemberIds.contains(memberId);
    }

    public void reserveFor(String memberId) {
        if (!reservationMemberIds.contains(memberId)) {
            reservationMemberIds.add(memberId);
        }
    }

    public String getNextReservation() {
        return reservationMemberIds.isEmpty() ? null : reservationMemberIds.get(0);
    }

    public void removeReservation(String memberId) {
        reservationMemberIds.remove(memberId);
    }

    public long calculateFine() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now()) * 2;
    }

    @Override
    public String toString() {
        String status = available ? "Available" : "Borrowed by: " + borrowedBy + " | Due: " + dueDate;
        String reservations = reservationMemberIds.isEmpty() ? "" : " | Reserved: " + reservationMemberIds.size();
        return String.format("ISBN: %s | Title: %s | Author: %s | Year: %d | %s%s",
                isbn, title, author, year, status, reservations);
    }
}
