package com.example.lababsencesystem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import static android.app.Activity.RESULT_OK;

public class EditCourseStudentsFragment extends Fragment {


    ProgressBar progressBar;
    TextView textImport,textShow,eror;
    EditText enterStudentId;
    Button chooseFile,search,submit;
    Intent myFileIntent;
    List<String> name;
    List<String> id;
    Workbook workbook;
    ArrayList<Student> students;
    ArrayList<CourseStudent> cs;
    int flag=0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int STORAGE_PERMISSION_CODE = 1;

    public EditCourseStudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_edit_student, container, false);

        textShow=view.findViewById(R.id.textShow);
        textImport=view.findViewById(R.id.textImport);
        chooseFile=view.findViewById(R.id.chooseFile);
        enterStudentId=view.findViewById(R.id.enterStudentId);
        search=view.findViewById(R.id.search);
        submit=view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        progressBar=view.findViewById(R.id.prbar);
        progressBar.setVisibility(View.GONE);
        eror=view.findViewById(R.id.eror);
        eror.setVisibility(View.GONE);

        name = new ArrayList<>();
        id = new ArrayList<>();

        students=new ArrayList<>();
        cs=new ArrayList<>();

        String getCourseCode="";
        Intent intent=getActivity().getIntent();
        getCourseCode=intent.getStringExtra("CourseCode");
        textShow.setText(getCourseCode);


        final RecyclerView rv = view.findViewById(R.id.addDeleteRv);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);

        final String finalGetCourseCode = getCourseCode;

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                students.clear();
                cs.clear();
                eror.setVisibility(View.GONE);
                final String idToSearch = enterStudentId.getText().toString();
                if (!idToSearch.equals("")) {
                    search.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    final String[] strings = idToSearch.split("-");

                    if (strings.length == 2) {

                        //////////


                        /////////
                        flag=2;
                        db.collection("courses").document(finalGetCourseCode).collection("students").whereLessThanOrEqualTo("fileNumber", Integer.parseInt(strings[1])).whereGreaterThanOrEqualTo("fileNumber", Integer.parseInt(strings[0])).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){

                                    if (task.getResult().size() > 0)
                                        for (QueryDocumentSnapshot document : task.getResult()){
//                                            if (document.getLong("fileNumber").intValue() >= Integer.parseInt(strings[0])
//                                                    && document.getLong("fileNumber").intValue() <= Integer.parseInt(strings[1])){
                                                String name = document.getString("name");
                                                int filenb = document.getLong("fileNumber").intValue();
                                                CourseStudent ss = new CourseStudent(name, filenb);
                                                cs.add(ss);
//                                            }
                                        }

                                    db.collection("users").document("students").collection("data").whereLessThanOrEqualTo("fileNumber", Integer.parseInt(strings[1])).whereGreaterThanOrEqualTo("fileNumber", Integer.parseInt(strings[0])).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
//                                                int ln=cs.size();
                                                if (task.getResult().size() > 0)
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                                        if (document.getLong("fileNumber").intValue() >= Integer.parseInt(strings[0])
//                                                                && document.getLong("fileNumber").intValue() <= Integer.parseInt(strings[1])) {
                                                            String name = document.getString("name");
                                                            String username = document.getString("username");
                                                            String password = document.getString("password");
                                                            String email = document.getString("email");
                                                            String type = document.getString("type");
                                                            int filenb = document.getLong("fileNumber").intValue();

                                                            Student st = new Student(name, email, username, password, filenb, type);
                                                            students.add(st);
//                                                        }
                                                    }
                                                for (int l = 0; l < students.size(); l++) {
                                                    for (int o = 0; o < cs.size(); o++) {
                                                        if (cs.get(o).getFileNumber() == students.get(l).getFileNumber())
                                                            students.remove(l);

                                                    }
                                                }
                                                search.setEnabled(true);
                                                RecyclerView.Adapter a = new DoctorAddDeleteStudentAdapter(students, cs, flag, finalGetCourseCode);
                                                rv.setAdapter(a);
                                                progressBar.setVisibility(View.GONE);
                                            }
//                                            try {
//                                                Thread.sleep(1000);
//
//
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
                                        }
                                    });
                                }
                            }
                        });

                    } else{

                        db.collection("courses").document(finalGetCourseCode).collection("students").document(idToSearch).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String name = task.getResult().getString("name");
                                        int filenb = task.getResult().getLong("fileNumber").intValue();

                                        CourseStudent ss = new CourseStudent(name, filenb);
                                        cs.add(ss);
                                        flag = 1;
                                        search.setEnabled(true);
                                        RecyclerView.Adapter a = new DoctorAddDeleteStudentAdapter(students, cs, flag, finalGetCourseCode);
                                        rv.setAdapter(a);
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        db.collection("users").document("students").collection("data").document(idToSearch).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        String name = task.getResult().getString("name");
                                                        String username = task.getResult().getString("username");
                                                        String password = task.getResult().getString("password");
                                                        String email = task.getResult().getString("email");
                                                        String type = task.getResult().getString("type");
                                                        int filenb = task.getResult().getLong("fileNumber").intValue();

                                                        Student st = new Student(name, email, username, password, filenb, type);
                                                        students.add(st);
                                                        flag = 0;
                                                        search.setEnabled(true);
                                                        RecyclerView.Adapter a = new DoctorAddDeleteStudentAdapter(students, cs, flag, finalGetCourseCode);
                                                        rv.setAdapter(a);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                    else{
                                                        eror.setText("File Number doesn't exists");
                                                        search.setEnabled(true);
                                                        eror.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        });

                                    }

