package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;

public class Main {

    private static Faker faker = new Faker();

    private static Person randomPerson(){

        Address randomAddress = Address.builder()
                .country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .streetAddress(faker.address().streetAddress())
                .zip(faker.address().zipCode())
                .build();

        return Person.builder()
                .name(faker.name().fullName())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.class))
                .address(randomAddress)
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();
    }

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        for (int i = 0; i<1000; i++){
            em.persist(randomPerson());
        }

        em.getTransaction().commit();
        em.createQuery("SELECT p FROM Person p", Person.class).getResultStream().forEach(System.out::println);

        em.close();
        emf.close();

    }

}
