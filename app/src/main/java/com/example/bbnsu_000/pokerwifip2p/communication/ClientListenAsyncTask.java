package com.example.bbnsu_000.pokerwifip2p.communication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.bbnsu_000.pokerwifip2p.Fragments.LobbyFragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by bbnsu_000 on 8/30/2015.
 */
public class ClientListenAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    public ClientListenAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            /**
             * Create a server socket and wait for GO connections. This
             * call blocks until a connection is accepted from a GO
             * clients listen on 8988
             */
            ServerSocket serverSocket = new ServerSocket(8988);
            Socket GOSocket = serverSocket.accept();

            /**
             * If this code is reached,  GO has connected and transferred data
             * Save the input stream from the GO
             */

            InputStream inputstream = GOSocket.getInputStream();
            //todo:get the state from input stream
            //todo:pass this to onPostExecute via result
            serverSocket.close();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Start activity that can handle the JPEG image
     */
    @Override
    protected void onPostExecute(Void result) {
        //todo:
        //now we received initial state from GO
        //start lobbyFragment with the initial state.
    }
}