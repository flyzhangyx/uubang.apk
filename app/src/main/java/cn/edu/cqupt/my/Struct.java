package cn.edu.cqupt.my;

public class Struct {
    private static  byte[] buf;
    private    static String Checkcode;
    private   static String Id;
    private  static String Pwd;
    private  static String talktoId;
    private  static String data;
    private  static String RPWD;
    public  static void Struct(String a,String b, String c,String d,String e,String f,String Savedinstance)
    {
        buf=new byte[721];
        Checkcode=a;
        Id=b;
        Pwd=c;
        talktoId=d;
        RPWD=e;
        data=f;
    }
    public static byte[] getBuf(String a,String b, String c,String d,String e,String f,String Savedinstance)
    {
        Struct(a,b,c,d,e,f,Savedinstance);
        System.arraycopy(Checkcode.getBytes(),0,buf,0,Checkcode.getBytes().length);
        System.arraycopy(Id.getBytes(),0,buf,18,Id.getBytes().length);
        System.arraycopy(Pwd.getBytes(),0,buf,30,Pwd.getBytes().length);
        System.arraycopy(talktoId.getBytes(),0,buf,63,talktoId.getBytes().length);
        System.arraycopy(RPWD.getBytes(),0,buf,75,RPWD.getBytes().length);
        System.arraycopy(data.getBytes(),0,buf,108,data.getBytes().length);
        //System.arraycopy("\n".getBytes(),0,buf,612,"\n".getBytes().length);
        return buf;
    }
}
