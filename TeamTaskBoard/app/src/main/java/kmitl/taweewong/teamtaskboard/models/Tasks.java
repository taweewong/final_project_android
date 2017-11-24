package kmitl.taweewong.teamtaskboard.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Tasks implements Parcelable {
    private ArrayList<Task> todoTasks;
    private ArrayList<Task> doingTasks;
    private ArrayList<Task> doneTasks;

    public static String TASKS_CLASS_KEY = "tasks";

    public enum TaskType {
        todoTasks, doingTasks, doneTasks
    }

    public Tasks() {
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

    public void setTodoTasks(ArrayList<Task> todoTasks) {
        this.todoTasks = todoTasks;
    }

    public void setDoingTasks(ArrayList<Task> doingTasks) {
        this.doingTasks = doingTasks;
    }

    public void setDoneTasks(ArrayList<Task> doneTasks) {
        this.doneTasks = doneTasks;
    }

    public ArrayList<Task> getTodoTasks() {
        if (todoTasks == null) {
            return new ArrayList<>();
        }

        return todoTasks;
    }

    public ArrayList<Task> getDoingTasks() {
        if (doingTasks == null) {
            return new ArrayList<>();
        }

        return doingTasks;
    }

    public ArrayList<Task> getDoneTasks() {
        if (doneTasks == null) {
            return new ArrayList<>();
        }

        return doneTasks;
    }
}
