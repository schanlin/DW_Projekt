package backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String index() {
        return "forward:/";
    }
}