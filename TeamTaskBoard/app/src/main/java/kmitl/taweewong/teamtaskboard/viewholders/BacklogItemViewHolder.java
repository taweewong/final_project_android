package kmitl.taweewong.teamtaskboard.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.views.TouchableImageButton;

public class BacklogItemViewHolder extends RecyclerView.ViewHolder {
    private TextView backlogItemNameText;
    private TouchableImageButton dragBacklogItemView;

    public BacklogItemViewHolder(View itemView) {
        super(itemView);
        backlogItemNameText = itemView.findViewById(R.id.backlogItemNameText);
        dragBacklogItemView = itemView.findViewById(R.id.dragBacklogItemView);
    }

    public TextView getBacklogItemNameText() {
        return backlogItemNameText;
    }

    public TouchableImageButton getDragBacklogItemView() {
        return dragBacklogItemView;
    }
}
