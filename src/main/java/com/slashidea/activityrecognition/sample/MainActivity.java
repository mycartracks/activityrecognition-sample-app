package com.slashidea.activityrecognition.sample;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.slashidea.activityrecognition.ActivityRecognitionClient;
import com.slashidea.activityrecognition.ActivityRecognitionListener;
import com.slashidea.activityrecognition.DetectedActivity;
import com.slashidea.activityrecognition.EnhancedDetectedActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView resultTextView;
    private TextView confidenceLevelTextView;
    private TextView info1TextView;
    private TextView info2TextView;
    private TextView info3TextView;

    private ActivityRecognitionClient activityRecognitionClient;

    private ActivityRecognitionListener activityRecognitionListener = new ActivityRecognitionListener() {
        
        @Override 
        public void onDetectedActivity(List<DetectedActivity> activities) {  

            final StringBuilder info2Builder = new StringBuilder();
            info2Builder.append("Raw: ");
            
            for (DetectedActivity da: activities) {
                String confidence = String.format("%.1f ", da.getConfidence());
                info2Builder.append("" + da.getActivity().name() + " - " + confidence + " ");
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {                   
                    info2TextView.setText(info2Builder.toString());
                }
            });
        }

        @Override
        public void onEnhancedDetectedActivity(final EnhancedDetectedActivity activity) {
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    resultTextView.setText(activity.getActivity().name());
                    String confidence = String.format("%.0f", activity.getConfidenceLevel());
                    confidenceLevelTextView.setText(confidence + " %");
                    String time = DateFormat.getDateTimeInstance().format(new Date(activity.getTime()));
                    info1TextView.setText(time + " - " + activity.getSameActivityDuration() + " ms");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        resultTextView = (TextView) findViewById(R.id.a_main_activity_result);
        confidenceLevelTextView = (TextView) findViewById(R.id.a_main_activity_confidence_level);
        info1TextView = (TextView) findViewById(R.id.a_main_activity_info1);
        info2TextView = (TextView) findViewById(R.id.a_main_activity_info2);
        info3TextView = (TextView) findViewById(R.id.a_main_activity_info3);

        activityRecognitionClient = new ActivityRecognitionClient.Builder(getApplicationContext())
                .addActivityRecognitionListener(activityRecognitionListener).build();
        activityRecognitionClient.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        activityRecognitionClient.unregister();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
