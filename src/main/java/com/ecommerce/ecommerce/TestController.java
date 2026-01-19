package com.ecommerce.ecommerce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {

    @GetMapping("/test")
    public String test() {
        return "✅ Backend funcionando!";
    }

    @GetMapping("/test/exception")
    public String testException() {
        throw new RuntimeException("Teste de exceção");
    }
}