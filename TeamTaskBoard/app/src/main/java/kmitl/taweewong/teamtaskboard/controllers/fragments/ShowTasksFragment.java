package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.adapters.TaskItemAdapter;
import kmitl.taweewong.teamtaskboard.adapters.TaskItemAdapter.OnClickTaskListener;
import kmitl.taweewong.teamtaskboard.models.Task;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType;

public class ShowTasksFragment extends Fragment {

    @BindView(R.id.showTasksRecyclerView) RecyclerView recyclerView;

    private List<Task> tasks;
    private String projectId;
    private String itemId;
    private TaskType taskType;

    private static final String TASK_LIST_KEY = "taskList";
    private static final String PROJECT_ID_KEY = "projectId";
    private static final String ITEM_ID_KEY = "itemId";
    private static final String TASK_TYPE_KEY = "taskType";

    public ShowTasksFragment() {
        // Required empty public constructor
    }

    public static ShowTasksFragment newInstance(ArrayList<Task> tasks, String projectId, String itemId, TaskType type) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(TASK_LIST_KEY, tasks);
        args.putString(PROJECT_ID_KEY, projectId);
        args.putString(ITEM_ID_KEY, itemId);
        args.putString(TASK_TYPE_KEY, type.name());

        ShowTasksFragment fragment = new ShowTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasks = getArguments().getParcelableArrayList(TASK_LIST_KEY);
        projectId = getArguments().getString(PROJECT_ID_KEY);
        itemId = getArguments().getString(ITEM_ID_KEY);
        taskType = TaskType.valueOf(getArguments().getString(TASK_TYPE_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_tasks, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ItemTouchHelper.Callback callback = createItemTouchHelperCallback(tasks);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(tasks,
                (OnClickTaskListener) getContext(),
                itemTouchHelper);

        RecyclerView recyclerView = view.findViewById(R.id.showTasksRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskItemAdapter);

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback createItemTouchHelperCallback(final List<Task> tasks) {
        return new ItemTouchHelper.Callback() {
            int sourcePosition;
            int targetPosition;
            DatabaseService databaseService = new DatabaseService();

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                sourcePosition = viewHolder.getAdapterPosition();
                targetPosition = target.getAdapterPosition();
                Collections.swap(tasks, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (isDropItem()) {
                    databaseService.updateTaskItems(tasks, projectId, itemId, taskType);
                    Toast.makeText(getContext(), "moved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(getContext(), "on swipe", Toast.LENGTH_SHORT).show();
            }

            private boolean isDropItem() {
                return sourcePosition != targetPosition;
            }
        };
    }
}
