package com.example.finaltodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class EditTodoActivity extends AppCompatActivity {
    Fragment mFragment;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        mFragment=new EditTodoFragment();
        mFragmentManager=getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container,mFragment)
                .commit();
    }
}
/*
<LinearLayout
android:layout_width="match_parent"
android:layout_height="match_parent"
android:layout_weight="1"
android:orientation="vertical">


<Button
    android:id="@+id/splash_btn_GoogleSignIn"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:text="@string/login_GoogleSignIn" />

<Button
    android:id="@+id/button2"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:text="Button" />
</LinearLayout>
*/