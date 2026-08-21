package school.hei.stdgrade.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static school.hei.stdgrade.conf.TestUtils.ADMIN_EMAIL;
import static school.hei.stdgrade.conf.TestUtils.STUDENT_A_EMAIL;
import static school.hei.stdgrade.conf.TestUtils.TEACHER_A_EMAIL;
import static school.hei.stdgrade.conf.TestUtils.TEACHING_UNITS_URL;
import static school.hei.stdgrade.conf.TestUtils.authHeaders;
import static school.hei.stdgrade.conf.TestUtils.jsonHeaders;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import school.hei.stdgrade.conf.TestcontainersConfigurer;
import school.hei.stdgrade.model.TeachingUnit;

class TeachingUnitRbacIT extends TestcontainersConfigurer {

  @Autowired TestRestTemplate testRestTemplate;

  private String adminToken;
  private String teacherToken;
  private String studentToken;

  @BeforeEach
  void setUp() {
    adminToken = utils.tokenOf(ADMIN_EMAIL);
    teacherToken = utils.tokenOf(TEACHER_A_EMAIL);
    studentToken = utils.tokenOf(STUDENT_A_EMAIL);
  }

  @Test
  void everyone_can_list_teaching_units_without_authentication() {
    var response =
        testRestTemplate.exchange(
            TEACHING_UNITS_URL, GET, new HttpEntity<>(jsonHeaders()), TeachingUnit[].class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void admin_can_crupdate_a_teaching_unit() {
    var toCreate = teachingUnit();

    var response =
        testRestTemplate.exchange(
            TEACHING_UNITS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(adminToken)),
            TeachingUnit.class);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(toCreate.code(), response.getBody().code());
  }

  @Test
  void teacher_cannot_crupdate_a_teaching_unit() {
    var toCreate = teachingUnit();

    var response =
        testRestTemplate.exchange(
            TEACHING_UNITS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(teacherToken)),
            Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void student_cannot_crupdate_a_teaching_unit() {
    var toCreate = teachingUnit();

    var response =
        testRestTemplate.exchange(
            TEACHING_UNITS_URL,
            PUT,
            new HttpEntity<>(toCreate, authHeaders(studentToken)),
            Map.class);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void anonymous_cannot_crupdate_a_teaching_unit() {
    var toCreate = teachingUnit();

    var response =
        testRestTemplate.exchange(
            TEACHING_UNITS_URL, PUT, new HttpEntity<>(toCreate, jsonHeaders()), Map.class);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  private static TeachingUnit teachingUnit() {
    var id = UUID.randomUUID().toString();
    var code = "UNIT-" + id.substring(0, 6);
    return TeachingUnit.builder()
        .id(id)
        .code(code)
        .title("Teaching Unit " + code)
        .semesterId("semester-s1")
        .credits(5)
        .build();
  }
}
