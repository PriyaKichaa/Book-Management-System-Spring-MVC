package com.cts.lms.entity;

import com.cts.lms.utils.IsbnFormatter;
import com.cts.lms.utils.YearAttributeConverter;
import com.cts.lms.utils.YearDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long bookId;

    @NotBlank(message = "Title cannot be empty")
    @Size(max=255, message = "Title cannot exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max=100, message = "Author cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String author;

    @NotBlank(message = "Genre is required")
    @Column(nullable = false)
    private String genre;

    @NotBlank(message = "ISBN is required")
    @Column(unique = true, nullable = false, length = 17)
    private String isbn;

    @Column(nullable = false)
    @PastOrPresent(message = "Year must not be in the future")
    @JsonDeserialize(using = YearDeserializer.class)
    @Convert(converter = YearAttributeConverter.class)
    private Year yearPublished;

    @Min(value=0, message = "Available copies cannot be negative")
    @Column(nullable = false)
    private int availableCopies;

    @PrePersist
    @PreUpdate
    private void validateAndFormatFields() {
        List<String> errors = new ArrayList<>();

        String cleanIsbn = isbn.replaceAll("[^0-9]", "");
        if (!(cleanIsbn.length() == 10 || cleanIsbn.length() == 13)) {
            errors.add("ISBN must be exactly 10 or 13 digits (numbers only).");
        } else {
            this.isbn = IsbnFormatter.formatIsbn(cleanIsbn);
        }

        if (yearPublished.isBefore(Year.of(1450))) {
            errors.add("Year must be 1450 or later.");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(" | ", errors));
        }
    }
}