package school.hei.stdgrade.endpoint.web.controller;

import static org.reflections.Reflections.log;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import school.hei.stdgrade.file.diploma.GraduatesXlsxGenerator;
import school.hei.stdgrade.service.DiplomaService;

@Controller
@AllArgsConstructor
public class PromotionWebController {
  private final DiplomaService diplomaService;
  private final GraduatesXlsxGenerator xlsxGenerator;

  @GetMapping("/web/promotions")
  public String listPromotions(Model model) {
    log.info("=== /web/promotions requested, fetching promotions ===");
    var promotions = diplomaService.getAllPromotions();
    log.info("Promotions found: {}", promotions);
    model.addAttribute("promotions", promotions);
    return "promotions";
  }

  @GetMapping("/web/promotions/{promotionYear}/graduates.xlsx")
  public ResponseEntity<byte[]> downloadGraduatesXlsx(@PathVariable int promotionYear) {
    log.info("=== /web/promotions/{}/graduates.xlsx requested ===", promotionYear);
    var graduates = diplomaService.getGraduatesByPromotion(promotionYear);
    log.info("Graduates count: {}", graduates.size());
    var content = xlsxGenerator.generate(graduates);
    var filename = "diplomes-promotion-" + promotionYear + ".xlsx";

    return ResponseEntity.ok()
        .contentType(
            MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .body(content);
  }
}
