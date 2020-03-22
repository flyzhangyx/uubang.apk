package cn.edu.cqupt.my;

import org.litepal.crud.LitePalSupport;

public class iotRec extends LitePalSupport {
        private String UserID;
        private String TalkToId;
        private String temp;
        private String water;
        private int state;

        public String Get_ID() {
            return UserID;
        }

        public String getTalktoId() {
            return TalkToId;
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
            this.UserID = UserId;
            this.TalkToId=TalktoId;
            this.temp=temp;
            this.water=water;
            this.state=state;
        }

    }

