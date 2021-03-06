package com.shuruta.sergey.ftpclient.ui.activitys;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.ui.DialogFactory;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity implements CommunicationEvent.ConnectionEventListener {

    private ConnectionsAdapter connectionsAdapter;
    private List<Connection> connections = new ArrayList<>();
    private boolean bound = false;
    private FtpService mFtpConnectionService;
    private LoadConnections loadConnections;

    public static final String TAG = MainActivity.class.getSimpleName();

    private interface OnConnectionMenuClickListener {
        public void onMenuClick(View view, Connection connection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setupToolBar(R.string.app_name, getString(R.string.list_of_connections), null);
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_connection:
                startActivity(new Intent(MainActivity.this, AddConActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initUI() {
        RecyclerView conRecyclerView = (RecyclerView) findViewById(R.id.conRecyclerView);
        conRecyclerView.setHasFixedSize(true);
        //conRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conRecyclerView.setLayoutManager(layoutManager);
        connectionsAdapter = new ConnectionsAdapter(connections, new ConnectionMenuClickListener());
        conRecyclerView.setAdapter(connectionsAdapter);
        conRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onEventMainThread(CommunicationEvent event) {
        event.setListener(MainActivity.this);
    }

    @Override
    public void onConnectionStart(Connection connection) {
        connection.setActive(true);
        connectionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnectionError(Connection connection, String message) {
        connection.setActive(false);
        connectionsAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFinish(Connection connection) {
        connection.setActive(false);
        connectionsAdapter.notifyDataSetChanged();
        CustomApplication.getInstance().setCurrentConnection(connection);
        startActivity(new Intent(MainActivity.this, FilesActivity.class));
    }


/*    public void onEventMainThread(EventBusMessenger event) {
        if(event.isValidListType(Constants.TYPE_CONNECTION)) {
            Log.d(TAG, "onEvent: " + event.event + " for connections");
            long connectionId = event.bundle.getLong(Constants.PARAM_CONNECTION_ID, 0);
            if(0 == connectionId) return;
            Connection connection = null;
            for(int position = 0; position < connections.size(); position++)
                if(connections.get(position).getId() == connectionId) {
                    connection = connections.get(position);
                    break;
                }
            if(null == connection) return;
            switch (event.event) {
                case START:
                    connection.setActive(true);
                    connectionsAdapter.notifyDataSetChanged();
                    break;
                case OK:
                    CustomApplication.getInstance().setCurrentConnection(connection);
                    mFtpConnectionService.readList(Constants.TYPE_FTP);
                    break;
                case ERROR:
                    Toast.makeText(MainActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                    break;
                case FINISH:
                    connection.setActive(false);
                    connectionsAdapter.notifyDataSetChanged();
                    break;
            }
        }

        if(event.isValidListType(Constants.TYPE_FTP)) {
            Log.d(TAG, "onEvent: " + event.event + " for ftp");
            switch (event.event) {
                case START:
                    break;
                case OK:
                    startActivity(new Intent(MainActivity.this, FilesActivity.class));
                    break;
                case ERROR:
                    Toast.makeText(MainActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MainActivity.this, FtpService.class), mServiceConnection, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        loadConnections = new LoadConnections();
        loadConnections.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!loadConnections.isCancelled()) {
            loadConnections.cancel(false);
        }
        if (!bound) return;
        unbindService(mServiceConnection);
        bound = false;
    }

    private class ConnectionMenuClickListener implements OnConnectionMenuClickListener {

        @Override
        public void onMenuClick(View view, final Connection connection) {
            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.menu_connection, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.edit:
                            Intent intent = new Intent(MainActivity.this, AddConActivity.class);
                            intent.putExtra(AddConActivity.CONNECTION_ID, connection.getId());
                            startActivity(intent);
                            break;
                        case R.id.remove:
                            DialogFactory.showDialog(MainActivity.this, R.string.connection_delete_ask, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CustomApplication.getInstance().getDatabaseAdapter().removeConnection(connection.getId());
                                    loadConnections = new LoadConnections();
                                    loadConnections.execute();
                                }
                            });
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private class LoadConnections extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            connections.clear();
            connections.addAll(CustomApplication.getInstance().getDatabaseAdapter().getConnections());
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            connectionsAdapter.notifyDataSetChanged();
        }
    }

    private class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ViewHolder> {

        private List<Connection> connections;
        private OnConnectionMenuClickListener listener;

        public ConnectionsAdapter(List<Connection> connections, OnConnectionMenuClickListener listener) {
            this.connections = connections;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(inflater.inflate(R.layout.row_connection, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final Connection connection = connections.get(i);
            viewHolder.nameTextView.setText(connection.getName());
            viewHolder.descTextView.setText(connection.getHost());
            viewHolder.menuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMenuClick(v, connection);
                }
            });
            viewHolder.progressBar.setVisibility(connection.isActive() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return connections.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView nameTextView, descTextView;
            private ImageView menuImageView;
            private ProgressBar progressBar;

            public ViewHolder(View holderView) {
                super(holderView);
                this.nameTextView  = (TextView)    holderView.findViewById(R.id.nameTextView);
                this.descTextView  = (TextView)    holderView.findViewById(R.id.descTextView);
                this.menuImageView = (ImageView)   holderView.findViewById(R.id.menuImageView);
                this.progressBar   = (ProgressBar) holderView.findViewById(R.id.progressBar);
                holderView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mFtpConnectionService.startConnection(connections.get(getAdapterPosition()));
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "MainActivity.onServiceConnected()");
            mFtpConnectionService = ((FtpService.ConnectionBinder) binder).getService();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "MainActivity.onServiceDisconnected()");
            bound = false;
        }
    };

}
