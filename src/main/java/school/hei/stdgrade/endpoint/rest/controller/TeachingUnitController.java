package school.hei.stdgrade.endpoint.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.stdgrade.model.TeachingUnit;
import school.hei.stdgrade.service.TeachingUnitService;

@RestController
@AllArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class TeachingUnitController {

  private final TeachingUnitService service;

  @GetMapping("/teaching-units")
  public List<TeachingUnit> getAllTeachingUnits() {
    return service.findAll();
  }

  @PutMapping("/teaching-units")
  public TeachingUnit crupdateTeachingUnit(@RequestBody TeachingUnit toSave) {
    return service.save(toSave);
  }
}
