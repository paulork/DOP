package br.com.paulork.dop.controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
@Controller
public class IndexController {

    @Get("/")
    public void index() {
    }

}
