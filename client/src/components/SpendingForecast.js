import React, { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const SpendingForecast = () => {
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

    fetch(`http://localhost:8080/transactions/forecast?userId=${storedUserID}&historicalMonths=6&forecastMonths=3`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((response) => {
        if (!response.ok) throw new Error('Failed to fetch spending forecast');
        return response.json();
      })
      .then((data) => {
        console.log('Raw forecast response:', data);
        setForecast(data);
      })
      .catch((err) => {
        setError('Failed to load forecast');
        console.error('Error fetching forecast:', err);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  console.log('Forecast state:', forecast);

  const chartData = {
    labels: Object.keys(forecast),
    datasets: [
      {
        label: '3-Month Spending Forecast ($)',
        data: Object.values(forecast),
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: '3-Month Spending Forecast' },
    },
    scales: {
      y: { beginAtZero: true, title: { display: true, text: 'Amount ($)' } },
      x: { title: { display: true, text: 'Categories' } },
    },
  };

  return (
    <div style={{ maxWidth: '800px', margin: '20px auto', height: '400px' }}>
      <Bar data={chartData} options={options} />
    </div>
  );
};

export default SpendingForecast;