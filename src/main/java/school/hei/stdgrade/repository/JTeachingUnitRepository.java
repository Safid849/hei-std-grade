package school.hei.stdgrade.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.stdgrade.repository.model.JTeachingUnit;

public interface JTeachingUnitRepository extends JpaRepository<JTeachingUnit, String> {
  Optional<JTeachingUnit> findByCode(String code);

  boolean existsByCodeAndIdNot(String code, String id);
}
