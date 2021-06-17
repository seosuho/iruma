package com.kep.iruma.common.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HealthCheckController {
    @get:GetMapping("/health_check.html")
    @get:ResponseBody
    val health: ResponseEntity<String>
        get() = ResponseEntity("iruma-server is running", HttpStatus.OK)

    @get:GetMapping(value = ["/"])
    val home: String
        get() = "redirect:/health_check.html"
}
