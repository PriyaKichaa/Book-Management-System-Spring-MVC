package com.cts.lms.service;

import com.cts.lms.entity.Book;
import com.cts.lms.exception.ResourceNotFoundException;
import com.cts.lms.repository.BookRepository;
import com.cts.lms.utils.ResetBookId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ResetBookId resetBookId;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setGenre("Programming");
        book.setIsbn("9780134685991");
        book.setYearPublished(Year.of(2018));
        book.setAvailableCopies(5);
    }

    @Test
    void testGetAllBooks_WhenBooksExist() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));

        List<Book> books = bookService.getAllBooks();
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).getTitle());
    }

    @Test
    void testGetAllBooks_WhenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, bookService::getAllBooks);
        assertEquals("No books to view list", exception.getMessage());
    }

    @Test
    void testGetBookById_WhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);
        assertEquals("Effective Java", foundBook.getTitle());
    }

    @Test
    void testGetBookById_WhenBookDoesNotExist() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(2L));
        assertEquals("Book ID 2 not found", exception.getMessage());
    }

    @Test
    void testAddBook_Success() {
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.addBook(book);
        assertEquals("Effective Java", savedBook.getTitle());
    }

    @Test
    void testAddBook_WithDuplicateISBN() {
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
        assertEquals("Given ISBN exists. Please enter a different ISBN.", exception.getMessage());
    }

    @Test
    void testDeleteBook_WhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        String message = bookService.deleteBook(1L);
        assertTrue(message.contains("Book with ID 1 has been deleted."));
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBook_WhenBookDoesNotExist() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(2L));
        assertEquals("Book ID 2 not found", exception.getMessage());
    }
}
