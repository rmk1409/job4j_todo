package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import java.sql.Timestamp;
import java.util.List;

public class Store {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public static void main(String[] args) {
        Store store = new Store();
        store.saveOrUpdate(new Item("descr1", new Timestamp(System.currentTimeMillis()), false));
        store.saveOrUpdate(new Item("descr2", new Timestamp(System.currentTimeMillis()), true));
        store.saveOrUpdate(new Item("descr3", new Timestamp(System.currentTimeMillis()), false));
        store.saveOrUpdate(new Item("descr4", new Timestamp(System.currentTimeMillis()), false));
    }

    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public Item saveOrUpdate(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.saveOrUpdate(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public boolean replace(int id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        item.setId(id);
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }
}
