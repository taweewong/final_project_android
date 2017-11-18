package kmitl.taweewong.teamtaskboard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.Project;
import kmitl.taweewong.teamtaskboard.viewholders.ProjectItemViewHolder;

public class ProjectItemAdapter extends RecyclerView.Adapter<ProjectItemViewHolder> implements View.OnClickListener {
    private List<Project> projects;
    private RecyclerView parentRecyclerView;

    public ProjectItemAdapter(List<Project> projects) {
        this.projects = projects;
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
    }
}
