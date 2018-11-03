package br.com.servprod;

/**
 * Created by cleoner on 01/10/17.
 */

public class SharedData {

    private static String Imei;
    private static Integer SyncTime;

    public static String getImei() {
        return Imei;
    }

    public static void setImei(String imei) {
        Imei = imei;
    }

    public static Integer getSyncTime() {
        return SyncTime;
    }

    public static void setSyncTime(Integer syncTime) {
        SyncTime = syncTime;
    }
}
