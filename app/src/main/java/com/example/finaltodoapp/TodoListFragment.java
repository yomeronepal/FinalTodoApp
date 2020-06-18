package com.example.finaltodoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finaltodoapp.model.Viewmodel.TodoViewModel;
import com.example.finaltodoapp.model.data.TodoRoomDatabase;
import com.example.finaltodoapp.model.entity.ETodo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;


public class TodoListFragment extends Fragment {
    View rootView;
    RecyclerView mTodoRecyclerView;
    TodoRoomDatabase database;

    TodoViewModel mTodoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_todo_list, container, false);
        mTodoRecyclerView=rootView.findViewById(R.id.recycle_todo);
        database=TodoRoomDatabase.getDatabase(getActivity().getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTodoRecyclerView.setLayoutManager(layoutManager);
        mTodoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        updateRV();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {


                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                List<ETodo> todoList = mTodoViewModel.getAllTodos().getValue();
                TodoAdapter adapter =new TodoAdapter(todoList);
                ETodo todo = adapter.getTodoAt(viewHolder.getAdapterPosition());
                mTodoViewModel.deleteById(todo);

            }
        }).attachToRecyclerView(mTodoRecyclerView);

        return rootView;
    }

    void updateRV()
    {
    mTodoViewModel.getAllTodos().observe(this, new Observer<List<ETodo>>() {
        @Override
        public void onChanged(List<ETodo> todoList) {

            TodoAdapter adapter = new TodoAdapter(todoList);
            mTodoRecyclerView.setAdapter(adapter);

        }
    });
    }

    private class TodoViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtTitle,txtDate;

        public TodoViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_todo,parent,false));
            txtTitle=itemView.findViewById(R.id.item_list_title);
            txtDate=itemView.findViewById(R.id.item_list_Description);

            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TodoAdapter adapter = new TodoAdapter(mTodoViewModel.getAllTodos().getValue());
                    int position = getAdapterPosition();
                    ETodo todo= adapter.getTodoAt(position);
                    Intent intent= new Intent(getActivity(),EditTodoActivity.class);
                    intent.putExtra("TodoId",todo.getId());
                    startActivity(intent);
                }
            });


        }
        public  void Bind(ETodo todo)
        {
            SimpleDateFormat dateFormater= new SimpleDateFormat("yyyy-MM-dd");
        txtTitle.setText(todo.getTitle());
        txtDate.setText(dateFormater.format(todo.getTodo_date()));
        }
    }
    private class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder>
    {
        List<ETodo> mETodoList;
        public TodoAdapter(List<ETodo> todoList)
        {
            mETodoList =todoList;
        }
        @NonNull
        @Override
        public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());

            return new TodoViewHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {

            ETodo todo =mETodoList.get(position);

            LinearLayout layout=(LinearLayout)(ViewGroup)holder.txtTitle.getParent();
            switch(todo.getPriority())
            {
                case 1:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_high_priority));
                    break;

                case 2:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_Medium_priority));
                    break;

                case 3:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_low_priority));
                    break;
            }

            holder.Bind(todo);

        }

        @Override
        public int getItemCount() {
            return mETodoList.size();
        }

        public ETodo getTodoAt(int index)
        {
            return mETodoList.get(index);
        }
    }
}