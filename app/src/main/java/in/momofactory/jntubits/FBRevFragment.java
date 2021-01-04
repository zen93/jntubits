package in.momofactory.jntubits;

import android.database.DataSetObserver;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class FBRevFragment extends Fragment {
    TextView questionTextView;
    ListView ansListView;

    Button next;

    String[] questionIds;
    int count;

    public static FBRevFragment newInstance(String[] questionIds, int pos) {
        FBRevFragment fragment = new FBRevFragment();
        Bundle args = new Bundle();
        args.putStringArray("questionIds", questionIds);
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public FBRevFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        questionIds = getArguments().getStringArray("questionIds");
        count = getArguments().getInt("position", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //Title through bundle

        View rootView = inflater.inflate(R.layout.fragment_fbrev, container, false);
        questionTextView = (TextView) rootView.findViewById(R.id.fbRevQuestionTextView);
        ansListView = (ListView) rootView.findViewById(R.id.fbRevAnswersList);

        //fbLL = (LinearLayout) rootView.findViewById(R.id.fbLL);
        questionTextView = (TextView) rootView.findViewById(R.id.fbRevQuestionTextView);
        ansListView = (ListView) rootView.findViewById(R.id.fbRevAnswersList);



        next = (Button) rootView.findViewById(R.id.fbRevSubmitButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                loadNextQuestion();
            }
        });

        loadNextQuestion();
        //ansListView.setItemsCanFocus(false);
        return rootView;

        /*

        String[] answer_choices = {"1. law", "2. weight",};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, answer_choices) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row;

                if (null == convertView) {
                    row = getLayoutInflater(savedInstanceState).inflate(android.R.layout.simple_list_item_1, null);
                } else {
                    row = convertView;
                }

                TextView tv = (TextView) row.findViewById(android.R.id.text1);
                tv.setText(Html.fromHtml(getItem(position)));
                //tv.setText(getItem(position));

                return row;
            }

        };
        ansListView.setAdapter(adapter);
        */


/*

        for(int i=0;i<noOfAnswers;i++) {
            ansTextView[i] = new TextView(getContext());
            ansTextView[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            ansTextView[i].setText(Html.fromHtml(answers[i]));
            ansTextView[i].setTextAppearance(getContext(), R.style.AnsBlankText);
            fbLL.addView(ansTextView[i]);
        }

        nextButton = new Button(getContext());
        nextButton.setText("NEXT");
        fbLL.addView(nextButton);

        //loadNextQuestion();

        //Button next = (Button) rootView.findViewById(R.id.fbRevSubmitButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadNextQuestion();
            }
        });
    */
    }

    private void loadNextQuestion() {
        if(!(count < questionIds.length)) {
            return;
        }
        if(!(count < questionIds.length-1)) {
            next.setVisibility(View.GONE);
        }

        String question = "";
        List<String> answers = new ArrayList<String>();
        String correct = "";
        DBHelper dbHelper;
        try {
            dbHelper = new DBHelper(getContext());
            question = dbHelper.getQuestion(questionIds[count]);
            answers = dbHelper.getAnswers(questionIds[count]);
        }
        catch (IOException e) {
            Log.e("jntubits.", "Oops");
        }

        questionTextView.setText(question);
        String[] answer_choices = new String[answers.size()];
        answers.toArray(answer_choices);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, answer_choices);
        ansListView.setAdapter(adapter);
    }
}
