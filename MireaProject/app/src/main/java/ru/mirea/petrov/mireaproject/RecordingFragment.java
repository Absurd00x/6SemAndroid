package ru.mirea.petrov.mireaproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final String TAG = MainActivity.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentActivity fa;
    private MediaRecorder mediaRecorder;
    private File audioFile = null;
    private Button startRecording;
    private Button endRecording;
    private Button playRecording;
    private Button stopRecording;

    public RecordingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordingFragment newInstance(String param1, String param2) {
        RecordingFragment fragment = new RecordingFragment();
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
    }

    private boolean tryGetPermissions() {
        int audioPermissionStatus = ContextCompat.checkSelfPermission(fa, Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(fa, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioPermissionStatus == PackageManager.PERMISSION_DENIED
                || storagePermissionStatus == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(fa, "У приложения недостаточно прав\nдля этой операции!",
                    Toast.LENGTH_LONG).show();
            // Выполняется запрос к пользователю на получение необходимых разрешений
            ActivityCompat.requestPermissions(fa, new String[]{
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);

            audioPermissionStatus = ContextCompat.checkSelfPermission(fa, Manifest.permission.RECORD_AUDIO);
            storagePermissionStatus = ContextCompat.checkSelfPermission(fa, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return audioPermissionStatus == PackageManager.PERMISSION_GRANTED
                && storagePermissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onStart() {
        super.onStart();
        fa = getActivity();
        startRecording = fa.findViewById(R.id.buttonStartRecording);
        endRecording = fa.findViewById(R.id.buttonEndRecording);
        playRecording = fa.findViewById(R.id.buttonPlayRecording);
        stopRecording = fa.findViewById(R.id.buttonStopRecording);

        startRecording.setOnClickListener(v -> {
            try {
                onStartClick(startRecording);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endRecording.setOnClickListener(v -> onEndClick(endRecording));
        playRecording.setOnClickListener(v -> onPlayClick(playRecording));
        stopRecording.setOnClickListener(v -> onStopClick(stopRecording));

        tryGetPermissions();
    }

    public void onStartClick(View v) throws IOException {
        if (!tryGetPermissions())
            return;
        fa.stopService(new Intent(fa, MyService.class));

        startRecording.setEnabled(false);
        playRecording.setEnabled(false);
        stopRecording.setEnabled(false);
        endRecording.setEnabled(true);

        mediaRecorder = new MediaRecorder();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                ||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "sd-card success");
            // выбор источника звука
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // выбор формата данных
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // выбор кодека
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if (audioFile == null) {
                // создание файла
                audioFile = new File(fa.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "mirea.3gp");
            }
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(fa, "Начало записи!", Toast.LENGTH_SHORT).show();
        }
    }
    public void onEndClick(View v) {
        if (!tryGetPermissions())
            return;
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        startRecording.setEnabled(true);
        endRecording.setEnabled(false);
        playRecording.setEnabled(true);
        startRecording.setEnabled(true);

        Log.d(TAG, "processAudioFile");
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        // установка meta данных созданному файлу
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
        ContentResolver contentResolver = fa.getContentResolver();
        Log.d(TAG, "audioFile: " + audioFile.canRead());
        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(baseUri, values);
        // оповещение системы о новом файле
        fa.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(fa, "Запись завершена!", Toast.LENGTH_SHORT).show();
    }

    private void launchService() {
        Intent intent = new Intent(fa, MyService.class);
        intent.putExtra("uri", Uri.fromFile(audioFile).toString());
        fa.startService(intent);
    }

    public void onPlayClick(View v) {
        stopRecording.setEnabled(true);
        launchService();
    }
    public void onStopClick(View v) {
        stopRecording.setEnabled(false);
        fa.stopService(new Intent(fa, MyService.class));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recording, container, false);
    }
}