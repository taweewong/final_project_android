package kmitl.taweewong.teamtaskboard.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kmitl.taweewong.teamtaskboard.R;

public class BacklogItemViewHolder extends RecyclerView.ViewHolder {
    private TextView backlogItemNameText;

    public BacklogItemViewHolder(View itemView) {
        super(itemView);
        backlogItemNameText = itemView.findViewById(R.id.backlogItemNameText);
    }

    public TextView getBacklogItemNameText() {
        return backlogItemNameText;
    }

    public void setBacklogItemNameText(TextView backlogItemNameText) {
        this.backlogItemNameText = backlogItemNameText;
    }
}
