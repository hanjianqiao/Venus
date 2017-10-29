package com.lct.sim.simtools;

import android.os.AsyncTask;

import java.io.DataOutputStream;

/**
 * Created by lct on 2017/10/30.
 */

public class TCPExecutor {
    private TCPClient mClient = null;

    public void start(){
        new connectTask().execute("");
    }
    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a Client object and
            mClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }, "lct.hanjianqiao.cn", 9090);

            mClient.run();

            return null;
        }
        Process process = null;
        DataOutputStream os = null;
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            try {
                // 获取root权限，这里大量申请root权限会导致应用卡死，可以把Runtime和Process放在Application中初始化
                if(process == null){
                    Runtime runtime = Runtime.getRuntime();
                    process = runtime.exec("su");
                }
                if(os == null) {
                    os = new DataOutputStream(process.getOutputStream());
                }

                if(!values[0].equals("exit")) {
                    os.write(values[0].getBytes());
                    os.writeBytes("\n");
                    os.flush();
                }else {
                    os.writeBytes("exit\n");
                    os.flush();
                    process.waitFor();
                    os = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
