import React, { useEffect, useState } from 'react';
import { Line, Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, LineElement, Title, Tooltip, Legend, ArcElement, PointElement } from 'chart.js';
import Navi from './ui/Navi';

ChartJS.register(CategoryScale, LinearScale, BarElement, LineElement, Title, Tooltip, Legend, ArcElement, PointElement);

const SpendingForecast = () => {
  const [historicalData, setHistoricalData] = useState({});
  const [forecast, setForecast] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const storedUserID = localStorage.getItem('userID');
    if (!storedUserID) {
      setError('User not logged in');
      setLoading(false);
      return;
    }

    fetch(`http://localhost:8080/transactions/forecast?userId=${storedUserID}&historicalMonths=6&forecastMonths=3`)
      .then(response => {
        if (!response.ok) throw new Error('Failed to fetch spending forecast');
        return response.json();
      })
      .then(data => {
        console.log('Forecast data:', data);  // Log the data structure to verify
        const historical = data.historical || {};
        const forecast = data.forecast || {};

        // Process historical data for graph
        setHistoricalData(historical);
        setForecast(forecast);
      })
      .catch(err => {
        setError('Failed to load forecast');
        console.error('Error fetching forecast:', err);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  // **Historical Spending Over Time (Line Chart)**
  const historicalMonths = Object.keys(historicalData);
  const historicalValues = historicalMonths.map(month => historicalData[month]);

  const chartDataTime = {
    labels: historicalMonths,
    datasets: [
      {
        label: 'Historical Spending ($)',
        data: historicalValues,
        borderColor: 'rgba(54, 162, 235, 1)',
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        fill: true,
        tension: 0.4,
      }
    ]
  };

  const optionsTime = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: 'Spending Trends: Historical Data' }
    },
    scales: {
      y: { beginAtZero: true, title: { display: true, text: 'Amount ($)' } },
      x: { title: { display: true, text: 'Months' } }
    }
  };

  // **3-Month Spending Forecast per Category (Bar Chart)**
  const categoryLabels = Object.keys(forecast);
  const categoryValues = Object.values(forecast).map(category => category);

  const chartDataCategory = {
    labels: categoryLabels,
    datasets: [
      {
        label: '3-Month Spending Forecast ($)',
        data: categoryValues,
        backgroundColor: [
          'rgba(75, 192, 192, 0.6)',
          'rgba(255, 99, 132, 0.6)',
          'rgba(54, 162, 235, 0.6)',
          'rgba(255, 159, 64, 0.6)',
          'rgba(153, 102, 255, 0.6)',
        ],
        borderColor: [
          'rgba(75, 192, 192, 1)',
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 159, 64, 1)',
          'rgba(153, 102, 255, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  const optionsCategory = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: '3-Month Spending Forecast per Category' },
    },
    scales: {
      y: { beginAtZero: true, title: { display: true, text: 'Amount ($)' } },
      x: { title: { display: true, text: 'Categories' } }
    }
  };

  return (
    <div>
      <Navi />
      <h2 style={{ textAlign: "center" }} className="text-3xl font-bold mb-6 text-light-black">
        Spending Forecast
      </h2>
      <div style={{ display: 'flex', justifyContent: 'space-between', gap: '20px', margin: '20px auto' }}>
        {/* Chart 1: 3-Month Spending Forecast per Category (Bar Chart) */}
        <div style = {{ width: '48%',
                        borderRadius: '15px',
                        padding: '20px',
                        backgroundColor: '#ECEFF7',
                        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
                      }}>
          <Bar data={chartDataCategory} options={optionsCategory} />
        </div>
        <div style = {{ width: '48%',
                                borderRadius: '15px',
                                padding: '20px',
                                backgroundColor: '#ECEFF7',
                                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
                              }}>
        {/* Chart 2: Spending Trends (Line Chart) */}
          <Line data={chartDataTime} options={optionsTime} />
        </div>
      </div>
    </div>
  );
};

export default SpendingForecast;
