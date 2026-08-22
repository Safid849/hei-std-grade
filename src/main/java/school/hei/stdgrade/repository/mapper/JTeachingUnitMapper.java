package school.hei.stdgrade.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.stdgrade.model.TeachingUnit;
import school.hei.stdgrade.repository.model.JTeachingUnit;

@Component
public class JTeachingUnitMapper {

  public TeachingUnit toDomain(JTeachingUnit entity) {
    return new TeachingUnit(
        entity.getId(),
        entity.getCode(),
        entity.getTitle(),
        entity.getSemesterId(),
        entity.getCredits());
  }

  public JTeachingUnit toEntity(TeachingUnit domain) {
    JTeachingUnit entity = new JTeachingUnit();
    entity.setId(domain.id());
    entity.setCode(domain.code());
    entity.setTitle(domain.title());
    entity.setSemesterId(domain.semesterId());
    entity.setCredits(domain.credits());
    return entity;
  }
}
