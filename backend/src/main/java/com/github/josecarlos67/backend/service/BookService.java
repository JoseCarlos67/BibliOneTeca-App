package com.github.josecarlos67.backend.service;

import com.github.josecarlos67.backend.model.collection.Book;
import com.github.josecarlos67.backend.repository.BookRepository;
import com.github.josecarlos67.backend.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

  final private BookRepository bookRepository;
  final private BookValidator bookValidator;

  public List<Book> search(String title, String subtitle) {
    Book book = new Book();
    book.setTitle(title);
    book.setSubtitle(subtitle);

    ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    Example<Book> bookExample = Example.of(book, matcher);

    return bookRepository.findAll(bookExample);
  }

  public Optional<Book> getById(UUID id) {
    return bookRepository.findById(id);
  }

  public void save(Book book) {
    bookValidator.validate(book);
    bookRepository.save(book);
  }

  public void update(Book book) throws IllegalAccessException {
    if (book.getId() == null)
      throw new IllegalAccessException("To perform an update, the book must alread be saved in the system.");

    bookValidator.validate(book);

    bookRepository.save(book);
  }

}
