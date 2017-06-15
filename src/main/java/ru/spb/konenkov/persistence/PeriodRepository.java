package ru.spb.konenkov.persistence;

import org.springframework.data.repository.CrudRepository;
import ru.spb.konenkov.entity.Period;

/**
 * Created by dmko1016 on 27.12.2016.
 */
public interface PeriodRepository extends CrudRepository<Period, Long>, PeriodRepositoryCustom {
}
