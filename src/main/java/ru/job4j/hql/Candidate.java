package ru.job4j.hql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String experience;
    private int salary;

    public static Candidate of(String name, String experience, int salary) {
        Candidate candidate = new Candidate();
        candidate.name = name;
        candidate.experience = experience;
        candidate.salary = salary;
        return candidate;
    }

    public static Candidate of(int id, String name, String experience, int salary) {
        Candidate candidate = of(name, experience, salary);
        candidate.id = id;
        return candidate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExperience() {
        return experience;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", experience='" + experience + '\'' +
                ", salary=" + salary +
                '}';
    }
}
