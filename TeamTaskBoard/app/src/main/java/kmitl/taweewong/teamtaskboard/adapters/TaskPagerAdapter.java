package kmitl.taweewong.teamtaskboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowTasksFragment;
import kmitl.taweewong.teamtaskboard.models.Tasks;

import static kmitl.taweewong.teamtaskboard.models.Tasks.TaskType.*;

public class TaskPagerAdapter extends FragmentPagerAdapter{

    private Tasks tasks;
    private String projectId;
    private String itemId;

    public TaskPagerAdapter(FragmentManager fm, Tasks tasks, String projectId, String itemId) {
        super(fm);
        this.tasks = tasks;
        this.projectId = projectId;
        this.itemId = itemId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShowTasksFragment.newInstance(tasks.getTodoTasks(), projectId, itemId, todoTasks);
            case 1:
                return ShowTasksFragment.newInstance(tasks.getDoingTasks(), projectId, itemId, doingTasks);
            default:
                return ShowTasksFragment.newInstance(tasks.getDoneTasks(), projectId, itemId, doneTasks);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TODO";
            case 1:
                return "DOING";
            default:
                return "DONE";
        }
    }
}
