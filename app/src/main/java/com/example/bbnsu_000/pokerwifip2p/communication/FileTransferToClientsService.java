package com.example.bbnsu_000.pokerwifip2p.communication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by bbnsu_000 on 8/30/2015.
 */


        import android.app.IntentService;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

import com.example.bbnsu_000.pokerwifip2p.Game.GameState;

import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.net.InetSocketAddress;
        import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by bbnsu_000 on 8/30/2015.
 */
public class FileTransferToClientsService extends IntentService {
    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_STATE = "com.example.bbnsu_000.pokerwifip2p.SEND_STATE";
    public static final String EXTRAS_DATA = "data_to_send";
    public static final String EXTRAS_CLIENT_ADDRESS_LIST = "go_host";
    public static final String EXTRAS_CLIENT_PORT = "go_port";

    public FileTransferToClientsService(String name) {
        super(name);
    }

    public FileTransferToClientsService() {
        super("FileTransferToClientsService");
    }

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();
        if (intent.getAction().equals(ACTION_SEND_STATE)) {
            GameState state = intent.getExtras().getParcelable(EXTRAS_DATA);

            ArrayList<CharSequence> host = intent.getCharSequenceArrayListExtra(EXTRAS_CLIENT_ADDRESS_LIST);
            int port = intent.getExtras().getInt(EXTRAS_CLIENT_PORT);

            Socket socket = new Socket();

            try {

                socket.bind(null);
                for(int i =0;i < host.size();i++){
                    socket.connect((new InetSocketAddress(host.get(i).toString(), port)), SOCKET_TIMEOUT);
                    OutputStream stream = socket.getOutputStream();
                    //todo:write state to stream
                    stream.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // Give up
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
