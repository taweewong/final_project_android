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
import kmitl.taweewong.teamtaskboard.models.Task;
import kmitl.taweewong.teamtaskboard.viewholders.TaskViewHolder;
import kmitl.taweewong.teamtaskboard.views.TouchableImageButton;

import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType;

public class TaskItemAdapter extends RecyclerView.Adapter<TaskViewHolder> implements
        View.OnClickListener, View.OnLongClickListener{

    public interface OnClickTaskListener {
        void onClickTask(int position, TaskType type);
        void onLongClickTask(int position);
    }

    private List<Task> tasks;
    private RecyclerView parentRecyclerView;
    private OnClickTaskListener listener;
    private ItemTouchHelper itemTouchHelper;
    private TaskType taskType;

    public TaskItemAdapter(List<Task> tasks,
                           OnClickTaskListener listener,
                           ItemTouchHelper itemTouchHelper,
                           TaskType taskType) {
        this.tasks = tasks;
        this.listener = listener;
        this.itemTouchHelper = itemTouchHelper;
        this.taskType = taskType;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.parentRecyclerView = recyclerView;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.task_item_view, parent, false);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        TextView taskItemNameText = holder.getTaskItemNameText();
        TextView taskItemDescriptionText = holder.getTaskItemDescriptionText();
        TouchableImageButton dragBacklogItemView = holder.getDragTaskItemView();

        taskItemNameText.setText(tasks.get(position).getTitle());

        String description = tasks.get(position).getDescription();
        if (description == null) {
            taskItemDescriptionText.setVisibility(View.GONE);
        } else {
            taskItemDescriptionText.setVisibility(View.VISIBLE);
            taskItemDescriptionText.setText(tasks.get(position).getDescription());
        }

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
        return tasks.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = parentRecyclerView.getChildAdapterPosition(view);
        listener.onClickTask(itemPosition, taskType);
    }

    @Override
    public boolean onLongClick(View view) {
        int itemPosition = parentRecyclerView.getChildAdapterPosition(view);
        listener.onLongClickTask(itemPosition);
        return true;
    }
}
