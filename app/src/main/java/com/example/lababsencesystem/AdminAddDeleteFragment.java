package com.example.lababsencesystem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Queue;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddDeleteFragment extends Fragment {

    Button addManualButton, addManualCancelButton, addManualSubmitButton;
    LinearLayout linearLayoutAddManualAdmin;
    EditText FileNumberAddEditText, nameAddEditText, emailAddEditText;
    Spinner courseSpinnerAdmin;

    EditText enterIdAdmin, NameSearch, typeSearch;
    Button searchAdmin, cancelSearch, submitSearch, chooseExcel;
    LinearLayout linearLayoutSearchAdmin;
    TextView erorrAdmin;
    ProgressBar prbarAdmin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> coursesCodeAdmin = new ArrayList<>();

    Intent myFileIntent;
    private String fileNumber = "";
    private ArrayList<Student> excelStudents;
    private ArrayList<Student> notRegisteredStudents;
    private ArrayList<Student> registeredStudents;
    private int STORAGE_PERMISSION_CODE = 1;
    private ProgressBar loadingExcel;
    private TextView excelResult;


    public AdminAddDeleteFragment() {
        // Required empty public constructor
    }

    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;


                if ("primary".equalsIgnoreCase(type)) {

                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                } else {

                    if (Environment.isExternalStorageRemovable()) {
                        storageDefinition = "EXTERNAL_STORAGE";

                    } else {
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

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_delete, container, false);

        linearLayoutAddManualAdmin = view.findViewById(R.id.linearLayoutAddManualAdmin);
        addManualButton = view.findViewById(R.id.addManualButton);
        addManualCancelButton = view.findViewById(R.id.addManualCancelButton);
        addManualSubmitButton = view.findViewById(R.id.addManualSubmitButton);
        courseSpinnerAdmin = view.findViewById(R.id.courseSpinnerAdmin);
        FileNumberAddEditText = view.findViewById(R.id.FileNumberAdd);
        emailAddEditText = view.findViewById(R.id.emailAdd);
        nameAddEditText = view.findViewById(R.id.nameAdd);
        loadingExcel = view.findViewById(R.id.excelLoading);
        excelResult = view.findViewById(R.id.excelTextResult);

        typeSearch = view.findViewById(R.id.typeSearch);
        NameSearch = view.findViewById(R.id.NameSearch);
        enterIdAdmin = view.findViewById(R.id.enterIdAdmin);
        searchAdmin = view.findViewById(R.id.searchAdmin);
        cancelSearch = view.findViewById(R.id.cancelSearch);
        submitSearch = view.findViewById(R.id.submitSearch);
        linearLayoutSearchAdmin = view.findViewById(R.id.linearLayoutSearchAdmin);
        erorrAdmin = view.findViewById(R.id.erorrAdmin);
        prbarAdmin = view.findViewById(R.id.prbarAdmin);
        chooseExcel = view.findViewById(R.id.chooseFileAdmin);

        excelStudents = new ArrayList<Student>();
        notRegisteredStudents = new ArrayList<Student>();
        registeredStudents = new ArrayList<>();

        linearLayoutAddManualAdmin.setVisibility(View.GONE);
        linearLayoutSearchAdmin.setVisibility(View.GONE);

        coursesCodeAdmin.clear();
        coursesCodeAdmin.add("student");
        coursesCodeAdmin.add("doctor");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, coursesCodeAdmin);
        courseSpinnerAdmin.setAdapter(adapter);

        addManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddManualAdmin.setVisibility(View.VISIBLE);
                nameAddEditText.setText("");
                emailAddEditText.setText("");
                FileNumberAddEditText.setText("");
            }
        });

        addManualCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddManualAdmin.setVisibility(View.GONE);
            }
        });

        addManualSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameAddEditText.getText().toString();
                String fileNb = FileNumberAddEditText.getText().toString();
                String email = emailAddEditText.getText().toString();
                String text = courseSpinnerAdmin.getSelectedItem().toString();

                int flag = 0;
                if (name.equals("")) {
                    nameAddEditText.setError("Name is Required");
                    flag = 1;
                }
                if (fileNb.equals("")) {
                    FileNumberAddEditText.setError("FileNumber is Required");
                    flag = 1;
                }
                if (!isValidEmail(email)) {
                    emailAddEditText.setError("Invalid Email Format");
                    flag = 1;
                }
                if (email.equals("")) {
                    emailAddEditText.setError("Email is Required");
                    flag = 1;
                }
                if (flag == 1)
                    return;

                //      Log.d("asssa",name+" ,"+email+" ,"+fileNb+" , "+text);
                if (text.equals("student")) {
                    Log.d("asssa", name + " ," + email + " ," + fileNb + " , " + text);
                    Student st = new Student(name, email, fileNb, "666666", Integer.parseInt(fileNb), text);
                    db.collection("users").document("students").collection("data").document(fileNb).set(st);
                } else {
                    //          Log.d("asssa",name+" ,"+email+" ,"+fileNb+" , "+text);
                    Doctor st = new Doctor(name, email, fileNb, "666666", Integer.parseInt(fileNb), text);
                    db.collection("users").document("doctors").collection("data").document(fileNb).set(st);
                }
                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                linearLayoutAddManualAdmin.setVisibility(View.GONE);

            }
        });

        searchAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSearchAdmin.setVisibility(View.GONE);
                final String idToSearch = enterIdAdmin.getText().toString();
                if (!idToSearch.equals("")) {
                    prbarAdmin.setVisibility(View.VISIBLE);
                    erorrAdmin.setVisibility(View.GONE);
                    searchAdmin.setEnabled(false);
                    db.collection("users").document("students").collection("data").document(idToSearch).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    String name = task.getResult().getString("name");
                                    String type = task.getResult().getString("type");
                                    fileNumber = idToSearch;
                                    linearLayoutSearchAdmin.setVisibility(View.VISIBLE);
                                    NameSearch.setText(name);
                                    typeSearch.setText(type);
                                    searchAdmin.setEnabled(true);
                                    prbarAdmin.setVisibility(View.GONE);
                                } else {
                                    db.collection("users").document("doctors").collection("data").document(idToSearch).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    String name = task.getResult().getString("name");
                                                    String type = task.getResult().getString("type");
                                                    fileNumber = idToSearch;
                                                    linearLayoutSearchAdmin.setVisibility(View.VISIBLE);
                                                    NameSearch.setText(name);
                                                    typeSearch.setText(type);
                                                    searchAdmin.setEnabled(true);
                                                    prbarAdmin.setVisibility(View.GONE);
                                                } else {
                                                    linearLayoutSearchAdmin.setVisibility(View.GONE);
                                                    erorrAdmin.setText("FileNumber doesn't Exist");
                                                    erorrAdmin.setVisibility(View.VISIBLE);
                                                    searchAdmin.setEnabled(true);
                                                    prbarAdmin.setVisibility(View.GONE);

                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSearchAdmin.setVisibility(View.GONE);
                fileNumber = "";
            }
        });

        submitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(typeSearch.getText().toString() + "s").collection("data").document(fileNumber).delete();
                linearLayoutSearchAdmin.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        chooseExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                } else {
                    myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    myFileIntent.setType("*/*");
                    startActivityForResult(myFileIntent, 10);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chooseExcel.setVisibility(View.GONE);
        loadingExcel.setVisibility(View.VISIBLE);

//        if(resultCode != RESULT_CANCELED) {
//            if (requestCode == 0) {
        int flag = 0;
        String path = "";
        Uri uri;
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {

//                    textImport.setText(path);
                    uri = data.getData();
                    path = getPath(getActivity(), uri);
                    String filenameArray[] = path.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];

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
                                for (int r = 0; r < rowsCount; r++) {
                                    Row row = sheet.getRow(r);
                                    int columnsCount = row.getPhysicalNumberOfCells();
                                    //inner loop, loops through columns
                                    if (columnsCount > 3) {
                                        Toast.makeText(getActivity(), "invalid format", Toast.LENGTH_SHORT).show();
                                        flag = 1;
                                        break;
                                    } else {
                                        String fileNumber = getCellAsString(row, 0, formulaEvaluator);
//                                        String cellInfo = "r:" + r + "; c:" + 0 + "; v:" + fileNumber;
                                        sb.append(fileNumber + ", ");

                                        String name = getCellAsString(row, 1, formulaEvaluator);
//                                        String cellInfo2 = "r:" + r + "; c:" + 1 + "; v:" + name;
                                        sb.append(name + ", ");

                                        String email = getCellAsString(row, 2, formulaEvaluator);
//                                        String cellInfo2 = "r:" + r + "; c:" + 1 + "; v:" + name;
                                        sb.append(email + ", ");

                                        int fn = (int) Double.parseDouble(fileNumber);

                                        Student cs = new Student(name, email, fileNumber, 666666 + "", fn, "student");
                                        excelStudents.add(cs);

                                    }

                                    sb.append(":");
                                }//for each row
                                if (flag == 1) {
                                    //format not correct
                                    Toast.makeText(getContext(), "error files", Toast.LENGTH_SHORT).show();
                                } else {
                                    AddStudents(0);
                                }


                                //parseStringBuilder(sb);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            requestStoragePermission();
                        }
                    } else
                        Toast.makeText(getActivity(), "please choose .xlsx file", Toast.LENGTH_SHORT).show();

                }
                break;
        }
//            }
//        }
    }

    private void AddStudents(int i) {
        Log.d("excel", "add students reached");
        if (excelStudents.size() > 0) {
            final int finalI = i;
            db.collection("users").document("students").collection("data").document(excelStudents.get(i).getFileNumber() + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            //student is found in db
                            //error already registered
                            registeredStudents.add(excelStudents.get(i));
                            excelStudents.remove(i);
                            AddStudents(i);
                        } else {
                            //student not registered in db
                            notRegisteredStudents.add(excelStudents.get(finalI));
                            excelStudents.remove(finalI);
                            AddStudents(i);

                        }
                    }
                }
            });

        } else {
            loadingExcel.setVisibility(View.GONE);
            chooseExcel.setVisibility(View.VISIBLE);
            excelResult.setText(" Students to add = " + notRegisteredStudents.size() + "\n Students already exists = " + registeredStudents.size() + "\n If you would like to proceed please submit");
            excelResult.setVisibility(View.VISIBLE);
//            submit.setVisibility(View.VISIBLE);

        }
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
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
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
