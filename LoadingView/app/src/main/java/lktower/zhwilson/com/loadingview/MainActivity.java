package lktower.zhwilson.com.loadingview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Runnable {
    private float progress = 0;
    NirvanaLoadingView loadingView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                loadingView.setProgress(progress);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingView = (NirvanaLoadingView) findViewById(R.id.loading_view);
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (progress < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress += 1;
            handler.sendEmptyMessage(0);
        }
    }
}
