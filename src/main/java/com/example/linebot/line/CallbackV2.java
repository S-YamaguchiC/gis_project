package com.example.linebot.line;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@LineMessageHandler
public class CallbackV2 {

    @Autowired
    private LineMessagingClient lineMessagingClient;

    // 返答メッセージを作る
    private TextMessage reply(String text) {
        return new TextMessage(text);
    }

    // 確認フォームテンプレ
    public ConfirmTemplate confirmTemplate(String text) {
        Action left = new PostbackAction("はい","y");
        Action right = new PostbackAction("いいえ","n");
        return new ConfirmTemplate(text,left,right);
    }

    // 普通のメッセージに対するイベント
    @EventMapping
    public Message handleMessage(MessageEvent<TextMessageContent> event) {

        TextMessageContent tmc = event.getMessage();
        String text = tmc.getText();

        //LIFFの報告の場合
        if (text.startsWith("種別：")) {

            Substring substring = new Substring();
            ArrayList<String> arrayList = substring.getString(text);

            for (String s : arrayList) {
                System.out.println(s);
            }

            try {
                String type = arrayList.get(0);
                String category = arrayList.get(1);
                String detail = arrayList.get(2);
                String latlng = arrayList.get(3);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            return new TemplateMessage("内容を修正しますか", confirmTemplate(text+"\n内容を修正しますか？"));

        } else {
            String string = "対応していないメッセージです。\n報告フォームを表示しますか？";
            return new TemplateMessage(string, confirmTemplate(string));
        }
    }


    // YES・NOに対応するイベント
    @EventMapping
    public Message handlePostback(PostbackEvent event) {

        // Postback
        PostbackContent pbc = event.getPostbackContent();

        //ボタンで選んだやつを取ってくる
        String data = pbc.getData();

        //確認フォームのボタンに対するアクション
        if("y".equals(data)) {
            return reply("LIFFを起動する予定");
        } else if("n".equals(data)) {
            return reply("報告を送信しました。（仮）\nありがとうございます。");
        } else {
            return reply("どうすることもできんゾ : data -> " + data);
        }
    }
}
