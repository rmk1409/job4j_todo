package ru.job4j.hql;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class JobDatabase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany
    private List<Job> jobs = new ArrayList<>();

    public static JobDatabase of(String name) {
        JobDatabase database = new JobDatabase();
        database.name = name;
        return database;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    @Override
    public String toString() {
        return "JobDatabase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobs=" + jobs +
                '}';
    }
}
