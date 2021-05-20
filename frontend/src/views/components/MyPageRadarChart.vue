<script>
import { Radar } from 'vue-chartjs'; // npm vue-chart.js 기반 차트 컴포넌트

export default {
  extends: Radar,
  props: ['learnData', 'averageData'],
  data() {
    return {
      maxNumber: 0,
      chartData: {
        hoverBackgroundColor: 'red',
        hoverBorderWidth: 10,
        labels: [],
        beginAtZero: true,
        datasets: [
          {
            label: '내점수',
            data: [],
            stepSize: 1,
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgb(54, 162, 235)',
            pointBackgroundColor: 'rgb(54, 162, 235)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(54, 162, 235)',
          },
          {
            label: '평균',
            data: [],

            fill: true,
            backgroundColor: 'rgba(117, 107, 255, 0.5)',
            borderColor: 'rgb(117, 107, 255)',
            pointBackgroundColor: 'rgb(117, 107, 255)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(117, 107, 255)',
          },
        ],
      },
      options: {
        scale: {
          ticks: {
            max: this.maxNumber,
            min: 0,

            beginAtZero: true,
          },
        },
        tooltips: {
          callbacks: {
            title: function(tooltipItem, chartData) {
              return;
            },
            label: function(tooltipItem, chartData) {
              console.log('3333', tooltipItem);
              console.log('4444', chartData);
              return `${chartData.labels[tooltipItem['index']]}:${tooltipItem.label}`;
            },
          },
        },
      },
    };
  },

  created() {
    var maxNum1 = 0;
    for (let i = 0; i < this.averageData.length; i++) {
      this.chartData.labels.push(this.averageData[i].data);
      this.chartData.datasets[1].data.push(this.averageData[i].per);
      if (this.averageData[i].per > maxNum1) {
        maxNum1 = this.averageData[i].per;
      }
    }
    var maxNum2 = 0;
    for (let i = 0; i < this.learnData.length; i++) {
      this.chartData.datasets[0].data.push(this.learnData[i].per);
      if (this.learnData[i].per > maxNum2) {
        maxNum2 = this.learnData[i].per;
      }
    }
    if (maxNum1 > maxNum2) {
      this.maxNumber = Math.ceil(Number(maxNum1) / 10) * 10;
    } else if (maxNum1 < maxNum2) {
      this.maxNumber = Math.ceil(Number(maxNum2) / 10) * 10;
    } else {
      this.maxNumber = Math.ceil(Number(maxNum1) / 10) * 10;
    }
  },
  mounted() {
    // 실제 차트 랜더링 부분
    this.renderChart(this.chartData, this.options);
  },
};
</script>
