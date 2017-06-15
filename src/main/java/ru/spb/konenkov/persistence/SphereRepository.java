package ru.spb.konenkov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.spb.konenkov.entity.Sphere;

/**
 * Created by dmko1016 on 27.12.2016.
 */
public interface SphereRepository extends CrudRepository<Sphere, Long> {

    Iterable<Sphere> findAllByOrderByNumberRowAsc();
    Iterable<Sphere> findAllByOrderByNumberRowDesc();

}
