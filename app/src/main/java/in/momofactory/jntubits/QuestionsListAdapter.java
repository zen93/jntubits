package in.momofactory.jntubits;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by Akshay on 23-Sep-15.
 */
public class QuestionsListAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    public static final String ARG_PAGE = "ARG_PAGE";
    private String tabTitles[];
    private Context context;
    private Bundle args;

    public QuestionsListAdapter(FragmentManager fm, Context context, Bundle savedInstanceState) {
        super(fm);
        this.context = context;
        this.args = savedInstanceState;
        tabTitles = new String[2];
        tabTitles[0] = context.getString(R.string.MCQ);
        tabTitles[1] = context.getString(R.string.FB);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MCQQuestionsListFragment.newInstance(args);
            case 1:
                return FBQuestionsListFragment.newInstance(args);
            default:
                return MCQQuestionsListFragment.newInstance(args);
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
