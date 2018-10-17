/*
* Base64変換された画像のりサイズ
* https://qiita.com/yasumodev/items/ec684e81ee2eac4bdddd
* http://jsfiddle.net/9hnpdd5p/6/
* */
/*
window.onload = function() {
    // Source Image (preview)
    var imgdata = document.getElementById("resizableImage").src;
    // Resize Base64 Image
    ImgB64Resize(imgdata, 300, 150,
        function(img_b64) {
            // Destination Image
            document.getElementById("resized").src = img_b64;
        }
    );
};

// Resize Base64 Image
//   img_base64_src: string "data:image/png;base64,xxxxxxxx"
function ImgB64Resize(imgB64_src, width, height, callback) {
    // Image Type
    var img_type = imgB64_src.substring(5, imgB64_src.indexOf(";"));
    // Source Image
    var img = new Image();
    img.onload = function() {
        // New Canvas
        var canvas = document.createElement('canvas');
        canvas.width = width;
        canvas.height = height;
        // Draw (Resize)
        var ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0, width, height);
        // Destination Image
        var imgB64_dst = canvas.toDataURL(img_type);
        callback(imgB64_dst);
    };
    img.src = imgB64_src;
}
*/

window.onload = function (ev) {
    //alert("onload");
    var base64code = document.getElementById('base64code');

    // Base64文字列を画像に変換
    Base64ToImage(base64code, function(img) {

        //高さと幅
        var fwidth = img.width;
        var fheight = img.height;

        //リサイズ
        img.width = 300;
        img.height = fheight * (img.width / fwidth);

        //出力
        document.getElementById('resized').appendChild(img);
        //alert("resized ->"+img.width+":"+img.height);
    });
};

function Base64ToImage(base64img, callback) {
    var img = new Image();
    $('<input id="resized">').one('load', function() {
        alert("callback?");
        callback(img);
    }).load();
    img.src = base64img;
}