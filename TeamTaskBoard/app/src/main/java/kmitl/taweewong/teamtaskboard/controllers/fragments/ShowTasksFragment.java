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
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.EDITED_TASK_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.ITEM_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.POSITION_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.PROJECT_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_LIST_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_TYPE_KEY;
import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType;

public class ShowTasksFragment extends Fragment implements TaskItemAdapter.OnClickTaskListener {

    @BindView(R.id.showTasksRecyclerView) RecyclerView recyclerView;

    private ArrayList<Task> tasks;
    private String projectId;
    private String itemId;
    private TaskType taskType;
    TaskItemAdapter taskItemAdapter;

    public static final int ADD_TASK_REQUEST_CODE = 255;
    public static final int EDIT_TASK_REQUEST_CODE = 355;

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
        setHasOptionsMenu(true);
        tasks = getArguments().getParcelableArrayList(TASK_LIST_KEY);
        projectId = getArguments().getString(PROJECT_ID_KEY);
        itemId = getArguments().getString(ITEM_ID_KEY);
        taskType = TaskType.valueOf(getArguments().getString(TASK_TYPE_KEY));
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

    @Override
    public void onClickTask(int position, TaskType type) {

    }

    @Override
    public void onLongClickTask(int position) {
        startEditTaskActivity(tasks.get(position), position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ADD_TASK_REQUEST_CODE:
                notifyAddTask(data);
                break;
            case EDIT_TASK_REQUEST_CODE:
                notifyEditTask(data);
        }
    }

    private void startAddTaskActivity() {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra(TASK_TYPE_KEY, taskType.name());
        intent.putExtra(TASK_LIST_KEY, tasks);
        intent.putExtra(PROJECT_ID_KEY, projectId);
        intent.putExtra(ITEM_ID_KEY, itemId);

        startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
    }

    private void startEditTaskActivity(Task task, int position) {
        Intent intent = new Intent(getContext(), EditTaskActivity.class);
        intent.putExtra(TASK_TYPE_KEY, taskType.name());
        intent.putExtra(TASK_KEY, task);
        intent.putExtra(PROJECT_ID_KEY, projectId);
        intent.putExtra(ITEM_ID_KEY, itemId);
        intent.putExtra(POSITION_KEY, position);

        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
    }

    private void notifyAddTask(Intent data) {
        Task newTask = data.getParcelableExtra(TASK_KEY);
        tasks.add(newTask);
        taskItemAdapter.notifyDataSetChanged();
    }

    private void notifyEditTask(Intent data) {
        Task editedTask = data.getParcelableExtra(EDITED_TASK_KEY);
        int position = data.getIntExtra(POSITION_KEY, -1);

        if (position != -1) {
            tasks.set(position, editedTask);
            taskItemAdapter.notifyDataSetChanged();
        }
    }
}
