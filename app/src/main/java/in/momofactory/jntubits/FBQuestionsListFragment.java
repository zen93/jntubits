package in.momofactory.jntubits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 23-Sep-15.
 */
public class FBQuestionsListFragment extends Fragment {
    private static String subject, midId, subjectId;
    private String[] question_ids, questions_list;
    private AppSettings appSettings;

    public static FBQuestionsListFragment newInstance(Bundle savedInstanceState) {
        Bundle args = savedInstanceState;

        subject = args.getString("subjectName");
        midId = args.getString("midId");
        subjectId = args.getString("subjectId");

        FBQuestionsListFragment fragment = new FBQuestionsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appSettings = new AppSettings(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_questions_list, container, false);

        List<String> questionIds = new ArrayList<String>();
        List<String> questions = new ArrayList<String>();

        try {
            DBHelper dbHelper = new DBHelper(getContext());
            questionIds = dbHelper.getQuestionIds(subjectId, midId, "0");
            questions = dbHelper.getQuestions(subjectId, midId, "0");
        }
        catch (IOException e) {
            Log.e("jntubits", "DB can't open in Questions List!");
        }

        questions_list = new String[questions.size()];
        questions.toArray(questions_list);

        question_ids = new String[questionIds.size()];
        questionIds.toArray(question_ids);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext() , android.R.layout.simple_list_item_1, questions_list);

        ListView qList = (ListView) view.findViewById(R.id.questionsListView);
        qList.setAdapter(adapter);
        qList.setOnItemClickListener(selectQuestion);

        return view;
    }

    private AdapterView.OnItemClickListener selectQuestion = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String mode = appSettings.getMode();
            Intent i = new Intent(getActivity().getApplicationContext(), FB.class);
            i.putExtra("mode", mode);
            i.putExtra("questionIds", question_ids);
            i.putExtra("position", position);
            //i.putExtra("questions", questions_list);
            startActivity(i);
        }
    };
}