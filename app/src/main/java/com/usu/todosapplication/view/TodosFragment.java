package com.usu.todosapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.usu.todosapplication.R;
import com.usu.todosapplication.viewmodel.TodosViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TodosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        TodosViewModel viewModel = new ViewModelProvider(this).get(TodosViewModel.class);

        View view = inflater.inflate(R.layout.fragment_todos_fragment, container, false);

        view.findViewById(R.id.plusButton).setOnClickListener(button -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_todos_fragment_to_newTodoFragment);
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (!message.equals("")) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new TodosAdapter(viewModel.getTodos(), (todo) -> {
            viewModel.toggleTodoStatus(todo);
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}