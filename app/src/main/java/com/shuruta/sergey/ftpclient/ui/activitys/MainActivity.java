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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.ui.DialogFactory;
import com.shuruta.sergey.ftpclient.ui.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    private ConnectionsAdapter connectionsAdapter;
    private List<Connection> connections = new ArrayList<>();
    private boolean bound = false;
    private FtpService mFtpConnectionService;

    public static final String TAG = MainActivity.class.getSimpleName();

    private interface OnConnectionMenuClickListener {
        public void onMenuClick(View view, Connection connection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setupToolBar(R.drawable.ic_launcher, R.string.app_name, R.string.list_of_connections, R.menu.menu_start, MainActivity.this);
        initUI();
    }

    public void initUI() {
        RecyclerView conRecyclerView = (RecyclerView) findViewById(R.id.conRecyclerView);
        conRecyclerView.setHasFixedSize(true);
        conRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conRecyclerView.setLayoutManager(layoutManager);
        connectionsAdapter = new ConnectionsAdapter(connections, new ConnectionMenuClickListener());
        conRecyclerView.setAdapter(connectionsAdapter);
        conRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void onEventMainThread(EventBusMessenger event) {
        Connection connection = null;
        for(int position = 0; position < connections.size(); position++)
            if(connections.get(position).getId() == event.conId) {
                connection = connections.get(position);
                break;
            }
        if(null == connection) return;
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
            case CONNECTION_START:
                connection.setActive(true);
                connectionsAdapter.notifyDataSetChanged();
                break;
            case CONNECTION_OK:
                mFtpConnectionService.readList();
                break;
            case CONNECTION_ERROR:
                Toast.makeText(MainActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                connection.setActive(false);
                connectionsAdapter.notifyDataSetChanged();
                break;
            case CONNECTION_FINISH:
                break;
            case READ_FTP_LIST_START:
                break;
            case READ_FTP_LIST_OK:
                startActivity(new Intent(MainActivity.this, FilesActivity.class));
                break;
            case READ_FTP_LIST_ERROR:
                Toast.makeText(MainActivity.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                break;
            case READ_FTP_LIST_FINISH:
                connection.setActive(false);
                connectionsAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        new LoadConnections().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.new_connection:
                startActivity(new Intent(MainActivity.this, AddConActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
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
                            DialogFactory.showDialog(MainActivity.this, R.string.delete_connection_ask, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CustomApplication.getInstance().getDatabaseAdapter().removeConnection(connection.getId());
                                    new LoadConnections().execute();
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
                mFtpConnectionService.startConnection(connections.get(getPosition()));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MainActivity.this, FtpService.class), mServiceConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;
        unbindService(mServiceConnection);
        bound = false;
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
