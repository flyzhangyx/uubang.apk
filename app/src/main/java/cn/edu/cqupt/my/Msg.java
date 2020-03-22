package cn.edu.cqupt.my;

public class Msg {
    public static final int Type_recv=0;
    public static final int Type_sendc=1;
    private String content;
    private int type;
    public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }
    public String getContent(){
        return content;
    }
    public int getType(){
        return type;
    }
}

