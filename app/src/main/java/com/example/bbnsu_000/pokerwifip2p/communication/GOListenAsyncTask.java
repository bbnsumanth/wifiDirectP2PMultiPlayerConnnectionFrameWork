package com.example.bbnsu_000.pokerwifip2p.communication;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by bbnsu_000 on 8/30/2015.
 */

public class GOListenAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    public GOListenAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            /**
             * Create a server socket and wait for Client connections. This
             * call blocks until a connection is accepted from a Client
             * GO listen on 8888
             */
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket clientSocket = serverSocket.accept();

            /**
             * If this code is reached,  client has connected and transferred data
             * Save the input stream from the client
             */

            InputStream inputstream = clientSocket.getInputStream();
            //todo:get the player from input stream
            //todo:pass this to onPostExecute via result
            serverSocket.close();
            return null;
        } catch (IOException e) {
            return null;
        }
    }


    @Override
    protected void onPostExecute(Void result) {
        //todo:
        //now we received player from clients
        //update gamestate in the GO
        //start listening to other requests
        new GOListenAsyncTask(context).execute();
    }
}