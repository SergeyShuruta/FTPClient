package com.shuruta.sergey.ftpclient.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuruta.sergey.ftpclient.R;
import com.shuruta.sergey.ftpclient.interfaces.FFile;
import com.shuruta.sergey.ftpclient.interfaces.OnFileClickListener;
import com.shuruta.sergey.ftpclient.interfaces.OnFileMenuClickListener;

import java.util.List;

/**
 * Created by Sergey Shuruta
 * 30.12.2015 at 15:52
 */
public class RecyclerFileAdapter extends RecyclerView.Adapter<RecyclerFileAdapter.ViewHolder> {

    private List<FFile> files;
    private OnFileClickListener onFileClickListener;
    private OnFileMenuClickListener onMenuClickListener;
    private Context context;
    private boolean isActiveState = true;

    public RecyclerFileAdapter(Context context, List<FFile> files,
                               OnFileClickListener onFileClickListener,
                               OnFileMenuClickListener onMenuClickListener) {
        this.files = files;
        this.onFileClickListener = onFileClickListener;
        this.onMenuClickListener = onMenuClickListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(inflater.inflate(R.layout.row_file, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final FFile file = files.get(i);
        boolean isBackBtn = file.getName().equals(".");
        if (isBackBtn) {
            viewHolder.nameTextView.setText(R.string.back);
            viewHolder.sizTextView.setVisibility(View.GONE);
            viewHolder.menuImageView.setVisibility(View.GONE);
            viewHolder.iconImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_back));
        } else {
            viewHolder.menuImageView.setVisibility(View.VISIBLE);
            viewHolder.sizTextView.setVisibility(file.isDir() ? View.GONE : View.VISIBLE);
            viewHolder.nameTextView.setText(file.getName());
            viewHolder.sizTextView.setText(file.getFormatSize());
            viewHolder.iconImageView.setImageDrawable(file.getIcon());
            viewHolder.setFile(file);
        }
        if (isActiveState) {
            viewHolder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
            viewHolder.sizTextView.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
        } else {
            viewHolder.nameTextView.setTextColor(ContextCompat.getColor(context, R.color.primary_text_disable));
            viewHolder.sizTextView.setTextColor(ContextCompat.getColor(context, R.color.secondary_text_disable));
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView, sizTextView;
        private ImageView iconImageView, menuImageView;

        public ViewHolder(View holderView) {
            super(holderView);
            this.nameTextView = (TextView) holderView.findViewById(R.id.nameTextView);
            this.sizTextView = (TextView) holderView.findViewById(R.id.sizeTextView);
            this.iconImageView = (ImageView) holderView.findViewById(R.id.iconImageView);
            this.menuImageView = (ImageView) holderView.findViewById(R.id.menuImageView);
            this.menuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMenuClickListener.onMenuClick(v, (FFile) v.getTag());
                }
            });
            holderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFileClickListener.onFileClick(v, files.get(getPosition()));
                }
            });
        }

        public void setFile(FFile file) {
            this.menuImageView.setTag(file);
        }
    }

    public boolean isActiveState() {
        return isActiveState;
    }

    public void setIsActive(boolean isActiveState) {
        this.isActiveState = isActiveState;
        notifyDataSetChanged();
    }
}