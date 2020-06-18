package com.example.finaltodoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.finaltodoapp.model.Viewmodel.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAddNew;
    Fragment mFragment;
    FragmentManager mFragmentManager;

    TodoViewModel mTodoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragment=new TodoListFragment();
        mFragmentManager=getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.list_container,mFragment)
                .commit();

        fabAddNew = findViewById(R.id.fab_add_new_todo);

        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,EditTodoActivity.class);
                startActivity(intent);
            }
        });

        mTodoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_delete_all:
            mTodoViewModel.deleteAll();
            break;

            case R.id.menu_logout:
                SharedPreferences preferences =getApplicationContext().getSharedPreferences("todo_pref",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                FirebaseAuth.getInstance().signOut();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}