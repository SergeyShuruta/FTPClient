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

import com.shuruta.sergey.ftpclient.Constants;
import com.shuruta.sergey.ftpclient.CustomApplication;
import com.shuruta.sergey.ftpclient.EventBusMessenger;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.entity.Connection;
import com.shuruta.sergey.ftpclient.ui.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sergey Shuruta
 * 04.09.2015 at 13:20
 */
public abstract class FilesFragment extends Fragment {

    private TextView mPatchTextView;
    private RecyclerView mFileRecyclerView;
    private FFileAdapter mFileAdapter;

    private List<FFile> mFilesList = new ArrayList<>();
    private boolean canDo = true;
    private boolean isSelected = false;
    protected Context mContext;
    private int listType;

    public static final String TAG = FilesFragment.class.getSimpleName();

    public abstract List<FFile> getFiles();
    public abstract void readList(String patch);
    public abstract void onFileClick(FFile ftpFile);

    public void disconnect() {

    }

    private interface OnFileMenuClickListener {
        public void onMenuClick(View view, Connection connection);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    public void initList(int listType) {
        this.listType = listType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files_list, null);
        mPatchTextView = (TextView) view.findViewById(R.id.patchTextView);
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
                    EventBusMessenger.sendMessage(FilesFragment.this.listType, EventBusMessenger.Event.SELECT);
                }
                return false;
            }
        });
        setSelected(this.listType == Constants.TYPE_FTP);
        return view;
    }

    public void onEventMainThread(EventBusMessenger event) {

        if(event.event.equals(EventBusMessenger.Event.SELECT)) {
            setSelected(event.getType() == this.listType);
            return;
        }

        if(!event.isValidListType(this.listType)) return;
        Log.d("TEST", "Event for: " + this.listType);
        switch (event.event) {
            case REFRESH:
                if(!isSelected) break;
                readList(CustomApplication.getInstance().getPath(listType));
                break;
            case BACK:
                if(!isSelected) break;
                backDir();
                readList(CustomApplication.getInstance().getPath(listType));
                break;
            case START:
                canDo = false;
                break;
            case OK:
                displayList();
                break;
            case ERROR:
                if(!isSelected) break;
                backDir();
                Toast.makeText(getActivity(),
                        this.listType == Constants.TYPE_FTP
                        ? R.string.connection_error
                        : R.string.read_error,
                        Toast.LENGTH_SHORT).show();
            case FINISH:
                canDo = true;
                break;
            case CLOSE:
                disconnect();
                break;
            case DISCONNECT_ERROR:
                Toast.makeText(mContext, getString(R.string.disconnect_error), Toast.LENGTH_SHORT).show();
            case DISCONNECT:
                getActivity().finish();
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
                if (!isSelected) {
                    EventBusMessenger.sendMessage(FilesFragment.this.listType, EventBusMessenger.Event.SELECT);
                    return;
                }
                if(!canDo) return;
                FFile file = files.get(getPosition());
                if(file.isBackButton()) {
                    backDir();
                    readList(CustomApplication.getInstance().getPath(listType));
                } else {
                    if(file.isDir()) {
                        addDir(file.getName());
                        Log.d("TEST", "Path: " + CustomApplication.getInstance().getPath(listType));
                        readList(CustomApplication.getInstance().getPath(listType));
                    } else if(file.isFile()) {
                        onFileClick(file);
                    }
                }
            }
        }
    }

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        mFileRecyclerView.setBackgroundColor(getResources().getColor(this.isSelected ? R.color.recyclerview_selected : R.color.recyclerview_not_selected));
    }

    private void displayList() {
        mFilesList.clear();
        mFilesList.addAll(getFiles());
        mFileAdapter.notifyDataSetChanged();
        mPatchTextView.setText(CustomApplication.getInstance().getPath(listType));
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        displayList();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void backDir() {
        String path = CustomApplication.getInstance().getPath(listType);
        if(path.isEmpty() || path.equals(File.separator)) return;
        String[] dirs = path.split(File.separator);
        path = File.separator;
        for(String d : dirs) {
            if(d.isEmpty()) continue;
            if(dirs[dirs.length - 1].equals(d)) continue;
            path += d + File.separator;
        }
        CustomApplication.getInstance().setPath(listType, path);
    }

    private void addDir(String dir) {
        if(dir.isEmpty()) return;
        String path = CustomApplication.getInstance().getPath(listType);
        String[] dirs = path.split(File.separator);
        if(0 != dirs.length && dirs[dirs.length - 1].equals(dir)) return;
        path += dir + File.separator;
        CustomApplication.getInstance().setPath(listType, path);
    }

}
