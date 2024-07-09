package com.sistema.vendas.tenis.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DashboardController {

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboardPage() {
        return "dashboard"; // Retorna o nome do arquivo HTML sem a extensão
    }
}
