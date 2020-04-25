package com.example.lababsencesystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class EditCourseStudentsFragment extends Fragment {


    TextView textImport,textShow;
    EditText enterStudentId;
    Button chooseFile,search,submit;
    Intent myFileIntent;
    List<String> name;
    List<String> id;
    Workbook workbook;

    public EditCourseStudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_student, container, false);

        textShow=view.findViewById(R.id.textShow);
        textImport=view.findViewById(R.id.textImport);
        chooseFile=view.findViewById(R.id.chooseFile);
        enterStudentId=view.findViewById(R.id.enterStudentId);
        search=view.findViewById(R.id.search);
        submit=view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);

        name = new ArrayList<>();
        id = new ArrayList<>();



        String getCourseCode="";
        Intent intent=getActivity().getIntent();
        getCourseCode=intent.getStringExtra("CourseCode");
        textShow.setText(getCourseCode);

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent, 10);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int flag=0;
        String path="";
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    path = data.getData().getPath();
                    Log.d("tag3", path);
                    textImport.setText(path);
                    WorkbookSettings ws = new WorkbookSettings();
                    ws.setGCDisabled(true);
                    File file = new File(path);
                    if (file.exists()) {
                        try {
                            workbook = Workbook.getWorkbook(file);
                            Sheet sheet = workbook.getSheet(0);
                            int j;
                            for (j=0;j<sheet.getColumns();j++);
                            if (j!=1)
                                flag=1;
                            if (flag==0) {
                                for (int i = 0; i < sheet.getRows(); i++) {
                                    Cell[] row = sheet.getRow(i);
                                    id.add(row[0].getContents());
                                }
                                Log.d("tag4", id + ":");
                                Log.d("tag4", name + "");
                                submit.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(getActivity(),"invalid format",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (BiffException e) {
                            Toast.makeText(getActivity(),"make sure your file .xls",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(),"File does not exist",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}
