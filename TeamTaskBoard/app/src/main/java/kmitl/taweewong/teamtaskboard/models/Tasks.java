package kmitl.taweewong.teamtaskboard.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Tasks implements Parcelable {
    private List<Task> todoTasks;
    private List<Task> doingTasks;
    private List<Task> doneTasks;

    public static String TASKS_CLASS_KEY = "tasks";

    Tasks() {
    }

    private Tasks(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tasks> CREATOR = new Creator<Tasks>() {
        @Override
        public Tasks createFromParcel(Parcel in) {
            return new Tasks(in);
        }

        @Override
        public Tasks[] newArray(int size) {
            return new Tasks[size];
        }
    };

    public void setTodoTasks(List<Task> todoTasks) {
        this.todoTasks = todoTasks;
    }

    public void setDoingTasks(List<Task> doingTasks) {
        this.doingTasks = doingTasks;
    }

    public void setDoneTasks(List<Task> doneTasks) {
        this.doneTasks = doneTasks;
    }

    public List<Task> getTodoTasks() {
        if (todoTasks == null) {
            return new ArrayList<>();
        }

        return todoTasks;
    }

    public List<Task> getDoingTasks() {
        if (doingTasks == null) {
            return new ArrayList<>();
        }

        return doingTasks;
    }

    public List<Task> getDoneTasks() {
        if (doneTasks == null) {
            return new ArrayList<>();
        }

        return doneTasks;
    }
}
