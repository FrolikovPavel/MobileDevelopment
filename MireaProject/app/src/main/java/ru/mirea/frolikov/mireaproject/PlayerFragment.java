package ru.mirea.frolikov.mireaproject;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.frolikov.mireaproject.databinding.FragmentPlayerBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {
    FragmentPlayerBinding binding;
    private int PermissionCode = 200;
    private static boolean isPlaying = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String param1, String param2) {
        PlayerFragment fragment = new PlayerFragment();
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isPlaying", isPlaying);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        if (savedInstanceState != null && savedInstanceState.getBoolean("isPlaying")) {
            binding.textViewTrackName.setText(getString(R.string.track_name));
            binding.imageViewAlbumArt.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.album_art, null));
            binding.imageButtonPlay.setEnabled(false);
            binding.imageButtonPlay.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button_disabled, null));
            binding.imageButtonPause.setEnabled(true);
            binding.imageButtonPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pause_button, null));
        } else {
            binding.textViewTrackName.setText(getString(R.string.music_player));
            binding.imageViewAlbumArt.setImageDrawable(null);
            binding.imageButtonPlay.setEnabled(true);
            binding.imageButtonPlay.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button, null));
            binding.imageButtonPause.setEnabled(false);
            binding.imageButtonPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pause_button_disabled, null));
        }

        if (ContextCompat.checkSelfPermission(getActivity(), POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения получены");
        } else {
            Log.d(MainActivity.class.getSimpleName().toString(), "Нет разрешений!");
            ActivityCompat.requestPermissions(getActivity(), new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE}, PermissionCode);
        }

        binding.imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getActivity(), PlayerService.class);
                getActivity().startForegroundService(serviceIntent);
                binding.textViewTrackName.setText(getString(R.string.track_name));
                binding.imageViewAlbumArt.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.album_art, null));
                binding.imageButtonPlay.setEnabled(false);
                binding.imageButtonPlay.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button_disabled, null));
                binding.imageButtonPause.setEnabled(true);
                binding.imageButtonPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pause_button, null));
                isPlaying = true;
            }
        });

        binding.imageButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(new Intent(getActivity(), PlayerService.class));
                binding.textViewTrackName.setText(getString(R.string.music_player));
                binding.imageViewAlbumArt.setImageDrawable(null);
                binding.imageButtonPlay.setEnabled(true);
                binding.imageButtonPlay.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.play_button, null));
                binding.imageButtonPause.setEnabled(false);
                binding.imageButtonPause.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.pause_button_disabled, null));
                isPlaying = false;
            }
        });
        return view;
    }
}