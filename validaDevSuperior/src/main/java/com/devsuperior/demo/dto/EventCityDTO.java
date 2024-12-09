package com.devsuperior.demo.dto;

import java.time.LocalDate;

public record EventCityDTO(String city, String event, String url, LocalDate date) {
}
