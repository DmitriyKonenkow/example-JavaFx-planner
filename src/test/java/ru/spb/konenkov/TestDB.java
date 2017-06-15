package ru.spb.konenkov;

import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by konenkov on 10/14/2016.
 */
@Ignore
public class TestDB {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeClass
    public static void setUpTest() {
        entityManagerFactory = Persistence.createEntityManagerFactory("plannerDBtest");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterClass
    public static void tearDownClass() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Before
    public void beforeTest() {
        entityManager.getTransaction().begin();
    }

    @After
    public void afterTest() {
        entityManager.getTransaction().commit();
    }

    @Test
    public void dbTest() {
        System.out.println("asd");
    }
}
