package org.mthree.service;

import org.mthree.dto.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ForecastService {
    Map<String, Double> forecastSpending(String userId, int historicalMonths, int forecastMonths);
}