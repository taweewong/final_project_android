package kmitl.taweewong.teamtaskboard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.viewholders.BacklogItemViewHolder;

public class BacklogItemAdapter extends RecyclerView.Adapter<BacklogItemViewHolder> {
    private List<BacklogItem> backlogItems;
    private RecyclerView parentRecyclerView;
    private Context context;

    public BacklogItemAdapter(List<BacklogItem> backlogItems, Context context) {
        this.backlogItems = backlogItems;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.parentRecyclerView = recyclerView;
    }

    @Override
    public BacklogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.backlog_item_view, null, false);

        return new BacklogItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BacklogItemViewHolder holder, int position) {
        TextView backlogItemNameText = holder.getBacklogItemNameText();

        backlogItemNameText.setText(backlogItems.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return backlogItems.size();
    }
}
