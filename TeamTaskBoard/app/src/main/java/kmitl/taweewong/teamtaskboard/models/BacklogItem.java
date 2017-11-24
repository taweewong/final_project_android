package kmitl.taweewong.teamtaskboard.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BacklogItem implements Parcelable {
    private String id;
    private String title;
    private Tasks tasks;

    public static String BACKLOG_ITEM_CLASS_KEY = "backlogItem";

    public BacklogItem() {

    }

    private BacklogItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        tasks = in.readParcelable(Tasks.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeParcelable(tasks, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    static final Creator<BacklogItem> CREATOR = new Creator<BacklogItem>() {
        @Override
        public BacklogItem createFromParcel(Parcel in) {
            return new BacklogItem(in);
        }

        @Override
        public BacklogItem[] newArray(int size) {
            return new BacklogItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Tasks getTasks() {
        if (tasks == null) {
            return new Tasks();
        }

        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
}
