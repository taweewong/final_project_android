package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.services.DatabaseService;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.taskEditText) EditText taskNameEditText;
    @BindView(R.id.taskDescriptionEditText) EditText taskDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addTaskButton)
    public void addTask() {
        String taskTitle = taskNameEditText.getText().toString();
        String taskDescription = taskDescriptionEditText.getText().toString();
        DatabaseService databaseService = new DatabaseService();
    }
}
