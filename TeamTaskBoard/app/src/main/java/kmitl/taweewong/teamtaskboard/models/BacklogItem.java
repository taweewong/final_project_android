package kmitl.taweewong.teamtaskboard.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BacklogItem implements Parcelable {
    private String title;
    private Tasks tasks;

    public static String BACKLOG_ITEM_CLASS_KEY = "backlogItem";

    public BacklogItem() {

    }

    private BacklogItem(Parcel in) {
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BacklogItem> CREATOR = new Creator<BacklogItem>() {
        @Override
        public BacklogItem createFromParcel(Parcel in) {
            return new BacklogItem(in);
        }

        @Override
        public BacklogItem[] newArray(int size) {
            return new BacklogItem[size];
        }
    };

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
