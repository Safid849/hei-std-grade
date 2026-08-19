package school.hei.stdgrade.it;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
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
import static school.hei.stdgrade.conf.TestUtils.TEACHER_B_EMAIL;
import static school.hei.stdgrade.conf.TestUtils.authHeaders;
import static school.hei.stdgrade.conf.TestUtils.jsonHeaders;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import school.hei.stdgrade.conf.TestcontainersConfigurer;
import school.hei.stdgrade.model.Exam;
import school.hei.stdgrade.model.SessionType;

class ExamAndGradeRbacIT extends TestcontainersConfigurer {
  @Autowired TestRestTemplate testRestTemplate;

  private String adminToken;
  private String teacherAToken;
  private String teacherBToken;
  private String studentAToken;

  private static final String COURSE_ID = "course-donnees1";
  private static final String ACADEMIC_YEAR_ID = "academic-year-2024";

  @BeforeEach
  void setUp() {
    adminToken = utils.tokenOf(ADMIN_EMAIL);
    teacherAToken = utils.tokenOf(TEACHER_A_EMAIL);
    teacherBToken = utils.tokenOf(TEACHER_B_EMAIL);
    studentAToken = utils.tokenOf(STUDENT_A_EMAIL);
  }

  @Test
  void everyone_can_list_exams_without_authentication() {
    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            GET,
            new HttpEntity<>(jsonHeaders()),
            Exam[].class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    assertEquals(OK, response.getStatusCode());
  }

  @Test
  void admin_can_crupdate_an_exam() {
    var toCreate = exam(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, authHeaders(adminToken)),
            Exam.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void assigned_teacher_can_crupdate_an_exam() {
    var toCreate = exam(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, authHeaders(teacherAToken)),
            Exam.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    assertEquals(OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void unassigned_teacher_cannot_crupdate_an_exam() {
    var toCreate = exam(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, authHeaders(teacherBToken)),
            Map.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void student_cannot_crupdate_an_exam() {
    var toCreate = exam(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, authHeaders(studentAToken)),
            Map.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    assertEquals(FORBIDDEN, response.getStatusCode());
  }

  @Test
  void anonymous_cannot_crupdate_an_exam() {
    var toCreate = exam(randomUUID().toString());

    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, jsonHeaders()),
            Map.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    assertEquals(UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void everyone_can_get_exam_by_id() {
    var toCreate = exam(randomUUID().toString());
    var createResponse =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, authHeaders(adminToken)),
            Exam.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);

    var response =
        testRestTemplate.exchange(
            "/exams/{examId}",
            GET,
            new HttpEntity<>(jsonHeaders()),
            Exam.class,
            createResponse.getBody().id());

    assertEquals(OK, response.getStatusCode());
  }

  private Exam createExam() {
    var toCreate = exam(randomUUID().toString());
    var response =
        testRestTemplate.exchange(
            "/courses/{courseId}/academic-years/{academicYearId}/exams",
            PUT,
            new HttpEntity<>(toCreate, authHeaders(adminToken)),
            Exam.class,
            COURSE_ID,
            ACADEMIC_YEAR_ID);
    return response.getBody();
  }

  private static Exam exam(String id) {
    return Exam.builder()
        .id(id)
        .examDate(now())
        .coefficient(0.5)
        .sessionType(SessionType.REGULAR)
        .build();
  }
}
