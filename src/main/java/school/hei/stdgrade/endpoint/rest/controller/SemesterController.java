package school.hei.stdgrade.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.stdgrade.model.Semester;
import school.hei.stdgrade.service.SemesterService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class SemesterController {

  private final SemesterService service;

  @GetMapping("/semesters")
  public List<Semester> getAllSemesters() {
    return service.findAll();
  }

  @PutMapping("/semesters")
  public Semester crupdateSemester(@RequestBody Semester toSave) {
    return service.save(toSave);
  }
}
