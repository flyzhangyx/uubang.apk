package cn.edu.cqupt.my;

import org.litepal.crud.LitePalSupport;

public class NewMsgUnread extends LitePalSupport {
    private String dataUnread;
    private  String UserID;
    private  String TalkToid;
    private int isread;
    public String getDataUnread() {
        return dataUnread;
    }

    public String getUserID() {
        return UserID;
    }

    public String getTalkToid() {
        return TalkToid;
    }

    public int getIsread() {
        return isread;
    }
    public void setUnreadMsg( String dataUnread,
            String UserID,
              String TalkToid,
             int isread)
    {
        this.dataUnread=dataUnread;
                this.isread=isread;
                        this.TalkToid=TalkToid;
                                this.UserID=UserID;
    }

}
