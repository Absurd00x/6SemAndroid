package ru.mirea.petrov.mireaproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoriesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = StoriesFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static StoryDAO dao;
    private FragmentActivity fa;
    private StoryRecyclerAdapter adapter;
    private FloatingActionButton fab;

    public StoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoriesFragment newInstance(String param1, String param2) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dao = Database.getInstance(getContext()).getStoryDAO();
        fa = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        fa = getActivity();

        RecyclerView rv = fa.findViewById(R.id.storiesRecycleView);
        rv.setLayoutManager(new LinearLayoutManager(fa));
        adapter = new StoryRecyclerAdapter(fa, new ArrayList<Story>());
        adapter.addActionCallback(new StoryRecyclerAdapter.ActionCallback() {
            @Override
            public void onLongClickListener(Story story) {
                dao.delete(story);
                loadStories();
            }
        });
        rv.setAdapter(adapter);

        fab = fa.findViewById(R.id.buttonAddStory);

        fab.setOnClickListener(this::addStory);

        loadStories();
    }

    private void addStory(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText input = new EditText(getContext());
        final int INPUT_ID = 123;
        input.setId(INPUT_ID);
        builder.setView(input);
        builder.setTitle("Запишите историю:");
        builder.setNegativeButton("Отмена", (dialog, which) -> {});
        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Story story = new Story();
                story.date = new Date();
                story.content = input.getText().toString();
                try {
                    dao.insert(story);
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(fa, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                }
                loadStories();
            }
        });
        builder.show();
    }

    private void loadStories() {
        Log.i(TAG, "Updating");
        adapter.updateData(dao.getStories());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stories, container, false);
    }
}