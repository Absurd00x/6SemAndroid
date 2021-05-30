package com.example.mireaproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final String TAG = LoginFragment.class.getSimpleName();
    private FragmentActivity fa;
    private EditText login, password;
    private FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        fa = getActivity();
    }

    private void setNavigation(boolean val) {
         Menu menu = ((NavigationView)fa.findViewById(R.id.nav_view)).getMenu();
         for(int i = 0; i < menu.size(); ++i) {
             menu.getItem(i).setEnabled(val);
         }
    }

    @Override
    public void onStart() {
        super.onStart();
        setNavigation(false);
        fa.findViewById(R.id.buttonEnter).setOnClickListener(this::onLogin);
        fa.findViewById(R.id.buttonSignUp).setOnClickListener(this::onSignUp);
        login = fa.findViewById(R.id.login);
        password = fa.findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
    }

    private boolean checkData() {
        String mLogin = login.getText().toString();
        String mPassword = password.getText().toString();
        if (mLogin.isEmpty()) {
            login.setError("Необходимо заполнить");
            return false;
        }
        if (mPassword.isEmpty()) {
            password.setError("Необходимо заполнить");
            return false;
        }
        return true;
    }

    private void onSignUp(View view) {
        if (!checkData())
            return;
        auth.createUserWithEmailAndPassword(login.getText().toString(), password.getText().toString()).
                addOnCompleteListener(fa, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setNavigation(true);
                    Navigation.findNavController(fa, R.id.nav_host_fragment_content_main)
                            .navigate(R.id.nav_home);
                } else {
                    Log.e(TAG, "Не удалось создать пользователя", task.getException());
                    Toast.makeText(getContext(), "Не удалось создать пользователя", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onLogin(View view) {
        if (!checkData())
            return;
        auth.signInWithEmailAndPassword(login.getText().toString(), password.getText().toString()).
                addOnCompleteListener(fa, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    View view = fa.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)fa.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    setNavigation(true);
                    Navigation.findNavController(fa, R.id.nav_host_fragment_content_main)
                            .navigate(R.id.nav_home);
                } else {
                    Log.e(TAG, "Не удалось войти", task.getException());
                    Toast.makeText(getContext(), "Не удалось войти", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}