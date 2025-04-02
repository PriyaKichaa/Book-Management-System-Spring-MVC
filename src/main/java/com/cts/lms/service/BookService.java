package com.cts.lms.service;

import com.cts.lms.entity.Book;
import com.cts.lms.exception.ResourceNotFoundException;
import com.cts.lms.repository.BookRepository;
import com.cts.lms.utils.IsbnFormatter;
import com.cts.lms.utils.ResetBookId;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final ResetBookId resetBookId;

    public BookService(BookRepository bookRepository, ResetBookId resetBookId) {
        this.bookRepository = bookRepository;
        this.resetBookId = resetBookId;
    }

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()) {
            throw new ResourceNotFoundException("No books to view list");
        }
        return books;
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID " + bookId + " not found"));
    }

    public Book addBook(Book book) {
        validateBook(book);

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("Given ISBN exists. Please enter a different ISBN.");
        }

        return bookRepository.save(book);
    }

    public Book modifyBook(Long bookId, Book bookDetails) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID " + bookId + " not found"));

        if (bookDetails.getTitle() != null) book.setTitle(bookDetails.getTitle());
        if (bookDetails.getAuthor() != null) book.setAuthor(bookDetails.getAuthor());
        if (bookDetails.getGenre() != null) book.setGenre(bookDetails.getGenre());
        if (bookDetails.getIsbn() != null) book.setIsbn(IsbnFormatter.formatIsbn(bookDetails.getIsbn()));
        if (bookDetails.getYearPublished() != null) book.setYearPublished(bookDetails.getYearPublished());
        if (bookDetails.getAvailableCopies() >= 0) {
            book.setAvailableCopies(bookDetails.getAvailableCopies());
        }

        return bookRepository.save(book);
    }

    public String deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book ID " + bookId + " not found"));
    
        bookRepository.delete(book);
    
        if (bookRepository.count() == 0) {
            resetBookId.resetAutoIncrement();
        }
    
        return "Book with ID " + bookId + " has been deleted.\nDeleted Book Details:\n" +
               "Title: " + book.getTitle() + "\n" +
               "Author: " + book.getAuthor() + "\n" +
               "Genre: " + book.getGenre() + "\n" +
               "ISBN: " + book.getIsbn() + "\n" +
               "Year Published: " + book.getYearPublished() + "\n" +
               "Available Copies: " + book.getAvailableCopies();
    }
    

    public List<Book> searchBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found by author: " + author);
        }
        return books;
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found with title: " + title);
        }
        return books;
    }

    public List<Book> searchBooksByGenre(String genre) {
        List<Book> books = bookRepository.findByGenreContainingIgnoreCase(genre);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found in genre: " + genre);
        }
        return books;
    }

    private void validateBook(Book book) {
        List<String> errors = new ArrayList<>();
    
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            errors.add("Title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            errors.add("Author is required");
        }
        if (book.getGenre() == null || book.getGenre().trim().isEmpty()) {
            errors.add("Genre is required");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            errors.add("ISBN is required");
        } else if (!book.getIsbn().matches("^[0-9]{10,13}$")) {
            errors.add("Invalid ISBN format. It must be 10 or 13 digits long.");
        }
        if (book.getYearPublished() == null || book.getYearPublished().getValue() < 0) {
            errors.add("Year of publication must be a valid positive number.");
        }
        if (book.getAvailableCopies() < 0) {
            errors.add("Invalid number of available copies");
        }
    
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("/n", errors));
        }
    }
    
}
