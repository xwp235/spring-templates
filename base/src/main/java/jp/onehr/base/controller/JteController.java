package jp.onehr.base.controller;

import jakarta.servlet.http.HttpServletResponse;
import jp.onehr.base.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JteController {

    @Autowired
    private VisitsRepository visitsRepository;

    @ResponseBody
    @GetMapping(value = "/profile")
    public String profile() {
        return SpringUtil.getActiveProfile();
    }

    @GetMapping("/")
    public String view(Model model, HttpServletResponse response) {
        visitsRepository.add();

        model.addAttribute("model", new DemoModel("mystérieux visiteur", visitsRepository.get()));
        return "demo";
    }

}
