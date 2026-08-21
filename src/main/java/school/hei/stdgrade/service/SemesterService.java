package school.hei.stdgrade.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.stdgrade.model.Semester;
import school.hei.stdgrade.repository.JSemesterRepository;
import school.hei.stdgrade.repository.mapper.JSemesterMapper;
import school.hei.stdgrade.repository.model.JSemester;
import school.hei.stdgrade.service.validator.CrupdateSemesterValidator;

@Service
@AllArgsConstructor
public class SemesterService {

  private final JSemesterRepository jRepository;
  private final JSemesterMapper jMapper;
  private final CrupdateSemesterValidator crupdateValidator;

  public List<Semester> findAll() {
    return jRepository.findAll().stream().map(jMapper::toDomain).toList();
  }

  public Semester getById(String id) {
    return jRepository
        .findById(id)
        .map(jMapper::toDomain)
        .orElseThrow(() -> new NoSuchElementException("Semester(id=" + id + ") not found"));
  }

  public Semester save(Semester toSave) {
    var existingByCode = jRepository.findByCode(toSave.code());
    var existingByPosition = jRepository.findByPosition(toSave.position());

    String targetId = toSave.id();
    if (existingByCode.isPresent()) {
      targetId = existingByCode.get().getId();
    } else if (existingByPosition.isPresent()) {
      targetId = existingByPosition.get().getId();
    }

    Semester semesterToValidate = toSave.toBuilder().id(targetId).build();

    crupdateValidator.accept(semesterToValidate);

    JSemester entityToSave =
        jRepository.findById(targetId).orElseGet(() -> jMapper.toEntity(semesterToValidate));

    entityToSave.setCode(semesterToValidate.code());
    entityToSave.setPosition(semesterToValidate.position());
    entityToSave.setTotalCredits(semesterToValidate.totalCredits());

    return jMapper.toDomain(jRepository.save(entityToSave));
  }
}
