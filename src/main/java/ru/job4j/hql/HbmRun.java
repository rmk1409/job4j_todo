package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        Candidate rsl = null;
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            rsl = session.createQuery(
                            "select distinct c from Candidate c "
                                    + "join fetch c.jobDatabase jDB "
                                    + "join fetch jDB.jobs j "
                                    + "where c.id = :id", Candidate.class
                    ).setParameter("id", 1)
                    .uniqueResult();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        System.out.println(rsl);
    }

    public static void save(Session session) {
        session.save(Candidate.of("name1", "exp1", 100));
        session.save(Candidate.of("name2", "exp2", 200));
        session.save(Candidate.of("name3", "exp3", 300));
    }

    public static List<Candidate> getAll(Session session) {
        return session.createQuery("from Candidate").list();
    }

    public static Candidate getById(Session session, int id) {
        return session.get(Candidate.class, id);
    }

    public static List<Candidate> getByName(Session session, String name) {
        return session.createQuery("from Candidate where name = :name", Candidate.class)
                .setParameter("name", name)
                .list();
    }

    public static void update(Session session, Candidate candidate) {
        session.createQuery("" +
                        "update Candidate c " +
                        "set c.name = :name, c.experience = :experience, c.salary = :salary " +
                        "where c.id = :id")
                .setParameter("name", candidate.getName())
                .setParameter("experience", candidate.getExperience())
                .setParameter("salary", candidate.getSalary())
                .setParameter("id", candidate.getId())
                .executeUpdate();
    }

    public static void delete(Session session, int id) {
        session.createQuery("" +
                        "delete from Candidate " +
                        "where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
