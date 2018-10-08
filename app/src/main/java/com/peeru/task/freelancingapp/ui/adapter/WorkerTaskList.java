package com.peeru.task.freelancingapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.databinding.AdapterWorkerTaskListBinding;
import com.peeru.task.freelancingapp.ui.activity.PartnerScreen;

import java.util.List;

/**
 * Created by Priyank Jain on 08-10-2018.
 */
public class WorkerTaskList extends RecyclerView.Adapter<WorkerTaskList.MessageViewHolder> {
    //region Variable Declaration
    private List<? extends Task> taskList;
    private Context mContext;
    private Activity activity;
    public WorkerTaskList(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }
    //endregion

    //region Set Message List Data
    public void setTaskList(final List<? extends Task> mEventList){
        this.taskList = mEventList;
        notifyDataSetChanged();
    }
    //endregion

    //region Create View Holder
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterWorkerTaskListBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.adapter_worker_task_list,
                        parent,
                        false);

        return new MessageViewHolder(binding);
    }
    //endregion

    //region Bind View Holder
    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.binding.setTask(task);
        String url = "http://maps.google.com/maps/api/staticmap?center=" +
                task.getLat() +
                "," +
                task.getLon() +
                "&zoom=20&size=900x900&sensor=false" +
                "&markers=color:red|" +
                task.getLat() +
                "," +
                task.getLon() +
                "&key="+mContext.getResources().getString(R.string.google_maps_key);

        Glide.with(mContext)
                .load(url)
                .into(holder.binding.mapView);
        holder.binding.taskDistance.setText(task.getDistance().toString()+" kms");
        if(!task.equals("Rejected") || !task.equals("Rated")) {
            holder.binding.taskLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PartnerScreen) activity).updateTask(task);
                }
            });
        }
        holder.binding.executePendingBindings();
    }
    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }
    //endregion

    //region View Holder Class
    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final AdapterWorkerTaskListBinding binding;

        public MessageViewHolder(AdapterWorkerTaskListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    //endregion
}
