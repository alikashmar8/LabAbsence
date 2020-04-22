package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorLabsFragment extends Fragment {


    Workbook workbook;
    Button chooseFile;
    Intent myFileIntent;
    List<String> name;
    List<String> id;
    int flag=0;
    String path="";
    TextView tt;

    public DoctorLabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_doctor_labs, container, false);
        tt=view.findViewById(R.id.tt);
        chooseFile=view.findViewById(R.id.chooseFile);
        name=new ArrayList<>();
        id=new ArrayList<>();

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFileIntent =new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent,10);
            }
        });
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    path = data.getData().getPath();
                    Log.d("tag3", path);
                    tt.setText(path);
                    WorkbookSettings ws = new WorkbookSettings();
                    ws.setGCDisabled(true);
                    //File file = new File(Environment.getExternalStorageDirectory() , path + ".txt");
                   File file = new File(path);
                    if (file.exists()) {
                        try {
                            //FileInputStream inputStream = new FileInputStream(file);
                            workbook = Workbook.getWorkbook(file);
                            Sheet sheet = workbook.getSheet(0);
                            for (int i = 0; i < sheet.getRows(); i++) {
                                Cell[] row = sheet.getRow(i);
                                id.add(row[0].getContents());
                                name.add(row[1].getContents());
                            }
                            Log.d("tag4", id + ":");
                            Log.d("tag4", name + "");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(),"Error reading file",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
