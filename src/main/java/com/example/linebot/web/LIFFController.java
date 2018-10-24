package com.example.linebot.web;

import com.example.linebot.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.SQLException;


@Controller
public class LIFFController {

    @Autowired
    ReportDao reportDao;

    @GetMapping("/liff")
    public String hello(Model model) {
        // [[${test}]] の部分を Hello... で書き換えて、liff.htmlを表示する

        //Project route
        String dir = System.getProperty("user.dir");
        System.out.println("ルート：" + dir);

        model.addAttribute("test", "報告フォーム");
        return "liff";
    }


    /*
    * @param type 報告の種別
    * @param cat 報告の内容
    * @param det 詳細記入
    * @param fname 送信するファイル名
    * @param locate 送信する位置情報
    * @param mfile Base64変換するファイルのバイナリデータ?
    *
    * */
    @RequestMapping("/result")
    public String pic(ModelMap modelMap,
                      @RequestParam("type")String type,
                      @RequestParam("category")String cat,
                      @RequestParam("detail")String det,
                      @RequestParam("filename")String fname,
                      @RequestParam("file") MultipartFile mfile,
                      @RequestParam("lat")String lat,
                      @RequestParam("lng")String lng

    ) throws IOException, SQLException {

        //DBにアクセス
        reportDao.insert(type,cat,det,lat,lng);

        modelMap.addAttribute("type",type);
        modelMap.addAttribute("category",cat);
        modelMap.addAttribute("detail",det);
        modelMap.addAttribute("filename",fname);
        modelMap.addAttribute("lat", lat);
        modelMap.addAttribute("lng", lng);

        //画像の有無
        boolean exist = false;

        //nullチェック
        if(mfile.isEmpty()) {
            //画像がなかった場合は画像の変換がいらないのでそのままresultに遷移
            return "/result";
        }

        //リサイズのためのインスタンス
        ResizeImage resizeImage = new ResizeImage();

        //base64にした画像データ
        //String base64encodingStr = base64EncodeStr(mfile);
        String base64encodingStr = resizeImage.base64EncodeStrV2(mfile);
        modelMap.addAttribute("base64","data:image/png;base64," + base64encodingStr);

        //画像がNULLじゃないはずなので、trueにして送信
        exist = true;
        modelMap.addAttribute("exist",exist);

        return "/result";
    }


    /*test*/
    @RequestMapping("/android_test")
    public String testpage() {
        return "android_test";
    }

}
