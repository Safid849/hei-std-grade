package school.hei.stdgrade.service.validator;

import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.stdgrade.model.Semester;
import school.hei.stdgrade.repository.JSemesterRepository;

@Component
@AllArgsConstructor
public class CrupdateSemesterValidator implements Consumer<Semester> {

  private final JSemesterRepository jSemesterRepository;

  private static final Set<String> VALID_CODES = Set.of("S1", "S2", "S3", "S4", "S5", "S6");

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(Semester semester) {
    var sj = new StringJoiner(". ");

    if (isBlank(semester.id())) {
      sj.add("Id is mandatory");
    }
    if (isBlank(semester.code())) {
      sj.add("Code is mandatory");
    }
    if (semester.position() < 1 || semester.position() > 6) {
      sj.add("Position must be between 1 and 6");
    }
    if (semester.totalCredits() <= 0) {
      sj.add("TotalCredits must be strictly positive");
    }

    if (!isBlank(semester.code()) && !VALID_CODES.contains(semester.code())) {
      sj.add("Code must be one of S1, S2, S3, S4, S5, S6");
    }

    if (!isBlank(semester.id())
        && !isBlank(semester.code())
        && jSemesterRepository.existsByCodeAndIdNot(semester.code(), semester.id())) {
      sj.add("Code already taken");
    }

    if (semester.position() >= 1
        && semester.position() <= 6
        && !isBlank(semester.id())
        && jSemesterRepository.existsByPositionAndIdNot(semester.position(), semester.id())) {
      sj.add("Position already taken");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
