package com.shuruta.sergey.ftpclient.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.cache.CacheManager;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.ui.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey Shuruta
 * 04.09.2015 at 13:20
 */
public abstract class FilesFragment extends Fragment {

    private RecyclerView mFileRecyclerView;
    private FFileAdapter mFileAdapter;

    private List<FFile> mFilesList = new ArrayList<>();
    private boolean canDo = true;
    private boolean isSelected = false;
    protected Context mContext;

    public static final int TYPE_LIST_FTP = 1;
    public static final int TYPE_LIST_LOCAL = 2;

    public static final String PARAM_LIST_SELECT = "select_list";
    public static final String TAG = FilesFragment.class.getSimpleName();

    public abstract void onBack();
    public abstract void onDirClick(FFile ftpFile);
    public abstract void onFileClick(FFile ftpFile);
    public abstract int getListType();

    private interface OnFileMenuClickListener {
        public void onMenuClick(View view, Connection connection);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
/*
        try {
            mActivityListener = (FtpFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FilesFragmentListener");
        }
*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files_list, null);
        mFileRecyclerView = (RecyclerView) view.findViewById(R.id.fileRecyclerView);
        mFileRecyclerView.setHasFixedSize(true);
        mFileRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFileRecyclerView.setLayoutManager(layoutManager);
        mFileAdapter = new FFileAdapter(mFilesList, new FileMenuClickListener());
        mFileRecyclerView.setAdapter(mFileAdapter);
        mFileRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFileRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isSelected) {
                    Log.d("TEST", "Send msg! " + getListType());
                    Bundle bundle = new Bundle();
                    bundle.putInt(PARAM_LIST_SELECT, getListType());
                    EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.SELECT_LIST));
                }
                return false;
            }
        });
        setSelected(getListType() == TYPE_LIST_FTP);
        return view;
    }

    public void onEvent(EventBusMessenger event) {
        Log.d("TEST", "onEvent: " + event.state);
        switch (event.state) {
            case SELECT_LIST:
                Log.d("TEST", "SELECT_LIST: " + event.bandle.getInt(PARAM_LIST_SELECT));
                setSelected(event.bandle.getInt(PARAM_LIST_SELECT) == getListType());
                break;
        }
    }

    private class FileMenuClickListener implements OnFileMenuClickListener {

        @Override
        public void onMenuClick(View view, final Connection connection) {

        }
    }

    private class FFileAdapter extends RecyclerView.Adapter<FFileAdapter.ViewHolder> {

        private List<FFile> files;
        private OnFileMenuClickListener listener;

        public FFileAdapter(List<FFile> files, OnFileMenuClickListener listener) {
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
            final FFile file = files.get(i);
            String name = file.getName().equals(".") ? getString(R.string.back) : file.getName();
            viewHolder.nameTextView.setText(name);
            viewHolder.sizTextView.setText(file.getFormatSize());
            viewHolder.iconImageView.setImageDrawable(file.getIcon());
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

            private TextView nameTextView, sizTextView;
            private ImageView iconImageView;

            public ViewHolder(View holderView) {
                super(holderView);
                this.nameTextView  = (TextView) holderView.findViewById(R.id.nameTextView);
                this.sizTextView   = (TextView) holderView.findViewById(R.id.sizeTextView);
                this.iconImageView = (ImageView) holderView.findViewById(R.id.iconImageView);
                holderView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(!isSelected) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PARAM_LIST_SELECT, getListType());
                    EventBus.getDefault().post(new EventBusMessenger(bundle, EventBusMessenger.State.SELECT_LIST));
                    return;
                }
                if(!canDo) return;
                FFile file = files.get(getPosition());
                if(file.isBackButton()) {
                    onBack();
                } else {
                    if(file.isDir())
                        onDirClick(file);
                    if(file.isFile())
                        onFileClick(file);
                }
            }
        }
    }

    private void setSelected(boolean isSelecetd) {
        // ToDo
        // mFileRecyclerView.setEnabled(isSelecetd);
        this.isSelected = isSelecetd;
    }

    protected void startReadList() {
        canDo = false;
    }

    protected void displayList(List<FFile> files) {
        Log.d("TEST", "displayList(" + files.size() + ")");
        mFilesList.clear();
        mFilesList.addAll(files);
        mFileAdapter.notifyDataSetChanged();
    }

    protected void errorReadList() {
        Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    protected void finishReadList() {
        canDo = true;
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

}
