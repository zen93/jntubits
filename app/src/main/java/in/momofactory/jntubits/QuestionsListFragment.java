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
public class QuestionsListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final int MCQ = 1;
    public static final int FB = 2;
    private static String subject, midId, subjectId;

    private int mPage;
    private AppSettings appSettings;


    onInitListener mCallback;

    public interface onInitListener {
         void onQuestionsInit();
    }

    public static QuestionsListFragment newInstance(int page, Bundle savedInstanceState) {
        Bundle args = savedInstanceState;
        args.putInt(ARG_PAGE, page);

        subject = args.getString("subjectName");
        midId = args.getString("midId");
        subjectId = args.getString("subjectId");

        QuestionsListFragment fragment = new QuestionsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        try {
            mCallback = (onInitListener) activity;
            Toast.makeText(getContext(), "onAttach()!", Toast.LENGTH_LONG).show();
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onInitLstener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        appSettings = new AppSettings(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_questions_list, container, false);
        List<String> questionIds = new ArrayList<String>();
        List<String> questions = new ArrayList<String>();
        String[] questions_list, question_ids;

        try {
            DBHelper dbHelper = new DBHelper(getContext());
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
            if(mPage == MCQ) {
                //MCQ
                Intent i = new Intent(getActivity().getApplicationContext(), MCQ.class);
                i.putExtra("mode", mode);
                startActivity(i);
            }
            else {
                if(mPage == FB) {
                   //Rev Mode FB
                    Intent i = new Intent(getActivity().getApplicationContext(), FB.class);
                    i.putExtra("mode", mode);
                    startActivity(i);
                    }
                }
            }

        };
}
