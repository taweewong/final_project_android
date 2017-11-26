package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.Task;
import kmitl.taweewong.teamtaskboard.models.Tasks.TaskType;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.EDITED_TASK_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.ITEM_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.POSITION_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.PROJECT_ID_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.activities.TaskActivity.TASK_TYPE_KEY;
import static kmitl.taweewong.teamtaskboard.controllers.fragments.ShowTasksFragment.EDIT_TASK_REQUEST_CODE;

public class EditTaskActivity extends AppCompatActivity {

    @BindView(R.id.editTaskEditText) EditText editTaskEditText;
    @BindView(R.id.editTaskDescriptionEditText) EditText editTaskDescriptionEditText;

    private Task task;
    private String projectId;
    private String itemId;
    private TaskType taskType;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        ButterKnife.bind(this);

        task = getIntent().getParcelableExtra(TASK_KEY);
        projectId = getIntent().getStringExtra(PROJECT_ID_KEY);
        itemId = getIntent().getStringExtra(ITEM_ID_KEY);
        taskType = TaskType.valueOf(getIntent().getStringExtra(TASK_TYPE_KEY));
        position = getIntent().getIntExtra(POSITION_KEY, -1);

        editTaskEditText.setText(task.getTitle());
        editTaskDescriptionEditText.setText(task.getDescription());
    }

    @OnClick(R.id.editTaskButton)
    public void editTask() {
        String editedTaskName = editTaskEditText.getText().toString();
        String editedTaskDescription = editTaskDescriptionEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();

        task.setTitle(editedTaskName);
        task.setDescription(editedTaskDescription);

        databaseService.editTask(task, projectId, itemId, taskType);

        sendDataBackToCaller(task);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    private void sendDataBackToCaller(Task editedTask) {
        Intent result = new Intent();
        result.putExtra(EDITED_TASK_KEY, editedTask);
        result.putExtra(POSITION_KEY, position);
        setResult(EDIT_TASK_REQUEST_CODE, result);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}