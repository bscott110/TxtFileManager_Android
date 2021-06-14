package com.example.file_test2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.file_test2.R;

import java.io.File;

public class ListFileAdapter extends ArrayAdapter<File> {
    Context context;
    File[] files;
    public ListFileAdapter(Context context, File[] files){
        super(context, R.layout.list_file, files);
        this.context=context;
        this.files=files;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view= LayoutInflater.from(context).inflate(R.layout.list_file,null);
       File file=files[position];
       TextView textViewFileName=view.findViewById(R.id.textViewFileName);
        textViewFileName.setText(file.getName());
        TextView textViewFileSize=view.findViewById(R.id.textViewFileSize);
        textViewFileSize.setText(String.valueOf(file.length()) + " byte(s)");

       return view;
    }
}
