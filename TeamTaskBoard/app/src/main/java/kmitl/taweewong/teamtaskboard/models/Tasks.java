package kmitl.taweewong.teamtaskboard.models;

import java.util.List;

public class Tasks {
    private List<Task> todoTasks;
    private List<Task> doingTasks;
    private List<Task> doneTasks;

    public List<Task> getTodoTasks() {
        return todoTasks;
    }

    public void setTodoTasks(List<Task> todoTasks) {
        this.todoTasks = todoTasks;
    }

    public List<Task> getDoingTasks() {
        return doingTasks;
    }

    public void setDoingTasks(List<Task> doingTasks) {
        this.doingTasks = doingTasks;
    }

    public List<Task> getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(List<Task> doneTasks) {
        this.doneTasks = doneTasks;
    }
}
