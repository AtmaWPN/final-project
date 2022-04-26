package com.usu.todosapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usu.todosapplication.R;
import com.usu.todosapplication.viewmodel.NewTodoViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewTodoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_todo, container, false);
        // Inflate the layout for this fragment
        NewTodoViewModel viewModel = new ViewModelProvider(this)
                .get(NewTodoViewModel.class);

        viewModel.getErrorMessage().observe(this, (message) -> {
            TextView errorView = view.findViewById(R.id.errorMessage);
            errorView.setText(message);
        });

        viewModel.getSaving().observe(this, saving -> {
            Button button = view.findViewById(R.id.saveButton);
            if (saving) {
                button.setEnabled(false);
                button.setText("Saving...");
            } else {
                button.setEnabled(true);
                button.setText("Save");
            }
        });

        viewModel.getSaveSuccess().observe(this, saveSuccess -> {
            if (saveSuccess) {
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        view.findViewById(R.id.saveButton).setOnClickListener(button -> {
            EditText taskText = view.findViewById(R.id.task);
            EditText quantity = view.findViewById(R.id.newQty);
            try {
                viewModel.saveTodo(taskText.getText().toString(), Integer.parseInt(quantity.getText().toString()));
            } catch (NumberFormatException e) {
                viewModel.saveTodo(taskText.getText().toString(), 1);
            }
        });
        return view;
    }
}