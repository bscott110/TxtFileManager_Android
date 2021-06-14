package com.example.file_test2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.system.ErrnoException;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.file_test2.adapters.ListFileAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static android.system.Os.chmod;


public class MainActivity extends AppCompatActivity {
    public ListView ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView = findViewById(R.id.ListView);

        registerForContextMenu(ListView);
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.ListView) {
            menu.add(getText(R.string.del));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create) {
            openCreateFiledialog();
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            delfile(item);
        }
        return true;
    }

    public void delfile(MenuItem item) {
        try {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            View view = adapterContextMenuInfo.targetView;
            TextView textViewFileName = view.findViewById(R.id.textViewFileName);
            String filename = textViewFileName.getText().toString();
            for (File file : getFilesDir().listFiles()) {
                if (file.getName().equalsIgnoreCase(filename)) {
                    file.delete();
                    break;
                }
            }
            loadData();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openCreateFiledialog() {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.create_file, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getText(R.string.file));
        builder.setView(view);
        builder.setCancelable(false);
        Dialog dialog = builder.show();
        EditText editTextName = view.findViewById(R.id.editTextname);
        EditText editTextContent = view.findViewById(R.id.editTextContent);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button buttonSave = view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                saveFile(editTextName.getText().toString(), editTextContent.getText().toString());
                String filecontent=editTextContent.getText().toString().trim();
                String filepath="MyFileDir";
                if(!filecontent.equals("")){
                    File exfile= new File(getExternalFilesDir(filepath),editTextName.getText().toString());
                    try {
                        chmod(exfile.getAbsolutePath(), 6);
                    } catch (ErrnoException e) {
                        e.printStackTrace();
                    }
                    FileOutputStream fos=null;
                    try {
                        fos=new FileOutputStream(exfile);
                        fos.write(filecontent.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Info saved to SD.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Couldn't be saved to SD.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public void saveFile(String filename, String content) {
        try {

            File file = new File(getFilesDir() + File.separator + filename);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
            loadData();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    public void loadData() {
        File dir = getFilesDir();
        ListFileAdapter listFileAdapter = new ListFileAdapter
                (getApplicationContext(), dir.listFiles());
        ListView.setAdapter(listFileAdapter);
    }

    public void saveToStorage(String filename, String content){

        try{
            File path= Environment.getExternalStorageDirectory();
            File dir= new File(path+"/data/"+filename);
            if (!dir.exists())
            {
                dir.mkdir();
            }

            File file= new File(dir,filename);
            FileWriter fw= new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw= new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            Toast.makeText(this, filename+"is saved to"+dir,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean is_ex_avail(){
        String exstate= Environment.getExternalStorageState();
        if(exstate.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }
}