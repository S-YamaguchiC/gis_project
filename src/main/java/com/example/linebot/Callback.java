package com.example.linebot;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@LineMessageHandler
public class Callback{

    private static final String CHANNEL_SECRET ="取得したものをいれる";
    private static final String CHANNEL_ACCESS_TOKEN ="取得したものをいれる";

    private static final Logger log = LoggerFactory.getLogger(Callback.class);

    private List<String> reportFlag;  //必ず「報告」という単語から報告が始まるように前の単語を管理する
    List<String> reportList;          //報告の内容を一時的に保存する

    // フォローイベントに対応する
    @EventMapping
    public TextMessage handleFollow(FollowEvent event) {
        // 実際の開発ではユーザIDを返信せず、フォロワーのユーザIDをデータベースに格納しておくなど
        String userId = event.getSource().getUserId();
        return reply("あなたのユーザIDは " + userId);
    }

    // 返答メッセージを作る
    private TextMessage reply(String text) {
        return new TextMessage(text);
    }


    //会話で報告をすすめる(LIFFは考えない)
    @EventMapping
    public Message handleMessage(MessageEvent<TextMessageContent> event) {
        TextMessageContent tmc = event.getMessage();
        String text = tmc.getText();

        switch (text) {
            case "報告":
                return reply("報告は、位置情報・種別・内容・詳細の4つを入力してください。（中断は「キャンセル」を入力）" + "\n1.位置情報を入力してください");
            case "キャンセル":
                return reply("報告を中断します");
            default:
                if(text.isEmpty()) {
                    return reply("？");
                } else {
                    return reply(text);
                }
        }
    }

    //位置情報を受け取ったときのイベント
    @EventMapping
    public Message handleLocationMessage(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent lmc = event.getMessage();
        double lat = lmc.getLatitude();     //
        double lon = lmc.getLongitude();    //
        String location = lmc.getAddress(); //住所名

        return reply("位置情報->\n住所: " + location + "\n緯度: " + lat + "\n経度: " + lon + "\nで報告します\n\n2.報告の種別を選択してください");
        //return handleTextMessageEvent(event);   //種別の選択肢を送信したい
    }

    //ボタンテンプレートの設計的な部分?(種別返信用ボタン)
    public ReplyMessage handleTextMessageEvent(MessageEvent<LocationMessageContent> event) {

        //ボタンに必要ぽいサムネイル画像
        String thumbnailImageUrl = "https://puu.sh/BbfNS/c2fa6e5411.jpg";
        //種別の選択肢
        Action a = new PostbackAction("舗装", "hosou");
        Action b = new PostbackAction("除雪", "josetu");
        Action c = new PostbackAction("その他", "sonota");
        //種別のリスト
        List<Action> actions = Arrays.asList(a, b, c);
        //ユーザーに選択させるときのボタンテンプレ
        ButtonsTemplate bt = new ButtonsTemplate(thumbnailImageUrl,"種別選択", "ひとつ選んで", actions);

        String altTitle = "種別選択";
        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(altTitle, bt));
    }

    //
    public ReplyMessage handlePostbackEvent(PostbackEvent event) {

        PostbackContent pbc = event.getPostbackContent();

        //ボタンで選んだやつを取ってくる
        String data = pbc.getData();

        final String replyText;

        //選んだもので分岐して内容選択させる
        if("hosou".equals(data)) {
            replyText = "「舗装」の内容を選んで";
        } else if("josetu".equals(data)) {
            replyText = "「除雪」の内容を選んで";
        } else {
            replyText = "「その他」の内容を選んで";
        }

        return new ReplyMessage(event.getReplyToken(), Arrays.asList(new TextMessage(replyText)));
    }


    public String getChannelSecret() {
        return CHANNEL_SECRET;
    }

    public String getChannelAccessToken() {
        return CHANNEL_ACCESS_TOKEN;
    }
}
