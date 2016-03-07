package com.shuruta.sergey.ftpclient.ui.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.adapters.RecyclerFileAdapter;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.event.CommunicationEvent;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.interfaces.NavigationListener;
import com.shuruta.sergey.ftpclient.interfaces.OnFileClickListener;
import com.shuruta.sergey.ftpclient.interfaces.OnFileMenuClickListener;
import com.shuruta.sergey.ftpclient.services.FtpService;
import com.shuruta.sergey.ftpclient.ui.dialog.DownloadDialog;
import com.shuruta.sergey.ftpclient.ui.dialog.DownloadPresentDialog;
import com.shuruta.sergey.ftpclient.ui.dialog.FTPDialog;
import com.shuruta.sergey.ftpclient.utils.Utils;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey Shuruta
 * 04.09.2015 at 13:20
 */
public class FilesFragment extends Fragment implements NavigationListener,
        CommunicationEvent.ListReadEventListener,
        CommunicationEvent.ConnectionEventListener,
        CommunicationEvent.DownloadEventListener {

    private FtpService mFtpService;

    private TextView mPatchTextView;
    private RecyclerView mFileRecyclerView;
    private RecyclerFileAdapter mFileAdapter;

    private List<FFile> mFilesList = new ArrayList<>();
    private boolean isSelected;
    protected Context mContext;
    private int listType;
    private boolean bound;
    private DialogFragment currentDialog;

    public static final String TAG = FilesFragment.class.getSimpleName();
    public static final String LIST_TYPE = "list_type";
    public static final String IS_SELECTED = "is_selected";

    private class FileMenuClickListener implements OnFileMenuClickListener {

        @Override
        public void onMenuClick(View view, final FFile file) {
            PopupMenu popup = new PopupMenu(mContext, view);
            popup.getMenuInflater().inflate(R.menu.menu_file, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.download:
                            mFtpService.download(file
                                    , CustomApplication.getInstance().getPath(Constants.TYPE_FTP)
                                    , CustomApplication.getInstance().getPath(Constants.TYPE_LOCAL));
                            break;
                        case R.id.remove:

                            break;
                        case R.id.rename:

                            break;
                        case R.id.properties:

                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listType = getArguments().getInt(LIST_TYPE, Constants.TYPE_FTP);
        this.isSelected = getArguments().getBoolean(IS_SELECTED, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mContext.bindService(new Intent(mContext, FtpService.class), mServiceConnection, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (!bound) return;
        mContext.unbindService(mServiceConnection);
        bound = false;
    }

    @Override
    public void onEventMainThread(CommunicationEvent event) {
        if(event.type != CommunicationEvent.Type.FILE_DOWNLOAD)
            Log.d("TEST", "event: " + event.type + " " + event.state);
        // TODO Relocate listeners to activity (in this case we have 2 events)
        event.setListener((CommunicationEvent.ListReadEventListener) FilesFragment.this);
        event.setListener((CommunicationEvent.ConnectionEventListener) FilesFragment.this);
        event.setListener((CommunicationEvent.DownloadEventListener) FilesFragment.this);
    }

    @Override
    public void onDownloadStart() {
        if(null == currentDialog) {
            currentDialog = DownloadDialog.show(getActivity());
            Log.d("DownloadTask", "onDownloadStart()");
        }
    }

    @Override
    public void onDownloadError(String message) {
        if(null != currentDialog) {
            currentDialog.dismiss();
        }
        Log.d("DownloadTask", "onDownloadError(" + message + ")");
        currentDialog = DownloadPresentDialog.show(getActivity());
    }

    @Override
    public void onDownloadFinish() {
        if(null != currentDialog) {
            currentDialog.dismiss();
        }
        Log.d("DownloadTask", "onDownloadFinish()");
    }

    @Override
    public void onConnectionStart(Connection connection) {
        currentDialog = FTPDialog.showProgressDialog(getActivity(),
                getString(R.string.connecting_to_server_x,
                        CustomApplication.getInstance().getCurrentConnection().getName()));
    }

    @Override
    public void onConnectionError(Connection connection, String message) {
        if(null != currentDialog)
            currentDialog.dismiss();
        // Show Dialog reconnect
    }

    @Override
    public void onConnectionFinish(Connection connection) {
        if(null != currentDialog)
            currentDialog.dismiss();
    }

    @Override
    public void onListReadStart(int listTpe) {
        if(this.listType != listTpe) return;
        deactivateList();
    }

    @Override
    public void onListReadError(int listTpe, String message) {
        if(this.listType != listTpe) return;
        activateList();
        if(Utils.isNetworkConnected(mContext)) {
            currentDialog = FTPDialog.showReconnectDialog(getActivity(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFtpService.startConnection(
                                    CustomApplication.getInstance().getCurrentConnection());
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFtpService.disconnect();
                        }
                    });
        } else {
            FTPDialog.showNoInternetMessage(getActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFtpService.disconnect();
                }
            });
        }
    }

    @Override
    public void onListReadFinish(int listTpe) {
        if(this.listType != listTpe) return;
        displayList();
    }

    @Override
    public void onListSelect(int listTpe) {
        setSelected(listTpe == this.listType);
    }

/*    public void onEventMainThread(EventBusMessenger event) {

        if(event.event.equals(EventBusMessenger.Event.SELECT)) {
            setSelected(event.getType() == this.listType);
            return;
        }

        if(!event.isValidListType(this.listType)) return;
        switch (event.event) {
*//*            case START:
                mFileAdapter.setIsActive(false);
                break;
            case OK:
                displayList();
                break;
            case ERROR:
                errorsProcessing();
            case FINISH:
                mFileAdapter.setIsActive(true);
                break;*//*
            case START_PREDOWNLOAD:
                // Show indeterminate progress dialog, if present - update
                break;
            case ERROR_PREDOWNLOAD:
                // Hide indeterminate progress dialog
                // Show error dialog
                break;
            case FINISH_PREDOWNLOAD:
                if(CacheManager.getInstance().isHasDownload()) {
                    mFtpConnectionService.download();
                }
                // Hide if present indeterminate progress dialog
                // If has download entities - Start download
                break;
            case START_DOWNLOAD:
                // Show progress dialog
                break;
            case ERROR_DOWNLOAD:
                // Hide progress dialog
                // Show error dialog
                break;
            case FINISH_DOWNLOAD:
                // Hide progress dialog
                // If has download entities - Start download
                break;
        }
    }*/

    @Override
    public void onListRefresh() {
        if(!isSelected) return;
        mFtpService.readList(listType);
        currentDialog = FTPDialog.showProgressDialog(getActivity(),R.string.refresh);
    }

    @Override
    public void onListBack() {
        if(!isSelected) return;
        CustomApplication.getInstance().setPath(listType,
                Utils.backDir(CustomApplication.getInstance().getPath(listType)));
        mFtpService.readList(listType);
    }

    @Override
    public void onDisconnect() {
        if(Constants.TYPE_FTP != listType) return;
        mFtpService.disconnect();
    }

    @Override
    public void readList() {
        mFtpService.readList(listType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, null);
        mPatchTextView = (TextView) view.findViewById(R.id.patchTextView);
        mFileRecyclerView = (RecyclerView) view.findViewById(R.id.fileRecyclerView);
        mFileRecyclerView.setHasFixedSize(true);
        //mFileRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFileRecyclerView.setLayoutManager(layoutManager);
        mFileAdapter = new RecyclerFileAdapter(mContext, mFilesList, new OnFileClickListener() {
            @Override
            public void onFileClick(View view, FFile file) {
                if (!isSelected) {
                    CommunicationEvent.selectFilesPanel(FilesFragment.this.listType);
                    return;
                }
                if (!mFileAdapter.isActiveState()) return;
                if (file.isBackButton()) {
                    CustomApplication.getInstance().setPath(listType,
                            Utils.backDir(CustomApplication.getInstance().getPath(listType)));
                    mFtpService.readList(listType);
                } else {
                    if (file.isDir()) {
                        CustomApplication.getInstance().setPath(listType,
                                Utils.addDir(file.getName(), CustomApplication.getInstance().getPath(listType)));

                        mFtpService.readList(listType);
                    } else if (file.isFile()) {
                        // on file click
                    }
                }

            }
        }, new FileMenuClickListener());
        activateList();
        mFileRecyclerView.setAdapter(mFileAdapter);
        mFileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFileRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isSelected) {
                    CommunicationEvent.selectFilesPanel(FilesFragment.this.listType);
                }
                return false;
            }
        });
        setSelected(isSelected);
        return view;
    }

    private void deactivateList() {
        mFileAdapter.setIsActive(false);
    }

    private void activateList() {
        mFileAdapter.setIsActive(true);
        if(null != currentDialog)
            currentDialog.dismiss();
    }

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        mFileRecyclerView.setBackgroundColor(getResources().getColor(this.isSelected ? R.color.recyclerview_selected : R.color.recyclerview_not_selected));
    }

    private void displayList() {
        mFilesList.clear();
        mFilesList.addAll(CacheManager.getInstance().getFiles(listType));
        mFileAdapter.notifyDataSetChanged();
        mPatchTextView.setText(CustomApplication.getInstance().getPath(listType));
        activateList();
    }

/*
    private void addDir(String dir) {
        if(dir.isEmpty()) return;
        String path = CustomApplication.getInstance().getPath(listType);
        String[] dirs = path.split(File.separator);
        if(0 != dirs.length && dirs[dirs.length - 1].equals(dir)) return;
        path += dir + File.separator;
        CustomApplication.getInstance().setPath(listType, path);
    }
*/

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "onServiceConnected()");
            mFtpService = ((FtpService.ConnectionBinder) binder).getService();
            mFtpService.readList(listType);
            bound = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
            bound = false;
        }
    };

}
