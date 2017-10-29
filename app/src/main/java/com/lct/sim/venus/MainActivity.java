package com.lct.sim.venus;

import android.os.Bundle;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.lct.sim.simtools.EventSender;
import com.lct.sim.simtools.ShellExecutor;
import com.lct.sim.simtools.TCPExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    // Example of a call to a native method
    TextView tv = (TextView) findViewById(R.id.sample_text);
    tv.setText(stringFromJNI());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.action_simulate:
                String[] search = {
                        "input keyevent 3",// 返回到主界面，数值与按键的对应关系可查阅KeyEvent
                        "sleep 1",// 等待1秒
                        "input swipe 600 400 200 400",// swipe
                        "sleep 3",// 等待3秒
                        "input swipe 200 400 600 400",// swipe
                };
                //如果input text中有中文，可以将中文转成unicode进行input,没有测试，只是觉得这个思路是可行的
                ShellExecutor se = new ShellExecutor();
                //search[5] = se.chineseToUnicode(search[5]);
                se.execShell(search);
                break;
            case R.id.action_sendevent:
                EventSender evtSender = new EventSender();
                evtSender.sampleCall();
                break;
            case R.id.action_tcp:
                new TCPExecutor().start();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
