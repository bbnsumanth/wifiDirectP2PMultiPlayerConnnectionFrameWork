package com.example.bbnsu_000.pokerwifip2p;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bbnsu_000.pokerwifip2p.Fragments.ClientListFragmentGO;
import com.example.bbnsu_000.pokerwifip2p.Fragments.DetailsFragmentClient;
import com.example.bbnsu_000.pokerwifip2p.Fragments.DetailsFragmentGO;
import com.example.bbnsu_000.pokerwifip2p.Fragments.LobbyFragment;
import com.example.bbnsu_000.pokerwifip2p.Fragments.StartScreenFragment;
import com.example.bbnsu_000.pokerwifip2p.Fragments.StartScreenFragment.OnFragmentInteractionListener;
import com.example.bbnsu_000.pokerwifip2p.Fragments.WaitingFragmentClient;
import com.example.bbnsu_000.pokerwifip2p.Game.GameState;
import com.example.bbnsu_000.pokerwifip2p.Game.Player;
import com.example.bbnsu_000.pokerwifip2p.communication.ClientListenAsyncTask;
import com.example.bbnsu_000.pokerwifip2p.communication.FileTransferToClientsService;
import com.example.bbnsu_000.pokerwifip2p.communication.FileTransferToGOService;
import com.example.bbnsu_000.pokerwifip2p.communication.GOListenAsyncTask;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends ActionBarActivity implements
        OnFragmentInteractionListener,
        ClientListFragmentGO.OnFragmentInteractionListener,
        DetailsFragmentGO.OnFragmentInteractionListener,
        DetailsFragmentClient.OnFragmentInteractionListener,
        LobbyFragment.OnFragmentInteractionListener{
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private WiFiDirectBroadcastReceiver receiver;
    private Boolean wifiP2pEnabled;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    public StartScreenFragment startFragment;
    public DetailsFragmentGO detailsFragment;
    public ClientListFragmentGO clientListFragment;
    public LobbyFragment lobbyFragment;
    public DetailsFragmentClient detailsFragmentClient;
    public GameState gameState = new GameState();
    public Player thisPlayer;
    private WaitingFragmentClient waitingFragmentClient;
    public Boolean isGO;
    public WifiP2pDevice GO;
    private String networkName;
    private InetAddress GOInetAddress = null;
    private ArrayList<String> clientAddress = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        startFragment = new StartScreenFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction .add(R.id.fragment_container, startFragment, "start_fragment");
        mFragmentTransaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);

    }
    public void onPause(){
        super.onPause();
        unregisterReceiver(receiver);

    }

    public void setIsWifiP2pEnabled(Boolean bool){
        wifiP2pEnabled = bool;
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateGroupButton() {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("group created successfully");
                mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup group) {
                        if (group != null) {
                            GO = group.getOwner();
                            isGO = group.isGroupOwner();
                            networkName = group.getNetworkName();
                        } else {
                            Toast.makeText(MainActivity.this, "group is null", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                detailsFragment = new DetailsFragmentGO();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, detailsFragment, "details_fragment");
                mFragmentTransaction.commit();
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(MainActivity.this, "create group failed,turn off and turn on your device wifi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onJoinGroupButton() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "discovery success", Toast.LENGTH_SHORT).show();
                System.out.println("discover success");
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method, detailed below.
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(MainActivity.this, "discovery failed", Toast.LENGTH_SHORT).show();
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
            }
        });

    }

    @Override
    public void onStartButton(String name,int buyin) {
        thisPlayer = new Player(name,buyin);
        gameState.addPlayer(thisPlayer);

        clientListFragment = new ClientListFragmentGO();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container,clientListFragment,"details_fragment");
        mFragmentTransaction.commit();

        /**
         * async task to hear from clients
         * when an updaste is posted => process it and turn on the async task again in the postExecute.
         */
        new GOListenAsyncTask(this).execute();

    }


    @Override
    public void onEnterLobbyButton() {


        /**
         * after all the clients get connected,get the address of all the clients and
         * send them the initial state who are listening on 8988
         * pass the state to lobbyFragment(gaming window) and open it.
         */
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                Collection<WifiP2pDevice> clientList = group.getClientList();
                ArrayList<WifiP2pDevice> clients = new ArrayList<WifiP2pDevice>();
                clients.addAll(clientList);
                for(int i = 0;i < clients.size();i++){
                    clientAddress.add(clients.get(i).deviceAddress);
                }
                Intent serviceIntent = new Intent(MainActivity.this, FileTransferToClientsService.class);
                serviceIntent.setAction(FileTransferToClientsService.ACTION_SEND_STATE);
                serviceIntent.putExtra(FileTransferToClientsService.EXTRAS_DATA, (Parcelable) gameState);
                serviceIntent.putExtra(FileTransferToClientsService.EXTRAS_CLIENT_ADDRESS_LIST,
                        clientAddress);
                serviceIntent.putExtra(FileTransferToClientsService.EXTRAS_CLIENT_PORT, 8988);
                startService(serviceIntent);

            }
        });


