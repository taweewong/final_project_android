package kmitl.taweewong.teamtaskboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.controllers.activities.BacklogItemActivity;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.viewholders.ProjectItemViewHolder;

public class ProjectItemAdapter extends RecyclerView.Adapter<ProjectItemViewHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    public interface OnClickProjectListener {
        void onClickProject(int position);
        void onLongClickProject(int position);
    }

    private List<Project> projects;
    private RecyclerView parentRecyclerView;
    private OnClickProjectListener listener;

    public ProjectItemAdapter(List<Project> projects, OnClickProjectListener listener) {
        this.projects = projects;
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.parentRecyclerView = recyclerView;
    }

    @Override
    public ProjectItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.project_item_view, null, false);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        return new ProjectItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectItemViewHolder holder, int position) {
        TextView projectNameText = holder.getProjectNameText();

        projectNameText.setText(projects.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = parentRecyclerView.getChildAdapterPosition(view);
        listener.onClickProject(itemPosition);
    }

    @Override
    public boolean onLongClick(View view) {
        int itemPosition = parentRecyclerView.getChildAdapterPosition(view);
        listener.onLongClickProject(itemPosition);
        return true;
    }
}
