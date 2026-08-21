package school.hei.stdgrade.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.stdgrade.model.TeachingUnit;
import school.hei.stdgrade.repository.JTeachingUnitRepository;
import school.hei.stdgrade.repository.mapper.JTeachingUnitMapper;
import school.hei.stdgrade.service.validator.CrupdateTeachingUnitValidator;

@Service
@AllArgsConstructor
public class TeachingUnitService {

  private final JTeachingUnitRepository jRepository;
  private final JTeachingUnitMapper jMapper;
  private final CrupdateTeachingUnitValidator crupdateValidator;

  public List<TeachingUnit> findAll() {
    return jRepository.findAll().stream().map(jMapper::toDomain).toList();
  }

  public TeachingUnit getById(String id) {
    return jRepository
        .findById(id)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("TeachingUnit(id=" + id + ") not found"));
  }

  public TeachingUnit save(TeachingUnit toSave) {
    crupdateValidator.accept(toSave);
    var entity = jMapper.toEntity(toSave);
    return jMapper.toDomain(jRepository.save(entity));
  }
}
