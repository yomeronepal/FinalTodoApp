package com.example.finaltodoapp.model.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finaltodoapp.model.data.TodoRepository;
import com.example.finaltodoapp.model.entity.ETodo;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mTodoRepository;
    private LiveData<List<ETodo>> mAllTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        mTodoRepository=new TodoRepository(application);
        mAllTodos =mTodoRepository.getAllTodoList();
    }
    public void insert(ETodo todo)
    {
        mTodoRepository.insert(todo);
    }

    public ETodo getTodoById(int id)
    {
        return mTodoRepository.getTodoById(id);
    }


    public LiveData<List<ETodo>> getAllTodos()
    {
        return mAllTodos;
    }

    public void deleteById(ETodo todo)
    {
        mTodoRepository.deleteById(todo);
    }

    public void deleteAll()
    {
        mTodoRepository.deleteAll();
    }

    public void update(ETodo todo)
    {
        mTodoRepository.update(todo);
    }
}
