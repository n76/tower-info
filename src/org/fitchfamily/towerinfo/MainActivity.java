package org.fitchfamily.towerinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import java.util.Random;

public class MainActivity extends Activity
{
    LocationEstimator myEstimator = new LocationEstimator(this);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        final Random mRand = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(
            new Button.OnClickListener() {
                @Override public void onClick(View arg0) {
//                    btn.setText(String.valueOf(mRand.nextInt(200)));
                    btn.setText(myEstimator.toString());
                }
            }
        );
    }
}
