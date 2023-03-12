 package com.exloringembeddedsystems.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG_MA" ;

    Button buttonSendMessage;
    Button buttonBTConnect;
    Button buttonRight;
    Button buttonLeft;
    Button buttonBack;
    Button buttonForward;
    ToggleButton buttonGrip;




    EditText editTextSentMessage;


    Spinner spinnerBTPairedDevices;





    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket BTSocket = null;
    BluetoothAdapter BTAdaptor = null;
    Set<BluetoothDevice> BTPairedDevices = null;
    boolean bBTConnected = false;
    BluetoothDevice BTDevice = null;
    classBTInitDataCommunication cBTInitSendReceive =null;

    static public final int BT_CON_STATUS_NOT_CONNECTED     =0;
    static public final int BT_CON_STATUS_CONNECTING        =1;
    static public final int BT_CON_STATUS_CONNECTED         =2;
    static public final int BT_CON_STATUS_FAILED            =3;
    static public final int BT_CON_STATUS_CONNECTiON_LOST   =4;
    static public int iBTConnectionStatus = BT_CON_STATUS_NOT_CONNECTED;

    static final int BT_STATE_LISTENING            =1;
    static final int BT_STATE_CONNECTING           =2;
    static final int BT_STATE_CONNECTED            =3;
    static final int BT_STATE_CONNECTION_FAILED    =4;
    static final int BT_STATE_MESSAGE_RECEIVED     =5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate-Start");


        editTextSentMessage = findViewById(R.id.idMAEditTextSendMessage);

        spinnerBTPairedDevices = findViewById(R.id.idMASpinnerBTPairedDevices);

        buttonSendMessage = findViewById(R.id.idMAButtonSendData);
        buttonBTConnect = findViewById(R.id.idMAButtonConnect);
        buttonRight = findViewById(R.id.btn_droite);
        buttonLeft = findViewById(R.id.btn_gauche);
        buttonBack = findViewById(R.id.btn_bas);
        buttonForward = findViewById(R.id.btn_haut);
        buttonGrip = findViewById(R.id.tgl_on_off);


        //tvReceivedMessage.setText("App Loaded");

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Send Button clicked");
                String sMessage = editTextSentMessage.getText().toString();
                Log.d(TAG, "Message = " + sMessage);
                sendMessage(sMessage);

            }
        });


        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Click buttonMemory1");
                sendMessage("D");
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Click buttonMemory1");
                sendMessage("G");
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Click buttonMemory1");
                sendMessage("B");
            }
        });

        buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Click buttonMemory1");
                sendMessage("H");
            }
        });



        buttonGrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Le code qui doit s'exécuter lorsque l'état du bouton change
                if (isChecked) {
                    // Le bouton est activé
                    sendMessage("O");
                } else {
                    // Le bouton est désactivé
                    sendMessage("F");
                }
            }
        });







        buttonBTConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Click buttonBTConnect");

                if(bBTConnected==false) {
                    if (spinnerBTPairedDevices.getSelectedItemPosition() == 0) {
                        Log.d(TAG, "Please select BT device");
                        Toast.makeText(getApplicationContext(), "Please select Bluetooth Device", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String sSelectedDevice = spinnerBTPairedDevices.getSelectedItem().toString();
                    Log.d(TAG, "Selected device = " + sSelectedDevice);

                    for (BluetoothDevice BTDev : BTPairedDevices) {
                        if (sSelectedDevice.equals(BTDev.getName())) {
                            BTDevice = BTDev;
                            Log.d(TAG, "Selected device UUID = " + BTDevice.getAddress());

                            cBluetoothConnect cBTConnect = new cBluetoothConnect(BTDevice);
                            cBTConnect.start();

                        }
                    }
                }
                else {
                    Log.d(TAG, "Disconnecting BTConnection");
                    if(BTSocket!=null && BTSocket.isConnected())
                    {
                        try {
                            BTSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(TAG, "BTDisconnect Exp " + e.getMessage());
                        }
                    }
                    buttonBTConnect.setText("Connect");
                    bBTConnected = false;

                }



            }
        });
    }







    public class cBluetoothConnect extends Thread
    {
        private BluetoothDevice device;


        public cBluetoothConnect (BluetoothDevice BTDevice)
        {
            Log.i(TAG, "classBTConnect-start");

            device = BTDevice;
            try{
                BTSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (Exception exp)
            {
                Log.e(TAG, "classBTConnect-exp" + exp.getMessage());
            }
        }

        public void run()
        {
            try {
                BTSocket.connect();
                Message message=Message.obtain();
                message.what=BT_STATE_CONNECTED;
                handler.sendMessage(message);


            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=BT_STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }

    }


    public class classBTInitDataCommunication extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private  InputStream inputStream =null;
        private  OutputStream outputStream=null;

        public classBTInitDataCommunication (BluetoothSocket socket)
        {
            Log.i(TAG, "classBTInitDataCommunication-start");

            bluetoothSocket=socket;


            try {
                inputStream=bluetoothSocket.getInputStream();
                outputStream=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "classBTInitDataCommunication-start, exp " + e.getMessage());
            }


        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (BTSocket.isConnected())
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(BT_STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "BT disconnect from decide end, exp " + e.getMessage());
                    iBTConnectionStatus=BT_CON_STATUS_CONNECTiON_LOST;
                    try {
                        //disconnect bluetooth
                        Log.d(TAG, "Disconnecting BTConnection");
                        if(BTSocket!=null && BTSocket.isConnected())
                        {

                                BTSocket.close();
                        }
                        buttonBTConnect.setText("Connect");
                        bBTConnected = false;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case BT_STATE_LISTENING:
                    Log.d(TAG, "BT_STATE_LISTENING");
                    break;
                case BT_STATE_CONNECTING:
                    iBTConnectionStatus = BT_CON_STATUS_CONNECTING;
                    buttonBTConnect.setText("Connecting..");
                    Log.d(TAG, "BT_STATE_CONNECTING");
                    break;
                case BT_STATE_CONNECTED:

                    iBTConnectionStatus = BT_CON_STATUS_CONNECTED;

                    Log.d(TAG, "BT_CON_STATUS_CONNECTED");
                    buttonBTConnect.setText("Disconnect");

                    cBTInitSendReceive = new classBTInitDataCommunication(BTSocket);
                    cBTInitSendReceive.start();

                    bBTConnected = true;
                    break;
                case BT_STATE_CONNECTION_FAILED:
                    iBTConnectionStatus = BT_CON_STATUS_FAILED;
                    Log.d(TAG, "BT_STATE_CONNECTION_FAILED");
                    bBTConnected = false;
                    break;

                case BT_STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    Log.d(TAG, "Message receive ( " + tempMsg.length() + " )  data : " + tempMsg);

                   // tvReceivedMessage.append(tempMsg);


                    break;

            }
            return true;
        }
    });


    public void sendMessage(String sMessage)
    {
        if( BTSocket!= null && iBTConnectionStatus==BT_CON_STATUS_CONNECTED)
        {
            if(BTSocket.isConnected() )
            {
                try {
                    cBTInitSendReceive.write(sMessage.getBytes());
                    //tvReceivedMessage.append("\r\n-> " + sMessage);
                }
                catch (Exception exp)
                {

                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please connect to bluetooth", Toast.LENGTH_SHORT).show();
            //tvReceivedMessage.append("\r\n Not connected to bluetooth");
        }

    }

    void getBTPairedDevices()
    {
        Log.d(TAG, "getBTPairedDevices - start ");
        BTAdaptor = BluetoothAdapter.getDefaultAdapter();
        if(BTAdaptor == null)
        {
            Log.e(TAG, "getBTPairedDevices , BTAdaptor null ");
            editTextSentMessage.setText("\nNo Bluetooth Device in the phone");
            return;

        }
        else if(!BTAdaptor.isEnabled())
        {
            Log.e(TAG, "getBTPairedDevices , BT not enabled");
            editTextSentMessage.setText("\nPlease turn ON Bluetooth");
            return;
        }

        BTPairedDevices = BTAdaptor.getBondedDevices();
        Log.d(TAG, "getBTPairedDevices , Paired devices count = " + BTPairedDevices.size());

        for (BluetoothDevice BTDev : BTPairedDevices)
        {
            Log.d(TAG, BTDev.getName() + ", " + BTDev.getAddress());
        }


    }

    void populateSpinnerWithBTPairedDevices()
    {
        ArrayList<String> alPairedDevices = new ArrayList<>();
        alPairedDevices.add("Select");
        for (BluetoothDevice BTDev : BTPairedDevices)
        {
            alPairedDevices.add(BTDev.getName());
        }
        final ArrayAdapter<String> aaPairedDevices = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,alPairedDevices);
        aaPairedDevices.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerBTPairedDevices.setAdapter(aaPairedDevices);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume-Resume");

        getBTPairedDevices();
        populateSpinnerWithBTPairedDevices();
        //readAllData();


    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause-Start");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy-Start");
    }
}