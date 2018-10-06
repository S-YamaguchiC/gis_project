/*
* liff.htmlの画面読み込み時に起動する
* */
var mymap;  //マップ
var marker; //マーカー
var popup = L.popup();  //ポップアップ
var oldLat;   //最初期の緯度
var oldLon;   //最初期の経度

function onPageLoad() {

    //初期設定
    drawMap();
    changeSelect();

}

//--------------------------------------------------------------------------------------------------------------------
//地図の制御
function drawMap() {

    //地図の表示
    mymap = L.map('mapid');

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
    }).addTo(mymap);


    function onLocationFound(e) {
        //現段階（10月ではまだ住所変換できてないので緯度・経度のまま）
        marker = L.marker(e.latlng).addTo(mymap).bindPopup("現在地\n" + e.latlng).openPopup();
        inputLocation(e.coords.latitude,e.coords.longitude);
        oldLat = e.coords.latitude;
        oldLon = e.coords.longitude;
    }

    function onLocationError(e) {
        alert("現在地を取得できませんでした。" + e.message);
    }

    function onMapClick(e) {
        //マップをクリックしたときのイベント
        // https://kita-note.com/leaflet-tutorial-4
        //すでにあるMarkerを削除して、新しいMarkerを設置
        mymap.removeLayer(marker);
        marker = L.marker(e.latlng).addTo(mymap).bindPopup(e.latlng.toString()).openPopup();
        inputLocation(e.coords.latitude,e.coords.longitude);
    }

    mymap.on('locationfound', onLocationFound);
    mymap.on('locationerror', onLocationError);
    mymap.on('click', onMapClick);

    mymap.locate({setView: true, maxZoom: 16, timeout: 20000});

}


//--------------------------------------------------------------------------------------------------------------------
//選択肢の制御
function changeSelect() {
    var select1 = document.forms.report.type; //変数select1を宣言
    var select2 = document.forms.report.category; //変数select2を宣言

    select2.options.length = 0; // 選択肢の数がそれぞれに異なる場合、これが重要

    if (select1.options[select1.selectedIndex].value == "舗装") {
        select2.options[0] = new Option("道路に穴が空いています");
        select2.options[1] = new Option("道路が爆発しています");
        select2.options[2] = new Option("その他");
    }

    else if (select1.options[select1.selectedIndex].value == "除雪") {
        select2.options[0] = new Option("雪の山で見通しが悪いんだよね");
        select2.options[1] = new Option("歩道がもはや存在しない");
        select2.options[2] = new Option("除雪して（切実）");
        select2.options[3] = new Option("その他");
    }

    else if (select1.options[select1.selectedIndex].value == "その他") {
        select2.options[0] = new Option("お腹痛い");
        select2.options[1] = new Option("眠い");
    }
}

//--------------------------------------------------------------------------------------------------------------------
//選択したファイルの名前をポップアップ(送信時：開発用)
function getFilename() {
    var path = document.getElementById('file').value;
    var regex = /\\|\\/;
    var array = path.split(regex);
    var f_name = array[array.length - 1];
    //alert(f_name);
    document.getElementsByName('filename').value = f_name;
    alert(document.getElementsByName('filename').value);
    document.report.submit();
}

//--------------------------------------------------------------------------------------------------------------------
//画像ファイルの取得確認ポップアップ（画像選択時：開発用）
function checkFileName() {

    var obj1 = document.getElementById('file');

    obj1.addEventListener('change', function(evt){
        var file = evt.target.files;
        alert(file[0].name + "を取得しました。");
        document.report.filename.value = file[0].name;
    },false);

}

//--------------------------------------------------------------------------------------------------------------------
//位置情報をHiddenにつっこむ(うまくいかない. Listenerじゃないとダメです)
function inputLocation(latitude,longitude) {

    if (latitude != oldLat || longitude != oldLon) {
        document.report.location.value = "緯度：" + latitude + ", 経度：" + longitude;
        oldLat = latitude;
        oldLon = longitude;
    }
}

//--------------------------------------------------------------------------------------------------------------------
//現在位置の再取得ボタン用関数
function setCurLocation(){
    if (navigator.geolocation == false){
        alert('現在地を取得できませんでした。');
        return;
    }

    function success(e) {
        mymap.removeLayer(marker);
        var lat  = e.coords.latitude;
        var lng = e.coords.longitude;
        mymap.setView([lat, lng], 15);
        marker = L.marker([lat,lng]).addTo(mymap).bindPopup('現在地\n緯度：' + lat + "\n経度：" + lng).openPopup();
        //追加
        inputLocation(lat,lng);
    };

    function error() {
        alert('現在地を取得できませんでした。');
    };

    navigator.geolocation.getCurrentPosition(success, error);
}