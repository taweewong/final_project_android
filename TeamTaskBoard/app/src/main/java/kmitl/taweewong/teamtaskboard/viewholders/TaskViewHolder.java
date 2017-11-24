package kmitl.taweewong.teamtaskboard.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.views.TouchableImageButton;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private TextView taskItemNameText;
    private TextView taskItemDescriptionText;
    private TouchableImageButton dragTaskItemView;

    public TaskViewHolder(View itemView) {
        super(itemView);
        taskItemNameText = itemView.findViewById(R.id.taskItemNameText);
        taskItemDescriptionText = itemView.findViewById(R.id.taskItemDescriptionText);
        dragTaskItemView = itemView.findViewById(R.id.dragTaskItemView);
    }

    public TextView getTaskItemNameText() {
        return taskItemNameText;
    }

    public void setTaskItemNameText(TextView taskItemNameText) {
        this.taskItemNameText = taskItemNameText;
    }

    public TextView getTaskItemDescriptionText() {
        return taskItemDescriptionText;
    }

    public void setTaskItemDescriptionText(TextView taskItemDescriptionText) {
        this.taskItemDescriptionText = taskItemDescriptionText;
    }

    public TouchableImageButton getDragTaskItemView() {
        return dragTaskItemView;
    }

    public void setDragTaskItemView(TouchableImageButton dragTaskItemView) {
        this.dragTaskItemView = dragTaskItemView;
    }
}
