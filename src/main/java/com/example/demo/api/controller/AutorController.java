package com.example.demo.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Api dados Autor")
@RestController
@RequiredArgsConstructor
@RequestMapping("/autor")
public class AutorController {
}
