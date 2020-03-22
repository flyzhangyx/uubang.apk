package cn.edu.cqupt.my;

import org.litepal.crud.LitePalSupport;

public class Contact extends LitePalSupport {
    private String UserID;
    private String TalkToId;

    public String GetID() {
        return UserID;
    }

    public String getTalktoId() {
        return TalkToId;
    }

    public void SetContact(String UserId,String TalktoId) {
        this.UserID = UserId;
        this.TalkToId=TalktoId;
    }

}
