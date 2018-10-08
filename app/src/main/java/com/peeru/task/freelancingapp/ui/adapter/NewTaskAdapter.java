package com.peeru.task.freelancingapp.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.model.Task;
import com.peeru.task.freelancingapp.databinding.AdapterTaskBinding;
import com.peeru.task.freelancingapp.ui.fragment.UserFragment;
import com.peeru.task.freelancingapp.util.Utility;

import java.util.List;

/**
 * Created by Priyank Jain on 08-10-2018.
 */
public class NewTaskAdapter extends RecyclerView.Adapter<NewTaskAdapter.MessageViewHolder> {
    //region Vairable Declaration
    private List<Task> taskList;
    private String parentEmail;
    private UserFragment userFragment;
    public NewTaskAdapter(UserFragment userFragment) {
        this.userFragment = userFragment;
    }
    //endregion

    //region Set User List Data
    public void setTaskList(final List<Task> taskList, String parentEmail){
        this.taskList = taskList;
        this.parentEmail = parentEmail;
        notifyDataSetChanged();
    }
    //endregion

    //region Create View Holder
    @Override
    public NewTaskAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterTaskBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.adapter_task,
                        parent,
                        false);

        return new MessageViewHolder(binding);
    }
    //endregion

    //region Bind View Holder
    @Override
    public void onBindViewHolder(NewTaskAdapter.MessageViewHolder holder, int position) {
        holder.binding.setTask(taskList.get(position));
        holder.binding.childrenName.setText(taskList.get(position).getTaskTitle());
        holder.binding.taskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFragment.deleteRecord(taskList.get(position));
            }
        });
        if(taskList.get(position).getTaskStatus().equals("Completed") || taskList.get(position).getTaskStatus().equals("Rated")){
            holder.binding.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userFragment.updateRating(taskList.get(position));
                }
            });
        }
        holder.binding.lastUpdate.setText(Utility.dateTimeCoversion(taskList.get(position).getModifyDate())+"");
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }
    //endregion

    //region Message View Holder Class
    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final AdapterTaskBinding binding;

        public MessageViewHolder(AdapterTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    //endregion
}
