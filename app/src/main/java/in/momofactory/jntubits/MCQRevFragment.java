package in.momofactory.jntubits;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MCQRevFragment extends Fragment {
    TextView questionTextView;
    ListView ansListView;
    Button next;

    String[] questionIds;
    int count;

    public static MCQRevFragment newInstance(String[] questionIds, int pos) {
        MCQRevFragment fragment = new MCQRevFragment();
        Bundle args = new Bundle();
        args.putStringArray("questionIds", questionIds);
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public MCQRevFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        questionIds = getArguments().getStringArray("questionIds");
        count = getArguments().getInt("position", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Title through bundle

        View rootView = inflater.inflate(R.layout.fragment_mcqrev, container, false);
        questionTextView = (TextView) rootView.findViewById(R.id.mcqRevQuestionTextView);
        ansListView = (ListView) rootView.findViewById(R.id.mcqRevAnswerChoicesList);


        next = (Button) rootView.findViewById(R.id.mcqRevSubmitButton);
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
            correct = dbHelper.getCorrectAnswer(questionIds[count]);
        }
        catch (IOException e) {
            Log.e("jntubits.", "Oops");
        }
        final String correctAnswer = correct;
        questionTextView.setText(question);
        String[] answer_choices = new String[answers.size()];
        answers.toArray(answer_choices);

        int i;
        for(i=0;i<answer_choices.length;i++) {
            if(correctAnswer.contentEquals(answer_choices[i]))
                break;
        }

        final int answerPos = i;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, answer_choices);
        ansListView.setAdapter(adapter);
        ansListView.setItemChecked(answerPos, true);
        ansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((ListView) adapterView).setItemChecked(answerPos, true);
            }
        });
    }
}
