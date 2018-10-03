package com.example.linebot.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 参考 -> https://qiita.com/nvtomo1029/items/316c5e8fe5d0cd92339c
* @Author S-Yamaguchi
*/

@RestController
@RequestMapping("/upload")
public class UploadController {


    //POST用メソッド
    @RequestMapping(method = RequestMethod.POST)
    public Object upload(
            @RequestParam("file")MultipartFile multipartFile) throws IOException {

        // ファイルが空の場合は異常終了
        if(multipartFile.isEmpty()){
            // 異常終了時の処理
            return "FILE is EMPTY";
        }

        // ファイル種類から決まる値をセットする(ここにLINEidを入れてディレクトリ分けでもいいかも)
        StringBuffer filePath = new StringBuffer("/uploadfile");   //ファイルパス

        // アップロードファイルを格納するディレクトリを作成する
        File uploadDir = mkdirs(filePath);

        try {
            // アップロードファイルを置く
            File uploadFile = new File(uploadDir.getPath());
            byte[] bytes = multipartFile.getBytes();
            BufferedOutputStream uploadFileStream =
                    new BufferedOutputStream(new FileOutputStream(uploadFile));
            uploadFileStream.write(bytes);
            uploadFileStream.close();

            return "You successfully uploaded.";

        } catch (Exception e) {
            // 異常終了時の処理
            return e;
        } catch (Throwable t) {
            // 異常終了時の処理
            return t;
        }

    }

    /**
     * アップロードファイルを格納するディレクトリを作成する
     *
     * @param filePath
     * @return
     */
    private File mkdirs(StringBuffer filePath){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        File uploadDir = new File(filePath.toString(), sdf.format(now));
        // 既に存在する場合はプレフィックスをつける
        int prefix = 0;
        while(uploadDir.exists()){
            prefix++;
            uploadDir =
                    new File(filePath.toString() + sdf.format(now) + "-" + String.valueOf(prefix));
        }

        // フォルダ作成
        uploadDir.mkdirs();

        return uploadDir;
    }

}
