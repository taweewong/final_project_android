package kmitl.taweewong.teamtaskboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.BacklogItem;
import kmitl.taweewong.teamtaskboard.viewholders.BacklogItemViewHolder;
import kmitl.taweewong.teamtaskboard.views.TouchableImageButton;

public class BacklogItemAdapter extends RecyclerView.Adapter<BacklogItemViewHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    public interface OnClickBacklogItemListener {
        void onClickBacklogItem(int position);
        void onLongClickBacklogItem(int position);
    }

    private List<BacklogItem> backlogItems;
    private RecyclerView parentRecyclerView;
    private OnClickBacklogItemListener listener;
    private ItemTouchHelper itemTouchHelper;

    public BacklogItemAdapter(List<BacklogItem> backlogItems,
                              OnClickBacklogItemListener listener,
                              ItemTouchHelper itemTouchHelper) {
        this.backlogItems = backlogItems;
        this.listener = listener;
        this.itemTouchHelper = itemTouchHelper;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.parentRecyclerView = recyclerView;
    }

    @Override
    public BacklogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.backlog_item_view, parent, false);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        return new BacklogItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BacklogItemViewHolder holder, int position) {
        TextView backlogItemNameText = holder.getBacklogItemNameText();
        TouchableImageButton dragBacklogItemView = holder.getDragBacklogItemView();

        backlogItemNameText.setText(backlogItems.get(position).getTitle());

        dragBacklogItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    view.performClick();
                    itemTouchHelper.startDrag(holder);
                }
                return false;
            }
        });
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
