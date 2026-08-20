package school.hei.stdgrade.endpoint.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import school.hei.stdgrade.service.DiplomaService;

@Controller
@AllArgsConstructor
public class PromotionWebController {
  private final DiplomaService service;

  @GetMapping("/web/promotions")
  public String listPromotions(Model model) {
    model.addAttribute("promotions", service.getAllPromotions());
    return "promotions";
  }
}
