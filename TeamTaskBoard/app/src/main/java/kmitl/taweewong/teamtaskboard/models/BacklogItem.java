package kmitl.taweewong.teamtaskboard.models;

public class BacklogItem {
    private String title;
    private Tasks tasks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
}
