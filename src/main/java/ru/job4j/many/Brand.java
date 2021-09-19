package ru.job4j.many;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Brand {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "brand")
    private final List<Model> models = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static Brand of(String name) {
        Brand brand = new Brand();
        brand.name = name;
        return brand;
    }

    public void addModel(Model model) {
        this.models.add(model);
    }

    public List<Model> getModels() {
        return models;
    }
}
