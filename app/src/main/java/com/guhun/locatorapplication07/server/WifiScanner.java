package com.guhun.locatorapplication07.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

public class WifiScanner {

    private WifiManager wifiManager;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private static final long SCAN_INTERVAL_MS = 1000; // 1秒

    public WifiScanner(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, WifiScanReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void startScan() {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), SCAN_INTERVAL_MS, pendingIntent);
    }

    public void stopScan() {
        alarmManager.cancel(pendingIntent);
    }

    public List<ScanResult> getScanResults() {
        return wifiManager.getScanResults();
    }

    public static class WifiScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
        }
    }
}
