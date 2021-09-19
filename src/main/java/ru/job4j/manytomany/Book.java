package ru.job4j.manytomany;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Author> authors = new ArrayList<>();

    public static Book of(String name) {
        Book book = new Book();
        book.name = name;
        return book;
    }

    public List<Author> getAuthors() {
        return authors;
    }
}
