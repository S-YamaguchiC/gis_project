/*
* liff.htmlの画面読み込み時に起動する
* */
var mymap;  //マップ
var marker; //マーカー
var newLat=0; //最後に送信する緯度
var newLon=0; //最後に送信する経度

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
        minZoom: 13,
        attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
    }).addTo(mymap);


    function onLocationFound(e) {
        //現段階（10月ではまだ住所変換できてないので緯度・経度のまま）
        marker = L.marker(e.latlng).addTo(mymap).bindPopup("現在地\n" + e.latlng).openPopup();
        //↑の下に以下の二行書けば取得できる　　inputはさむとできない　原因はわからん
        newLat = e.latlng.lat; //緯度取得
        newLon = e.latlng.lng; //経度取得
    }

    function onLocationError(e) {
        alert("現在地を取得できませんでした。\nブラウザまたは本体の位置情報設定を見直してください。" + e.message);
    }

    function onMapClick(e) {
        //マップをクリックしたときのイベント
        // https://kita-note.com/leaflet-tutorial-4
        //すでにあるMarkerを削除して、新しいMarkerを設置
        mymap.removeLayer(marker);
        marker = L.marker(e.latlng).addTo(mymap).bindPopup(e.latlng.toString()).openPopup();
        //↑の下に以下の二行書けば取得できる　　inputはさむとできない　原因はわからん
        newLat = e.latlng.lat; //緯度取得
        newLon = e.latlng.lng; //経度取得
    }

    mymap.on('locationfound', onLocationFound);
    mymap.on('locationerror', onLocationError);
    mymap.on('click', onMapClick);

    //Androidで位置情報が取れない
    mymap.locate({setView: true, maxZoom: 16, minZoom: 13, timeout: 20000 ,enableHighAccuracy:true});

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
    //alert(document.getElementsByName('filename').value);

    //hiddenに緯度と経度を入れる
    document.getElementById('lat').value=newLat;
    document.getElementById('lng').value=newLon;

    document.report.submit();
}

//--------------------------------------------------------------------------------------------------------------------
//画像ファイルの取得確認ポップアップ（画像選択時：開発用）
function checkFileName() {

    var obj1 = document.getElementById('file');

    obj1.addEventListener('change', function(evt){
        var file = evt.target.files;
        //alert(file[0].name + "を取得しました。");
        document.report.filename.value = file[0].name;
    },false);

}

//-----------------------------------------------この２つはつかいません-----------------------------------------------
//位置情報をHiddenにつっこむ(うまくいかない. Listenerじゃないとダメです)
function inputLocation(latitude,longitude) {

    /*
    if (latitude != oldLat || longitude != oldLon) {
        document.report.location.value = "緯度：" + latitude + ", 経度：" + longitude;
        oldLat = latitude;
        oldLon = longitude;
    }
    */
}

//できない
function inputLocation2(latitude,longitude) {

    marker.addEventListener('change', function (e) {
        //マーカーが変わった？ときのイベント
        document.report.location.value = latitude + " ," + longitude;
    });

}

//--------------------------------------------------------------------------------------------------------------------
//現在位置の再取得ボタン用関数
function setCurLocation(){
    if (navigator.geolocation == false){
        alert('現在地を取得できませんでした。\n位置情報の設定を見直してください。');
        return;
    }

    function success(e) {
        mymap.removeLayer(marker);
        var lat  = e.coords.latitude;
        var lng = e.coords.longitude;
        mymap.setView([lat, lng], 15);
        marker = L.marker([lat,lng]).addTo(mymap).bindPopup('現在地\n緯度：' + lat + "\n経度：" + lng).openPopup();
        //追加
        newLat=lat;
        newLon=lng;
    };

    function error() {
        alert('現在地を取得できませんでした。');
    };

    navigator.geolocation.getCurrentPosition(success, error, {enableHighAccuracy: true});
}

//--------------------------------------------------------------------------------------------------------------------
//地図の表示切り替え
function Display(no){
    if(no == "no1"){
        document.getElementById("mapid").style.display = "block";
        document.getElementById("maphide").style.display = "none";
    }else if(no == "no2"){
        document.getElementById("mapid").style.display = "none";
        document.getElementById("maphide").style.display = "block";
    }
}