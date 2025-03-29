package org.mthree.service;

import org.mthree.dto.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final TransactionService transactionService;

    public ForecastServiceImpl(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Map<String, Object> forecastSpending(String userId, int historicalMonths, int forecastMonths) {
        List<Transaction> transactions = transactionService.getTransactions(userId, historicalMonths);
        System.out.println("Transactions fetched: " + transactions.size());

        if (transactions.isEmpty()) {
            System.out.println("No transactions, returning default forecast");
            return Map.of(
                    "historical", getDefaultHistoricalData(historicalMonths),
                    "forecast", getDefaultForecast()
            );
        }

        Map<String, Object> avgMonthlySpending = calculateWeightedMonthlyAverages(transactions, historicalMonths);

        Map<String, Object> forecast = new HashMap<>();
        avgMonthlySpending.forEach((category, avg) -> {
            double forecastedAmount = (Double) avg * forecastMonths;
            forecast.put(category, forecastedAmount);
        });

        addMissingCategories(forecast);
        Map<String, Object> historicalData = calculateHistoricalSpending(transactions, historicalMonths);

        return Map.of("historical", historicalData, "forecast", forecast);
    }

    private Map<String, Object> calculateWeightedMonthlyAverages(List<Transaction> transactions, int historicalMonths) {
        LocalDate now = LocalDate.now();
        LocalDate latestDate = transactions.stream()
                .map(Transaction::getDate)
                .max(LocalDate::compareTo)
                .orElse(now);

        Map<String, Map<Integer, Double>> monthlyTotals = transactions.stream()
                .filter(t -> !t.isPending())
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.groupingBy(
                                t -> {
                                    int monthsAgo = (int) latestDate.until(t.getDate(), java.time.temporal.ChronoUnit.MONTHS);
                                    return Math.max(0, Math.min(historicalMonths - 1, historicalMonths + monthsAgo));
                                },
                                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
                        )
                ));

        Map<String, Object> weightedAverages = new HashMap<>();
        monthlyTotals.forEach((category, monthTotals) -> {
            double totalWeighted = 0.0;
            int totalWeight = 0;
            for (int month = 0; month < historicalMonths; month++) {
                double amount = monthTotals.getOrDefault(month, 0.0);
                int weight = month + 1;
                totalWeighted += amount * weight;
                totalWeight += weight;
            }
            double avg = totalWeight > 0 ? totalWeighted / totalWeight : 0.0;
            weightedAverages.put(category, avg);
        });

        return weightedAverages;
    }

    private Map<String, Object> calculateHistoricalSpending(List<Transaction> transactions, int historicalMonths) {
        LocalDate now = LocalDate.now();
        Map<String, Object> monthlySpending = new HashMap<>();

        for (int i = 0; i < historicalMonths; i++) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

            double total = transactions.stream()
                    .filter(t -> !t.isPending() && !t.getDate().isBefore(monthStart) && !t.getDate().isAfter(monthEnd))
                    .mapToDouble(Transaction::getAmount)
                    .sum();

            monthlySpending.put(monthStart.getMonth().toString() + " " + monthStart.getYear(), Math.abs(total));
        }

        return monthlySpending;
    }

    private Map<String, Object> getDefaultForecast() {
        Map<String, Object> defaultForecast = new HashMap<>();
        String[] categories = {"Food and Drink", "Travel", "Shops", "Entertainment", "Bills and Utilities", "Transportation", "Health", "Unknown", "Transfer"};
        for (String category : categories) {
            defaultForecast.put(category, 0.0);
        }
        return defaultForecast;
    }

    private Map<String, Object> getDefaultHistoricalData(int historicalMonths) {
        Map<String, Object> defaultHistoricalData = new HashMap<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < historicalMonths; i++) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            defaultHistoricalData.put(monthStart.getMonth().toString() + " " + monthStart.getYear(), 0.0);
        }

        return defaultHistoricalData;
    }

    private void addMissingCategories(Map<String, Object> forecast) {
        String[] commonCategories = {"Food and Drink", "Travel", "Shops", "Entertainment", "Bills and Utilities", "Transportation", "Health", "Unknown", "Transfer"};
        for (String category : commonCategories) {
            forecast.putIfAbsent(category, 0.0);
        }
    }

    @Override
    public String toString() {
        return "ForecastServiceImpl{" +
                "transactionService=" + transactionService +
                '}';
    }
}
