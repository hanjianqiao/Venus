package com.lct.sim.simtools;

import java.io.DataOutputStream;

/**
 * Created by lct on 2017/10/29.
 */

public class EventSender {
    /**
     * 执行Shell命令
     *
     * @param commands
     *            要执行的命令数组
     */
    public void execShell(String[] commands) {
        // 获取Runtime对象
        Runtime runtime = Runtime.getRuntime();

        DataOutputStream os = null;
        try {
            // 获取root权限，这里大量申请root权限会导致应用卡死，可以把Runtime和Process放在Application中初始化
            Process process = runtime.exec("su");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset
                // error
                os.write(command.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sampleCall(){
        // key definition: https://android.googlesource.com/platform/external/kernel-headers/+/8bc979c0f7b0b30b579b38712a091e7d2037c77e/original/uapi/linux/input.h
        String[] search = {
                "sleep 3",
                "sendevent /dev/input/event7 1 102 1;",
                "sendevent /dev/input/event7 0 0 0;",
                "sendevent /dev/input/event7 1 102 0;",
                "sendevent /dev/input/event7 0 0 0"
        };
        execShell(search);
    }
}
