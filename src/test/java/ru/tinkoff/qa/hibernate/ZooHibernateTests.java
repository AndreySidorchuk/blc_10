package ru.tinkoff.qa.hibernate;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.tinkoff.qa.BeforeCreator;

import java.util.List;

public class ZooHibernateTests {

    private static SessionFactory sessionFactory = null;
    private Session session = null;

    @BeforeAll
    static void init() {
        BeforeCreator.createData();
    }

    @BeforeEach
    void setupThis() {
        sessionFactory = HibernateSessionFactoryCreator.createSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterAll
    static void tear() {
        sessionFactory.close();
    }

    /**
     * В таблице public.animal ровно 10 записей
     */
    @Test
    public void countRowAnimal() {
        List<Integer> list = session.createQuery("SELECT a.id FROM Animal a", Integer.class).getResultList();
        Assertions.assertEquals(10, list.size());
    }

    /**
     * В таблицу public.animal нельзя добавить строку с индексом от 1 до 10 включительно
     */
    @Test
    public void insertIndexAnimal() {
        for (int i = 1; i <= 10; i++) {
            Animal animal = new Animal();
            animal.setId(i);
            animal.setName("Соня");
            animal.setAge(3);
            animal.setType(2);
            animal.setSex(2);
            animal.setPlace(2);
            session.save(animal);
            try {
                session.getTransaction().commit();
            } catch (IllegalStateException | PersistenceException e) {
                if (e.getClass() != PersistenceException.class) {
                    Assertions.assertTrue(e.getMessage().contains("Transaction not successfully started"));
                } else {
                    Assertions.assertTrue(e.getMessage().contains("Converting `org.hibernate.exception.ConstraintViolationException` to JPA"));
                }
            }
        }
    }

    /**
     * В таблицу public.workman нельзя добавить строку с name = null
     */
    @Test
    public void insertNullToWorkman() {
        Workman workman = new Workman();
        workman.setId(11);
        workman.setName(null);
        workman.setAge(2);
        workman.setPosition(2);
        session.save(workman);
        PersistenceException exception = Assertions.assertThrows(PersistenceException.class, () -> {
            session.getTransaction().commit();
        });
        Assertions.assertTrue(exception.getMessage().contains("Converting `org.hibernate.exception.ConstraintViolationException` to JPA"));
    }

    /**
     * Если в таблицу public.places добавить еще одну строку, то в ней будет 6 строк
     */
    @Test
    public void insertPlacesCountRow() {
        Places places = new Places();
        places.setId(6);
        places.setRow(2);
        places.setPlaceNum(2);
        places.setName("Орехово");
        session.save(places);
        session.getTransaction().commit();
        List<Integer> listPlaces = session.createQuery("SELECT p.id FROM Places p", Integer.class).getResultList();
        Assertions.assertEquals(6, listPlaces.size());
    }

    /**
     * В таблице public.zoo всего три записи с name 'Центральный', 'Северный', 'Западный'
     */
    @Test
    public void countRowZoo() {
        List<String> names = session.createQuery("SELECT z.name FROM Zoo z", String.class).getResultList();
        Assertions.assertEquals(3, names.size());
        Assertions.assertEquals(names, List.of("Центральный", "Северный", "Западный"));
    }
}
