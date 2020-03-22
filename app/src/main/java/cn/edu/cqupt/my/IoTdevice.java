package cn.edu.cqupt.my;

import org.litepal.crud.LitePalSupport;

public class IoTdevice extends LitePalSupport {
        private String User_ID;
        private String Talk_ToId;
        private String temp;
        private String water;
        private int state;

        public String Get_ID() {
            return User_ID;
        }

        public String getTalktoId() {
            return Talk_ToId;
        }
        public String gettenp() {
            return temp;
        }
    public String getwater() {
        return water;
    }
    public int getstate() {
        return state;
    }
        public void SetContact(String UserId,String TalktoId,String temp,String water,int state) {
            this.User_ID = UserId;
            this.Talk_ToId=TalktoId;
            this.temp=temp;
            this.water=water;
            this.state=state;
        }

    }

