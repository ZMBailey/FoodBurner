package soulstudios.caloriecounter;

import android.os.CountDownTimer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * count up timer class for timer-based scoring.
 */

public abstract class CountUpTimer extends CountDownTimer {
    private static final long interval_ms = 1000;
    private final long duration;

    protected CountUpTimer(long duration_ms){
        super(duration_ms,interval_ms);
        this.duration = duration_ms;
    }

    public abstract void onTick(int second);

    public static String ConvertToTime(int second){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("mm:ss", Locale.getDefault());
        df.setTimeZone(tz);
        String time = df.format(new Date(second*1000L));

        return time;
    }

    @Override
    public void onTick(long msUntilFinished){
        int second = (int) ((duration-msUntilFinished)/1000);
        onTick(second);
    }

    @Override
    public void onFinish(){
        onTick(duration/1000);
    }
}
