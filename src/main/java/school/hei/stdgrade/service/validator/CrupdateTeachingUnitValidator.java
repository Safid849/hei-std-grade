package school.hei.stdgrade.service.validator;

import java.util.StringJoiner;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.stdgrade.model.TeachingUnit;
import school.hei.stdgrade.repository.JSemesterRepository;
import school.hei.stdgrade.repository.JTeachingUnitRepository;

@Component
@AllArgsConstructor
public class CrupdateTeachingUnitValidator implements Consumer<TeachingUnit> {

  private final JTeachingUnitRepository jTeachingUnitRepository;
  private final JSemesterRepository jSemesterRepository;

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @Override
  public void accept(TeachingUnit teachingUnit) {
    var sj = new StringJoiner(". ");

    if (isBlank(teachingUnit.id())) {
      sj.add("Id is mandatory");
    }
    if (isBlank(teachingUnit.code())) {
      sj.add("Code is mandatory");
    }
    if (isBlank(teachingUnit.title())) {
      sj.add("Title is mandatory");
    }
    if (isBlank(teachingUnit.semesterId())) {
      sj.add("SemesterId is mandatory");
    }
    if (teachingUnit.credits() <= 0) {
      sj.add("Credits must be strictly positive");
    }

    if (!isBlank(teachingUnit.id())
        && !isBlank(teachingUnit.code())
        && jTeachingUnitRepository.existsByCodeAndIdNot(teachingUnit.code(), teachingUnit.id())) {
      sj.add("Code already taken");
    }

    if (!isBlank(teachingUnit.semesterId())
        && !jSemesterRepository.existsById(teachingUnit.semesterId())) {
      sj.add("Semester(id=" + teachingUnit.semesterId() + ") does not exist");
    }

    if (sj.length() > 0) {
      throw new IllegalArgumentException(sj.toString());
    }
  }
}
