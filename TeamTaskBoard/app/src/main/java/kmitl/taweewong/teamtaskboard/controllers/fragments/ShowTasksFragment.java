package kmitl.taweewong.teamtaskboard.controllers.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import kmitl.taweewong.teamtaskboard.controllers.activities.AddTaskActivity;
import kmitl.taweewong.teamtaskboard.controllers.activities.EditTaskActivity;
import kmitl.taweewong.teamtaskboard.models.Task;
import kmitl.taweewong.teamtaskboard.models.Tasks;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.ITEM_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.POSITION_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.PROJECT_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_LIST_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_TYPE_KEY;
import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType;

public class ShowTasksFragment extends Fragment implements TaskItemAdapter.OnClickTaskListener,
        DatabaseService.OnQueryTasksCompleteListener {

    @BindView(R.id.showTasksRecyclerView) RecyclerView recyclerView;

    private ArrayList<Task> tasks;
    private String projectId;
    private String itemId;
    private TaskType taskType;
    TaskItemAdapter taskItemAdapter;

    public static final int ADD_TASK_REQUEST_CODE = 255;
    public static final int ADD_TASK_RESPONSE_CODE = 256;
    public static final int EDIT_TASK_REQUEST_CODE = 355;
    public static final int EDIT_TASK_RESPONSE_CODE = 356;
    public static final int DELETE_TASK_RESPONSE_CODE = 357;

    public ShowTasksFragment() {
        // Required empty public constructor
    }

    public static ShowTasksFragment newInstance(String projectId, String itemId, TaskType type) {
        Bundle args = new Bundle();
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
        setHasOptionsMenu(true);
        tasks = new ArrayList<>();
        projectId = getArguments().getString(PROJECT_ID_KEY);
        itemId = getArguments().getString(ITEM_ID_KEY);
        taskType = TaskType.valueOf(getArguments().getString(TASK_TYPE_KEY));

        DatabaseService databaseService = new DatabaseService();
        databaseService.updateTasks(projectId, itemId, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTaskMenu:
                startAddTaskActivity();
        }

        return super.onOptionsItemSelected(item);
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

        taskItemAdapter = new TaskItemAdapter(tasks,
                this,
                itemTouchHelper,
                taskType);

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
                int swipeFlags = 0;
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
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            private boolean isDropItem() {
                return sourcePosition != targetPosition;
            }
        };
    }

    @Override
    public void onClickTask(int position, TaskType type) {

    }

    @Override
    public void onLongClickTask(int position) {
        startEditTaskActivity(tasks.get(position), position);
    }

    private void startAddTaskActivity() {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra(TASK_TYPE_KEY, taskType.name());
        intent.putExtra(TASK_LIST_KEY, tasks);
        intent.putExtra(PROJECT_ID_KEY, projectId);
        intent.putExtra(ITEM_ID_KEY, itemId);

        startActivity(intent);
    }

    private void startEditTaskActivity(Task task, int position) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra(TASK_TYPE_KEY, taskType.name());
        intent.putExtra(TASK_KEY, task);
        intent.putExtra(PROJECT_ID_KEY, projectId);
        intent.putExtra(ITEM_ID_KEY, itemId);
        intent.putExtra(POSITION_KEY, position);
        intent.putExtra(TASK_LIST_KEY, tasks);

        startActivity(intent);
    }

    @Override
    public void onQueryTasksItemsSuccess(Tasks tasks) {
        this.tasks.clear();

        switch (taskType) {
            case todoTasks:
                this.tasks.addAll(tasks.getTodoTasks());
                break;
            case doingTasks:
                this.tasks.addAll(tasks.getDoingTasks());
                break;
            case doneTasks:
                this.tasks.addAll(tasks.getDoneTasks());
        }

        taskItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryTasksItemsFailed() {

    }
}