//                                    try {
//                                        Thread.sleep(1000);
//
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                }
                            }
                        });
                }
                }

            }
        });

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
        Uri uri;
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                   // path = data.getData().getPath();

                    Log.d("tag3", path);
                    textImport.setText(path);
                    //WorkbookSettings ws = new WorkbookSettings();
                    //ws.setGCDisabled(true);
                    //File file = new File(path);
                  /*  if (file.exists()) {
                        try {
                            FileInputStream is = new FileInputStream(file);
                            workbook = Workbook.getWorkbook(is);
                            Toast.makeText(getActivity(),path,Toast.LENGTH_LONG).show();
                            Sheet sheet = workbook.getSheet(0);
                            int j;
                            for (j=0;j<sheet.getColumns();j++);
                            if (j!=2)
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
                    }*/
                    uri = data.getData();
                    path = getPath(getActivity(), uri);
                    String filenameArray[] = path.split("\\.");
                    String extension = filenameArray[filenameArray.length-1];

                    if (extension.equals("xlsx")) {

                        File inputFile = new File(path);


                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            try {
                                InputStream inputStream = new FileInputStream(inputFile);
                                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                                XSSFSheet sheet = workbook.getSheetAt(0);
                                int rowsCount = sheet.getPhysicalNumberOfRows();
                                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                                StringBuilder sb = new StringBuilder();

                                //outter loop, loops through rows
                                for (int r = 1; r < rowsCount; r++) {
                                    Row row = sheet.getRow(r);
                                    int cellsCount = row.getPhysicalNumberOfCells();

                                        //inner loop, loops through columns
                                        for (int c = 0; c < cellsCount; c++) {
                                            //handles if there are to many columns on the excel sheet.
                                            if (c >= 2) {
                                                //Toast.makeText(getActivity(), "invalid format", Toast.LENGTH_SHORT).show();
                                               // break;
                                            } else {
                                                String value = getCellAsString(row, c, formulaEvaluator);
                                                String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                                                sb.append(value + ", ");
                                            }
                                        }

                                    sb.append(":");
                                }submit.setVisibility(View.VISIBLE);

                                //parseStringBuilder(sb);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            requestStoragePermission();
                        }
                    }else
                        Toast.makeText(getActivity(), "please choose .xlsx file", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

        }
        return value;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;


                if("primary".equalsIgnoreCase(type)){

                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                } else {

                    if(Environment.isExternalStorageRemovable()){
                        storageDefinition = "EXTERNAL_STORAGE";

                    } else{
                        storageDefinition = "SECONDARY_STORAGE";
                    }

                    return System.getenv(storageDefinition) + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {// DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);

            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}
