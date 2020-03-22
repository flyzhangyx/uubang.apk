package cn.edu.cqupt.my;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginConfig {
        private static SharedPreferences sp;
        public static void saveBoolean(Context ctx, String key, boolean value) {
            if (sp == null) {
                sp = ctx.getSharedPreferences("userconfig", Context.MODE_PRIVATE);
            }
            sp.edit().putBoolean(key, value).commit();
        }
        public static Boolean getBoolean(Context ctx, String key, boolean defValue) {
            if (sp == null) {
                sp = ctx.getSharedPreferences("userconfig", Context.MODE_PRIVATE);
            }
            return sp.getBoolean(key, defValue);
        }
      public static void  saveUser(Context ctx, String ID,String PWD)
      {
          if (sp == null) {
              sp = ctx.getSharedPreferences("userconfig", Context.MODE_PRIVATE);
          }
          sp.edit().putString("ID", ID).commit();
          sp.edit().putString("PWD", PWD).commit();
          //sp.edit().putString("Contactflag", "NO").commit();
      }
    public static String getID(Context ctx) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("userconfig", Context.MODE_PRIVATE);
        }
        return sp.getString("ID","");
    }
    public static String getPWD(Context ctx) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("userconfig", Context.MODE_PRIVATE);
        }
        return sp.getString("PWD","");
    }
    }
