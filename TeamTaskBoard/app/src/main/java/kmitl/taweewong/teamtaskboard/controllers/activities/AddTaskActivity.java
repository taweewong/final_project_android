package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.Task;
import kmitl.taweewong.teamtaskboard.models.Tasks.TaskType;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.ITEM_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.PROJECT_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_LIST_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_TYPE_KEY;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.taskEditText) EditText taskNameEditText;
    @BindView(R.id.taskDescriptionEditText) EditText taskDescriptionEditText;

    private TaskType taskType;
    private ArrayList<Task> taskList;
    private String projectId;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskType = TaskType.valueOf(getIntent().getStringExtra(TASK_TYPE_KEY));
        taskList = getIntent().getParcelableArrayListExtra(TASK_LIST_KEY);
        projectId = getIntent().getStringExtra(PROJECT_ID_KEY);
        itemId = getIntent().getStringExtra(ITEM_ID_KEY);

        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @OnClick(R.id.addTaskButton)
    public void addTask() {
        String taskTitle = taskNameEditText.getText().toString();
        String taskDescription = taskDescriptionEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        Task newTask = new Task();
        newTask.setTitle(taskTitle);
        newTask.setDescription(taskDescription);
        newTask.setId(databaseService.generateIdKey());

        taskList.add(newTask);
        databaseService.addTask(taskList, projectId, itemId, taskType);
        finishAddTaskActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    private void finishAddTaskActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
