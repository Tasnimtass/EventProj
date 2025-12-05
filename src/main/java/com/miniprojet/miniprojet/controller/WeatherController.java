package com.miniprojet.miniprojet.controller;

import com.miniprojet.miniprojet.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Nouveau endpoint : récupérer météo par ville
    @GetMapping("/city/{ville}")
    public String getWeatherByCity(@PathVariable String ville) {
        return weatherService.getWeatherByCity(ville);
    }
}