//todo:lobbyFragment is not implemented,mostly unity Ui will come here
        lobbyFragment = new LobbyFragment();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container,lobbyFragment,"details_fragment");
        mFragmentTransaction.commit();
    }

    public void connectToGO(Collection<WifiP2pDevice> deviceList) {
        WifiP2pDevice groupOwner = null;
        ArrayList<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
        peers.addAll(deviceList);
        int i = 0 ;
        while(i < peers.size() && groupOwner ==null){
            if(peers.get(i).isGroupOwner() == true){
                groupOwner = peers.get(i);
            }
            i =i+1 ;
        }
        //create a config obj to pass to mManager.connect()
        WifiP2pConfig config = new WifiP2pConfig();
        //deviceAddress : device MAC address uniquely identifies a Wi-Fi p2p device
        if(groupOwner != null){
            GO = groupOwner;
            isGO = false;
            config.deviceAddress = groupOwner.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    System.out.println("connected to group successfully");
                    detailsFragmentClient  = new DetailsFragmentClient();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.fragment_container,detailsFragmentClient,"details_fragment_client");
                    mFragmentTransaction.commit();

                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            System.out.println("no group found to connect");
        }
    }

    @Override
    public void onClientStartButton(String name, int buyin) {
        thisPlayer = new Player(name,buyin);
        /**
         * this player info is send to the GO who is listening on 8888
         * to send info open socket and connect to group owner and send data. this is done in service.
         */
        if(GO != null){
           String address = GO.deviceAddress;
            Intent serviceIntent = new Intent(MainActivity.this, FileTransferToGOService.class);
            serviceIntent.setAction(FileTransferToGOService.ACTION_SEND_PLAYER);
            serviceIntent.putExtra(FileTransferToGOService.EXTRAS_DATA, thisPlayer.toString());
            serviceIntent.putExtra(FileTransferToGOService.EXTRAS_GROUP_OWNER_ADDRESS,
                    address);
            serviceIntent.putExtra(FileTransferToGOService.EXTRAS_GROUP_OWNER_PORT, 8888);
            startService(serviceIntent);
        }else{
            System.out.println("GroupOwner is null");
        }
        /*
        //another way to do the same
        mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                GOInetAddress = info.groupOwnerAddress;
                isGO = info.isGroupOwner;
                Intent serviceIntent = new Intent(MainActivity.this, FileTransferToGOService.class);
                serviceIntent.setAction(FileTransferToGOService.ACTION_SEND_PLAYER);
                serviceIntent.putExtra(FileTransferToGOService.EXTRAS_DATA, thisPlayer.toString());
                serviceIntent.putExtra(FileTransferToGOService.EXTRAS_GROUP_OWNER_ADDRESS,
                        GOInetAddress);
                serviceIntent.putExtra(FileTransferToGOService.EXTRAS_GROUP_OWNER_PORT, 8988);
                startService(serviceIntent);
            }
        });
*/

        waitingFragmentClient = new WaitingFragmentClient();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, waitingFragmentClient, "details_fragment");
        mFragmentTransaction.commit();
        /**
         * open server socket to receive initialState from GO,do this in async task
         * in the onPostExecute after reciving initial state start lobbyFragment.
         */
        new ClientListenAsyncTask(this).execute();


    }

    @Override
    public void onLobbyInteraction() {

    }

}
