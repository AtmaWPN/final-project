package com.usu.todosapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.usu.todosapplication.R;
import com.usu.todosapplication.model.Todo;

public class QuickAccessAdapter extends RecyclerView.Adapter<QuickAccessAdapter.QuickAccessViewHolder> {

    public interface OnQuickAccessClick {
        public void onClick(Todo todo);
    }

    ObservableArrayList<Todo> quickAccess;
    QuickAccessAdapter.OnQuickAccessClick eventListener;

    public QuickAccessAdapter(
            ObservableArrayList<Todo> quickAccess,
            OnQuickAccessClick eventListener
    ) {
        this.quickAccess = quickAccess;
        this.eventListener = eventListener;
        quickAccess.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Todo>>() {
            @Override
            public void onChanged(ObservableList<Todo> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Todo> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Todo> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Todo> sender, int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Todo> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @NonNull
    @Override
    public QuickAccessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuickAccessViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.quick_access_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull QuickAccessViewHolder holder, int position) {
        Todo quickAccessButton = quickAccess.get(position);
        Button button = holder.itemView.findViewById(R.id.button);
        button.setText(quickAccessButton.task);
        button.setOnClickListener(view -> eventListener.onClick(quickAccessButton));
    }

    @Override
    public int getItemCount() {
        return quickAccess.size();
    }

    class QuickAccessViewHolder extends RecyclerView.ViewHolder {
        public QuickAccessViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
