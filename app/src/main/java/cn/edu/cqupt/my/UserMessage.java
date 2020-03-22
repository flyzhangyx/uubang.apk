package cn.edu.cqupt.my;

import org.litepal.crud.LitePalSupport;

public class UserMessage extends LitePalSupport {
    private String UserId;
    private  String TalktoId;
    private  String MessageContent;
    private  int MessageType;
    private  String date;

    public String getUserId() {
        return UserId;
    }

    public String getTalktoId() {
        return TalktoId;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public int getMessageType() {
        return MessageType;
    }

    public String Getdate(){
        return date;
    }

    public void setUser(String userId,String TalktoId,String MessageContent,int MessageType,String date) {
        this.UserId = userId;
        this.  TalktoId=TalktoId;
        this. MessageContent=MessageContent;
        this. MessageType=MessageType;
        this.date=date;
    }
}
