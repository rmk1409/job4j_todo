package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Store {
    private final static Logger LOGGER = Logger.getLogger(Store.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private Store() {
    }

    public static Store getInstance() {
        return Holder.INSTANCE;
    }

    public User findUserByEmail(String email) {
        return (User) tx(session -> {
            String hql = "from User where email =: email";
            Object user = null;
            try {
                user = session.createQuery(hql)
                        .setParameter("email", email)
                        .getSingleResult();
            } catch (NoResultException e) {
                LOGGER.log(Level.SEVERE, "An exception was thrown", e);
            }
            return user;
        });
    }

    private static final class Holder {
        private static final Store INSTANCE = new Store();

    }

    public User save(User user) {
        return tx(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    public List<Item> findAllItems() {
        return tx(session -> session.createQuery("from Item").list());
    }

    public Item saveOrUpdate(Item item) {
        return tx(session -> {
            session.saveOrUpdate(item);
            return item;
        });
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
