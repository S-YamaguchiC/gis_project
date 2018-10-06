/*
* Base64変換された画像のりサイズ
* https://qiita.com/yasumodev/items/ec684e81ee2eac4bdddd
* http://jsfiddle.net/9hnpdd5p/6/
* */

window.onload = function(imgdata) {
    var imageWidth = imgdata
    // Resize Base64 Image
    ImgB64Resize(imgdata, 300, 150,
        function(img_b64) {
            // Destination Image
            document.getElementById("img_dst").src = img_b64;
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
