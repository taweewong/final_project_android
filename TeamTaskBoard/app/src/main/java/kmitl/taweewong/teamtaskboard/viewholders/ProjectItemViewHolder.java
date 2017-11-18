package kmitl.taweewong.teamtaskboard.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kmitl.taweewong.teamtaskboard.R;

public class ProjectItemViewHolder extends RecyclerView.ViewHolder {
    private TextView projectNameText;

    public ProjectItemViewHolder(View itemView) {
        super(itemView);
        projectNameText = itemView.findViewById(R.id.projectNameText);
    }

    public TextView getProjectNameText() {
        return projectNameText;
    }

    public void setProjectNameText(TextView projectNameText) {
        this.projectNameText = projectNameText;
    }
}
