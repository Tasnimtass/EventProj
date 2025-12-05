package com.miniprojet.miniprojet.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
@Service
public class WeatherService {
    private final String API_KEY = "918fb1571695015716466a5f5085e965";

    public String getWeatherByCity(String ville) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + ville + "&appid=" + API_KEY + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");

        if (weatherList != null && !weatherList.isEmpty()) {
            Map<String, Object> mainData = (Map<String, Object>) response.get("main");
            double temp = mainData != null ? (double) mainData.get("temp") : 0;
            return weatherList.get(0).get("description") + " à " + temp + "°C";
        }
        return "Météo non disponible";
    }
}
