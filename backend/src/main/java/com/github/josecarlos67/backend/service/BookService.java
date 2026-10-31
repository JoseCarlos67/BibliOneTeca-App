package com.github.josecarlos67.backend.service;

import com.github.josecarlos67.backend.model.collection.Book;
import com.github.josecarlos67.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

  final private BookRepository bookRepository;

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

}
