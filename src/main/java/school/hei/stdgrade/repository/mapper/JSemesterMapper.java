package school.hei.stdgrade.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.stdgrade.model.Semester;
import school.hei.stdgrade.repository.model.JSemester;

@Component
public class JSemesterMapper {

  public Semester toDomain(JSemester entity) {
    return new Semester(
        entity.getId(), entity.getCode(), entity.getPosition(), entity.getTotalCredits());
  }

  public JSemester toEntity(Semester domain) {
    JSemester entity = new JSemester();
    entity.setId(domain.id());
    entity.setCode(domain.code());
    entity.setPosition(domain.position());
    entity.setTotalCredits(domain.totalCredits());
    return entity;
  }
}
