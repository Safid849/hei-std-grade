package school.hei.stdgrade.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import school.hei.stdgrade.repository.model.JSemester;

public interface JSemesterRepository extends JpaRepository<JSemester, String> {
  Optional<JSemester> findByCode(String code);

  Optional<JSemester> findByPosition(int position);

  boolean existsByCodeAndIdNot(String code, String id);

  boolean existsByPositionAndIdNot(int position, String id);
}
