/*
 * Copyright 2014 Akexorcist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meplus.robot.presenters;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.meplus.events.EventUtils;
import com.meplus.punub.Command;
import com.meplus.robot.activity.DeviceListActivity;
import com.meplus.robot.events.BluetoothEvent;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothSPP.BluetoothConnectionListener;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import hugo.weaving.DebugLog;

public class BluetoothPresenter {
    private static final String TAG = BluetoothPresenter.class.getSimpleName();
    private final static boolean ENABLE = true;
    private final int PERCENT = 42;
    private final int MAX = 500;
    private BluetoothSPP bt;
    int times = 0;
    byte U1_Dis;
    byte U2_Dis;
    byte U3_Dis;
    byte U4_Dis;
    byte U5_Dis;

    public BluetoothPresenter(Context context) {
        if (!ENABLE) return;
        bt = new BluetoothSPP(context);
    }

    public boolean isBluetoothAvailable() {
        if (!ENABLE) return true;
        return bt.isBluetoothAvailable();
    }

    public boolean isBluetoothEnabled() {
        if (!ENABLE) return true;
        return bt.isBluetoothEnabled();
    }

    public boolean isServiceAvailable() {
        if (!ENABLE) return true;
        return bt.isServiceAvailable();
    }

    public void create(Context context) {
        if (!ENABLE) return;
        bt.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                postConnectedEvent();
                Toast.makeText(context, "Connected to " + name + "\n" + address, Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                postConnectedEvent();
                Toast.makeText(context, "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                postConnectedEvent();
                Toast.makeText(context, "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
        bt.setOnDataReceivedListener((data, message) -> receivedData(data, message));
    }

    /**
     * 启动蓝牙软件部分
     *
     * @param activity
     */
    public void enableBluetooth(Activity activity) {
        if (!ENABLE) return;

        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
    }

    /**
     * 启动蓝牙的服务
     */
    public void startBluetoothService() {
        if (!ENABLE) return;

        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
    }

    /**
     * 选择可以拦截的蓝牙
     *
     * @param activity
     */
    public void connectDeviceList(Activity activity) {
        if (!ENABLE) return;

        Intent intent = new Intent(activity, DeviceListActivity.class);
        activity.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (!ENABLE) return;

        if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
            bt.disconnect();
        }
    }

    /**
     *
     */
    public void stopBluetoothService() {
        if (!ENABLE) return;

        bt.stopService();
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (!ENABLE) return;

        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(activity, "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        }
    }


    @DebugLog
    public boolean sendDefault() {
        if (!ENABLE) return true;

        if (!isConnected()) {
            return false;
        }
        // 自主避障功能使能（默认关闭），00关闭，01开启
        byte CheckSum = (byte) (0X66 + (byte) 0XAA + 0X09 + 0X10);
        byte[] buffer = new byte[]{0X66, (byte) 0XAA, 0X09, 0X10, 01, 00, 00, 00, CheckSum};
        sendData(buffer);
        return true;
    }

    public boolean sendGoHome() {
        if (!ENABLE) return true;

        if (!isConnected()) {
            return false;
        }
        byte CheckSum = (byte) (0X66 + (byte) 0XAA + 0X09 + 0X14 + 0X01 + 0X00 + 0X00 + 0X00);
        byte[] buffer = new byte[]{0X66, (byte) 0XAA, 0X09, 0X14, 0X01, 0X00, 0X00, 0X00, CheckSum};
        sendData(buffer);
        return true;
    }

    @DebugLog
    public boolean sendDirection(String action) {
        if (!ENABLE) return true;

        if (!isConnected()) {
            return false;
        }

        final int V = (MAX * PERCENT / 100);

        int V1 = 0;
        int V2 = 0;
        byte V1H = 0;
        byte V1L = 0;
        byte V2H = 0;
        byte V2L = 0;
        byte CheckSum;

        if (action.equals(Command.ACTION_UP)) {
            V1 = V * 3 / 2;
            V2 = V * 3 / 2;
        } else if (action.equals(Command.ACTION_DOWN)) {
            V1 = -V;
            V2 = -V;
        } else if (action.equals(Command.ACTION_LEFT)) {
            V1 = -V / 2;
            V2 = V / 2;
        } else if (action.equals(Command.ACTION_RIGHT)) {
            V1 = V / 2;
            V2 = -V / 2;
        } else if (action.equals(Command.ACTION_STOP)) {
            V1 = 0;
            V2 = 0;
        }

        V1H = (byte) (V1 >> 8);
        V2H = (byte) (V2 >> 8);
        V1L = (byte) (V1);
        V2L = (byte) (V2);

        CheckSum = (byte) (0X66 + (byte) 0XAA + 0X09 + 0X11 + V1H + V1L + V2H + V2L);
        byte[] buffer = new byte[]{0X66, (byte) 0XAA, 0X09, 0X11, V1H, V1L, V2H, V2L, CheckSum};

        sendData(buffer);
        return true;
    }


    @DebugLog
    public void receivedData(byte[] data, String message) {


        if (!ENABLE) return;

        if (data != null && data.length >= 0x22) {

            final byte Head1 = data[0];
            final byte Head2 = data[1];
            final byte Length = data[2];

            if (Head1 == ((byte) 0x88) && Head2 == ((byte) 0xBB) && Length == ((byte) 0x22)) {
//                Bit 0	充电器是否接触，1：接触，0：未接触
//                Bit 1	充电器是否连接，1：连接，0：未连接
//                Bit 2	电机电源状态，1：打开，0：关闭
//                Bit 3	保留
//                Bit 4	保留
//                Bit 5	保留
//                Bit 6	保留
//                Bit 7	保留
                final byte BMS_Status = data[3];
//                Bit 0	电压报警
//                Bit 1	电压错误
//                Bit 2	电流报警
//                Bit 3	电流错误
//                Bit 4	温度报警
//                Bit 5	温度错误
//                Bit 6	容量报警
//                Bit 7	容量错误
                final byte BMS_Error = data[4];
//                0-100 单位%
                final byte SOC = data[5];//电量
                final byte Voltage_H = data[6];
                final byte Voltage_L = data[7];
                final byte Current_H = data[8];
                final byte Current_L = data[9];
                // 左轮电机编码器（4 Byte）
                // data[10]-data[13];
                // 右轮电机编码器（4 Byte）
                // data[14]-data[17];
                // 0-255 单位cm
                /* final byte U1_Dis = data[18];*/
               // byte U1_Dis = data[18];
                // 0-255 单位cm
               // byte U2_Dis = data[19];
                // 0-255 单位cm
              //  byte U3_Dis = data[20];
                //byte U3_Dis = (byte)(sum/10);
                // 0-255 单位cm
               // byte U4_Dis = data[21];
                // 0-255 单位cm
               // byte U5_Dis = data[22];
                // 防跌落传感器
                // final byte Fall_IR = data[23];
                // 航向角
                // data[24]-data[25;
                // 俯仰角
                // data[26]-data[27];
                // 横滚角
                // data[28]-data[29];
                // 通信异常
                // data[30];
                // 系统故障
                // data[31];
                byte obj[] = new byte[5];

                U1_Dis = data[18];
                U2_Dis = data[19];
                U3_Dis = data[20];
                U4_Dis = data[21];
                U5_Dis = data[22];

          /*      if(U1_Dis<20)
                    times++;
                else
                    times=0;*/

                //final String info = String.format("%1$x,%2$x,%3$x,%4$x,%5$x,%6$x,%7$x,%8$x,%9$x,%10$x", Head1, Head2, Length, BMS_Status, BMS_Error, SOC, Voltage_H, Voltage_L, Current_H, Current_L);
               /* final String info = String.format("%1$x,%2$x,%3$x,%4$x,%5$x,%6$x,%7$x,%8$x,%9$x,%10$x,%11$x", Head1, Head2, Length, BMS_Status, BMS_Error, SOC, Voltage_H, Voltage_L, Current_H, Current_L,obj);
                Log.i(TAG, info);*/
                final BluetoothEvent event = new BluetoothEvent();
                event.setConnected(isConnected());
                event.setSOC(SOC);
                EventUtils.postEvent(event);

                final BluetoothEvent event1 = new BluetoothEvent();
                if(U1_Dis<20)
                {
                    byte temp1 = 1;
                    event1.setAvoidObj(temp1);
                    EventUtils.postEvent(event1);
                }else
                {
                    byte temp0 = 0;
                    event1.setAvoidObj(temp0);
                    EventUtils.postEvent(event1);
                }

                //add obj
               /* for (int i=0;i<5;i++){
                    if(temp[i]<20){
                        obj[i] = 1;
                        event.setAvoidObj(obj[i]);
                        EventUtils.postEvent(event);
                    }else{
                        obj[i] = 0;
                        event.setAvoidObj(obj[i]);
                        EventUtils.postEvent(event);
                    }
                }*/
            }
        }

    }

    @DebugLog
    private void sendData(byte[] buffer) {
        if (!ENABLE) return;

        bt.send(buffer, false);
    }


    private boolean isConnected() {
        if (!ENABLE) return true;

        return bt.getServiceState() == BluetoothState.STATE_CONNECTED;
    }

    private void postConnectedEvent() {
        if (!ENABLE) return;

        final BluetoothEvent event = new BluetoothEvent();
        event.setConnected(isConnected());
        EventUtils.postEvent(event);
    }

}
