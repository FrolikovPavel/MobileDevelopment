package ru.mirea.frolikov.mireaproject;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileOutputStream;

import ru.mirea.frolikov.mireaproject.databinding.FragmentWorkWithFilesBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkWithFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkWithFilesFragment extends Fragment {
    private FragmentWorkWithFilesBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkWithFilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkWithFilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkWithFilesFragment newInstance(String param1, String param2) {
        WorkWithFilesFragment fragment = new WorkWithFilesFragment();
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
        binding = FragmentWorkWithFilesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextMessage.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Введите сообщение", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Integer.parseInt(binding.editTextShift.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "В поле \"сдвиг\" нужно ввести целое число", Toast.LENGTH_SHORT).show();
                    return;
                }
                SaveFileDialog dialog = new SaveFileDialog();
                dialog.show(WorkWithFilesFragment.this.getChildFragmentManager(), "save_file");
            }
        });
        return view;
    }
    public String cipher(String msg, int shift){
        while (shift < 0)
            shift += 26;
        while (shift > 26) {
            shift -= 26;
        }
        String s = "";
        int len = msg.length();
        for(int x = 0; x < len; x++){
            char c = msg.charAt(x);
            char new_c = (char) (msg.charAt(x) + shift);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                if ((c >= 'a' && new_c > 'z') || (c <= 'Z' && new_c > 'Z')) {
                    s += (char) (c - (26 - shift));
                } else {
                    s += new_c;
                }
            } else {
                s += c;
            }
        }
        return s;
    }
    public void onSaveClicked(String fileName) {
        FileOutputStream outputStream;
        try {
            outputStream = getActivity().openFileOutput(fileName, MODE_PRIVATE);
            String message = binding.editTextMessage.getText().toString();
            int shift = Integer.parseInt(binding.editTextShift.getText().toString());
            outputStream.write(cipher(message, shift).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}