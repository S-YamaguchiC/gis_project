/*
 * 「種別」の選択肢によって「内容」の選択肢を変える
 *  9/10
 */
function selectChange()
{

    var select1 = document.forms.report.type; //変数select1を宣言
    var select2 = document.forms.report.category; //変数select2を宣言

    select2.options.length = 0; // 選択肢の数がそれぞれに異なる場合、これが重要

    if (select1.options[select1.selectedIndex].value == "舗装")
    {
        select2.options[0] = new Option("道路に穴が空いています");
        select2.options[1] = new Option("道路が爆発しています");
        select2.options[2] = new Option("その他");
    }

    else if (select1.options[select1.selectedIndex].value == "除雪")
    {
        select2.options[0] = new Option("雪の山で見通しが悪いんだよね");
        select2.options[1] = new Option("歩道がもはや存在しない");
        select2.options[2] = new Option("除雪して（切実）");
        select2.options[3] = new Option("その他");
    }

    else if (select1.options[select1.selectedIndex].value == "その他")
    {
        select2.options[0] = new Option("お腹痛い");
        select2.options[1] = new Option("眠い");
    }

}

/*
* LIFFで画像をアップロードするための関数
*/

<!--画像の複数選択:iOS版-->
IMAGES = [];
function resizeImage(file, no) {
    var d = new $.Deferred();
    var mpImg = new MegaPixImage(file);
    var src_keeper = document.getElementById('src_keeper');
    EXIF.getData(file, function() {
        var orientation = file.exifdata.Orientation;
        var mpImg = new MegaPixImage(file);
        mpImg.render(src_keeper, { maxWidth: 1024, orientation: orientation }, function() {
            var resized_img = $(src_keeper).attr('src');
            d.resolve(resized_img, no);
        });
    });
    return d.promise();
}

var sortable;
$('#file').on('change', function() {
    var files_length = this.files.length;
    for (var i=0; i<files_length; i++) {
        var file = this.files[i];
        resizeImage(file, i).then(function(resize_image, no) {
            var img = $('<img>');
            $(img).css('width', '100px');
            img.attr('id', no);
            img.attr('src', resize_image);
            var canvasData = resize_image.replace(/^data:image\/jpeg;base64,/, '');
            IMAGES.push(canvasData);
            $('#img').append(img);
            // 全ての画像を読み込んだら以下の処理を実行
            if ($('#img').find('img').size() == files_length) {
                if (files_length > 1) {
                    sortable = new Sortable(document.getElementById('img'), {
                        group: 'photo',
                        animation: 150
                    });
                }
            }
        });
    };
});

$('#upload_button').on('click', function() {
    var fd = new FormData();
    $('#img').find('img').each(function() {
        var i = parseInt($(this).attr('id'));
        fd.append('base64_encoded_data[]', IMAGES[i]);
    });
    $.ajax({
        url: '/index.php',
        type: 'POST',
        data: fd,
        processData: false,
        contentType: false,
        dataType: 'json'
    })
        .done(function(data) {
        });
});