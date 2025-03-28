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
    public Map<String, Double> forecastSpending(String userId, int historicalMonths, int forecastMonths) {
        List<Transaction> transactions = transactionService.getTransactions(userId, historicalMonths);
        System.out.println("Transactions fetched: " + transactions.size());
        System.out.println("Raw transactions: " + transactions);

        if (transactions.isEmpty()) {
            System.out.println("No transactions, returning default forecast");
            return getDefaultForecast();
        }

        Map<String, Double> avgMonthlySpending = calculateWeightedMonthlyAverages(transactions, historicalMonths);

        Map<String, Double> forecast = new HashMap<>();
        avgMonthlySpending.forEach((category, avg) -> {
            double forecastedAmount = avg * forecastMonths;
            forecast.put(category, forecastedAmount);
        });

        addMissingCategories(forecast);
        System.out.println("Forecast calculated: " + forecast);

        return forecast;
    }

    private Map<String, Double> calculateWeightedMonthlyAverages(List<Transaction> transactions, int historicalMonths) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusMonths(historicalMonths);

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
                                Collectors.summingDouble(t -> t.getAmount() < 0 ? -t.getAmount() : t.getAmount())
                        )
                ));

        Map<String, Double> weightedAverages = new HashMap<>();
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

        System.out.println("Weighted averages: " + weightedAverages);
        return weightedAverages;
    }

    private Map<String, Double> getDefaultForecast() {
        Map<String, Double> defaultForecast = new HashMap<>();
        defaultForecast.put("Food and Drink", 0.0);
        defaultForecast.put("Travel", 0.0);
        defaultForecast.put("Shops", 0.0);
        defaultForecast.put("Entertainment", 0.0);
        defaultForecast.put("Bills and Utilities", 0.0);
        defaultForecast.put("Transportation", 0.0);
        defaultForecast.put("Health", 0.0);
        defaultForecast.put("Unknown", 0.0);
        defaultForecast.put("Transfer", 0.0); // Added Transfer
        return defaultForecast;
    }

    private void addMissingCategories(Map<String, Double> forecast) {
        String[] commonCategories = {
                "Food and Drink", "Travel", "Shops", "Entertainment",
                "Bills and Utilities", "Transportation", "Health", "Unknown", "Transfer"
        };
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