mapApp.controller('naviTopicCtrl', ['$scope', naviTopicCtrl]);
function naviTopicCtrl($scope) {
    $scope.myInterval = 3000;
    $scope.slides = [
      {
          image: '/Images/mach1.jpg',
          description: 'We Are The One'
      },
      {
          image: '/Images/mach2.jpg',
          description: 'We Are The World'
      },
      {
          image: "/Images/mach3.jpg",
          description: '世界辣么大，我想去看看'
      }
    ];
}