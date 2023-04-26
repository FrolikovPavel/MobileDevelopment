package ru.mirea.frolikov.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import ru.mirea.frolikov.mireaproject.databinding.FragmentAudioRecordBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioRecordFragment extends Fragment {

    private final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork;
    private FragmentAudioRecordBinding binding;
    private ImageButton recordButton = null;
    private ImageButton playButton = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    boolean isStartRecording = true;
    boolean isStartPlaying = true;
    private String recordFilePath = null;
    private int startTime;
    private int recordingLength;
    private TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AudioRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioRecordFragment newInstance(String param1, String param2) {
        AudioRecordFragment fragment = new AudioRecordFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAudioRecordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        textView = binding.textViewRecorder;
        recordButton = binding.imageButtonRecord;
        playButton = binding.imageButtonPlayRecording;
        playButton.setEnabled(false);
        recordFilePath = (new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();

        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissions(new String[] {android.Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRecording) {
                    recordButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_stop_button, null));
                    playButton.setEnabled(false);
                    playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button_disabled, null));
                    startTime = (int) System.currentTimeMillis();
                    textView.setText("Идет запись...");
                    startRecording();
                } else {
                    recordButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_button, null));
                    playButton.setEnabled(true);
                    playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button, null));
                    recordingLength = (int) System.currentTimeMillis() - startTime;
                    textView.setText(String.format("Запись завершена. Длина записи: %.1f с", (float) recordingLength/1000));
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartPlaying) {
                    playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pause_button, null));
                    recordButton.setEnabled(false);
                    recordButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_button_disabled, null));
                    textView.setText("Воспроизведение...");
                    startPlaying();
                } else {
                    playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button, null));
                    recordButton.setEnabled(true);
                    recordButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.record_button, null));
                    textView.setText(String.format("Длина записи: %.1f c", (float) recordingLength/1000));
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isWork) getActivity().finish();
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        recorder.start();
    }
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }
    private void stopPlaying() {
        player.release();
        player = null;
    }
}