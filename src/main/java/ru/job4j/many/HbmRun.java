package ru.job4j.many;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Brand ferarri = Brand.of("Ferarri");
            ferarri.getModels().add(Model.of("model1"));
            ferarri.addModel(Model.of("model2"));
            ferarri.addModel(Model.of("model3"));
            ferarri.addModel(Model.of("model4"));
            ferarri.addModel(Model.of("model5"));
            session.save(ferarri);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
