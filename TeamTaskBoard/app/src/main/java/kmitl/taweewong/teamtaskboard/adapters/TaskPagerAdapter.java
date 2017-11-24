package kmitl.taweewong.teamtaskboard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kmitl.taweewong.teamtaskboard.controllers.fragments.ShowTasksFragment;

public class TaskPagerAdapter extends FragmentPagerAdapter{

    public TaskPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ShowTasksFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
