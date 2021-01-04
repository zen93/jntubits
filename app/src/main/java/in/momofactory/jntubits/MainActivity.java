package in.momofactory.jntubits;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        OnItemSelectedListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Spinner midSpinner;
    protected static AppSettings appSettings;
    protected static String semId, batchId, branchId, midId;
    protected static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appSettings = new AppSettings(this);
        Log.i("jntubits", "on Create... App Settings initialized");


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        loadId();

    }

    protected void loadId() {
        String year, semester, branch, mid;
        year = appSettings.getYear();
        semester = appSettings.getSem();
        branch = appSettings.getBranch();
        mid = appSettings.getMid();

        try {
            dbHelper = new DBHelper(getApplicationContext());
            semId = dbHelper.getSemId(year, semester);
            branchId = dbHelper.getBranchId(branch);

            batchId = dbHelper.getBatchId(semId, branchId);
            midId = dbHelper.getMidId(semId, mid);
            //Toast.makeText(this, "midId: " + midId + " mid: " + mid, Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Log.e("jnutbits", "Can't init db handle");
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        if(appSettings == null)
            appSettings = new AppSettings(getApplicationContext());
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                appSettings.setMode(AppSettings.LEARNING);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                appSettings.setMode(AppSettings.REVISION);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            //midSpinner = (Spinner) findViewById(R.id.action_bar_spinner);

            MenuItem item = menu.findItem(R.id.mid_spinner);
            midSpinner = (Spinner) MenuItemCompat.getActionView(item);
            SpinnerAdapter mSpinnerAdapter;
            if(appSettings.getYear().contentEquals("1")) {
                mSpinnerAdapter = ArrayAdapter.createFromResource(this.getSupportActionBar().getThemedContext(),
                        R.array.mid_selection_first_year, android.R.layout.simple_spinner_dropdown_item); //  create the adapter from a StringArray
            }
            else {
                mSpinnerAdapter = ArrayAdapter.createFromResource(this.getSupportActionBar().getThemedContext(),
                        R.array.mid_selection, android.R.layout.simple_spinner_dropdown_item); //  create the adapter from a StringArray
            }
            midSpinner.setAdapter(mSpinnerAdapter); // set the adapter
            midSpinner.setSelection(Integer.parseInt(appSettings.getMid())-1);
            midSpinner.setOnItemSelectedListener(this);
            /*midSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            appSettings.setMid(midSpinner.getSelectedItem().toString());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //nothing
                        }
                }
            ); */
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent i = new Intent(this, HelpActivity.class);
            startActivity(i);
            return true;
        }

        if(id == R.id.action_about){
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.action_score) {

            Intent i = new Intent(this, ScoreActivity.class);
            i.putExtra("batchId", batchId);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private String[] subjects_list;
        private String[] subjects_id;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            int layoutId = R.layout.fragment_main;
            switch (sectionNumber) {
                case 1:
                    layoutId = R.layout.fragment_subjects;
                    break;
                case 2:
                    layoutId = R.layout.fragment_subjects;
                    break;
                case 3:
                    layoutId = R.layout.fragment_settings;
                    break;
            }
            View rootView = inflater.inflate(layoutId, container,
                    false);

            if(sectionNumber == 1 || sectionNumber == 2) {
                List<String> subjects = new ArrayList<String>();
                List<String> subjectids = new ArrayList<String>();

                subjects = dbHelper.getSubjects(batchId);
                subjectids = dbHelper.getSubIds(batchId);

                subjects_list = new String[subjects.size()];
                subjects.toArray(subjects_list);

                subjects_id = new String[subjectids.size()];
                subjectids.toArray(subjects_id);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subjects_list);
                ListView subList = (ListView) rootView.findViewById(R.id.subjectsListView);

                subList.setAdapter(adapter);
                subList.setOnItemClickListener(selectSubject);
            }
            if(sectionNumber == 3) {
                String[] settings_list = {"Change Batch"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, settings_list);
                ListView settingsList = (ListView) rootView.findViewById(R.id.settingsListView);

                settingsList.setAdapter(adapter);
                settingsList.setOnItemClickListener(selectBatch);
            }
            return rootView;
        }

        public AdapterView.OnItemClickListener selectSubject = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent i = new Intent(getActivity(), QuestionsListActivity.class);
                i.putExtra("subjectName", subjects_list[position]);
                i.putExtra("subjectId", subjects_id[position]);
                i.putExtra("midId", midId);
                startActivity(i);
            }
        };

        public AdapterView.OnItemClickListener selectBatch = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent i = new Intent(getActivity(),SelectOptions.class);
                getActivity().finish();
                startActivity(i);
            }
        };



        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            ((MainActivity) context).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
        // TODO Auto-generated method stub
        String mid = (String) parent.getItemAtPosition(pos);
        appSettings.setMid(mid);
        loadId();
        Toast.makeText(MainActivity.this, mid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

}
