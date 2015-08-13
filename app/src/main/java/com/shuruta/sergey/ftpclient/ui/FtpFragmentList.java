package com.shuruta.sergey.ftpclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shuruta.sergey.ftpclient.CacheManager;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.database.entity.Connection;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import it.sauronsoftware.ftp4j.FTPFile;

/**
 * Created by Sergey Shuruta
 * 08/15/15 at 22:11
 */
public class FtpFragmentList extends Fragment {

    private FtpFileAdapter ftpFileAdapter;
    private List<FTPFile> ftpFiles = new ArrayList<>();
    private FtpFragmentListener ftpFragmentListener;
    private RecyclerView fileRecyclerView;

    public static final String TAG = FtpFragmentList.class.getSimpleName();

    public interface FtpFragmentListener {
        public void onBack();
        public void onDirClick(FTPFile ftpFile);
        public void onFileClick(FTPFile ftpFile);
    }

    private interface OnFileMenuClickListener {
        public void onMenuClick(View view, Connection connection);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ftpFragmentListener = (FtpFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FtpFragmentListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventBusMessenger event) {
        if(null == fileRecyclerView) return;
        Log.d(TAG, "onEvent: " + event.state);
        switch (event.state) {
            case READ_LIST_START:
                fileRecyclerView.setEnabled(false);
                break;
            case READ_LIST_OK:
                fillAdapter();
                break;
            case READ_LIST_ERROR:
                Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            case READ_LIST_FINISH:
                fileRecyclerView.setEnabled(true);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ftplist, null);
        fileRecyclerView = (RecyclerView) view.findViewById(R.id.fileRecyclerView);

        fileRecyclerView.setHasFixedSize(true);
        fileRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fileRecyclerView.setLayoutManager(layoutManager);
        ftpFileAdapter = new FtpFileAdapter(ftpFiles, new FileMenuClickListener());
        fileRecyclerView.setAdapter(ftpFileAdapter);
        fileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fillAdapter();
        return view;
    }

    private void fillAdapter() {
        ftpFiles.clear();
        ftpFiles.addAll(CacheManager.getInstance().getFtpFiles());
        ftpFileAdapter.notifyDataSetChanged();
    }

    private class FileMenuClickListener implements OnFileMenuClickListener {

        @Override
        public void onMenuClick(View view, final Connection connection) {

        }
    }

    private class FtpFileAdapter extends RecyclerView.Adapter<FtpFileAdapter.ViewHolder> {

        private List<FTPFile> files;
        private OnFileMenuClickListener listener;

        public FtpFileAdapter(List<FTPFile> files, OnFileMenuClickListener listener) {
            this.files = files;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(inflater.inflate(R.layout.row_file, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            final FTPFile connection = files.get(i);
            String name = connection.getName().equals(".") ? getString(R.string.back) : connection.getName();
            viewHolder.nameTextView.setText(name);
/*
            viewHolder.menuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMenuClick(v, connection);
                }
            });
*/
        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView nameTextView;

            public ViewHolder(View holderView) {
                super(holderView);
                this.nameTextView  = (TextView)    holderView.findViewById(R.id.nameTextView);
                holderView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                FTPFile ftpFile = files.get(getPosition());
                if(ftpFile.getName().equals("."))
                    ftpFragmentListener.onBack();
                if(ftpFile.getType() == FTPFile.TYPE_DIRECTORY)
                    ftpFragmentListener.onDirClick(ftpFile);
                if(ftpFile.getType() == FTPFile.TYPE_FILE)
                    ftpFragmentListener.onFileClick(ftpFile);
            }
        }
    }
}
