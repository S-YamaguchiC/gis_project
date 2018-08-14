

function getCurrentLocation() {
    //GeolocationAPIのgetCurrentPosition関数を使って現在地を取得
    navigator.geolocation.getCurrentPosition(function (currentPosition) {
        var lat = currentPosition.coords.latitude;
        var lon = currentPosition.coords.longitude;

    }, function (error) {
        console.log(error);
        alert('位置情報が取得できません');
    });
}