package kmitl.taweewong.teamtaskboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.viewholders.BacklogItemViewHolder;

public class BacklogItemAdapter extends RecyclerView.Adapter<BacklogItemViewHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    public interface OnClickBacklogItemListener {
        void onClickBacklogItem(int position);
        void onLongClickBacklogItem(int position);
    }

    private List<BacklogItem> backlogItems;
    private RecyclerView parentRecyclerView;
    private OnClickBacklogItemListener listener;

    public BacklogItemAdapter(List<BacklogItem> backlogItems, OnClickBacklogItemListener listener) {
        this.backlogItems = backlogItems;
        this.listener = listener;
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
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

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

    @Override
    public void onClick(View view) {
        int itemPosition = parentRecyclerView.getChildAdapterPosition(view);
        listener.onClickBacklogItem(itemPosition);
    }

    @Override
    public boolean onLongClick(View view) {
        int itemPosition = parentRecyclerView.getChildAdapterPosition(view);
        listener.onLongClickBacklogItem(itemPosition);
        return true;
    }
}
