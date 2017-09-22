package com.rlard.rlard008.texttopdf;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelfNoteFragment extends Fragment {


    EditText mSubjectEditText,mBodyEditText;
    Button mSaveButton;

    File myFile;

    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_self_note, container, false);

        mSubjectEditText = (EditText) mRootView.findViewById(R.id.edit_text_subject);
        mBodyEditText = (EditText) mRootView.findViewById(R.id.edit_text_body);
        mSaveButton = (Button) mRootView.findViewById(R.id.button_save);


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubjectEditText.getText().toString().isEmpty()){
                    mSubjectEditText.setError("Subject is empty");
                    mSubjectEditText.requestFocus();
                    return;
                }

                if (mBodyEditText.getText().toString().isEmpty()){
                    mBodyEditText.setError("Body is empty");
                    mBodyEditText.requestFocus();
                    return;
                }



                boolean hasPermission = (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                }


               // try {
                    createPdf();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
            }
        });



        return mRootView;
    }

    private void createPdf(){


//        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");

        File pdfFolder = new File(Environment.getExternalStorageDirectory(), "pdfdemo");

        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.e("", "Pdf Directory created");
        }

        Log.e("pdfFolder"+pdfFolder, "Pdf Directory created");

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder +File.separator+ timeStamp + ".pdf");

        try {
            OutputStream output = new FileOutputStream(myFile);
            //Step 1
            Document document = new Document();

            //Step 2
            PdfWriter.getInstance(document, output);

            //Step 3
            document.open();

            //Step 4 Add content
            document.add(new Paragraph(mSubjectEditText.getText().toString()));
            document.add(new Paragraph(mBodyEditText.getText().toString()));

            //Step 5: Close the document
            document.close();

            promptForNextAction();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        //viewPdf();

    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void promptForNextAction()
    {
//        final String[] options = { getString(R.string.label_email), getString(R.string.label_preview),
//                getString(R.string.label_cancel) };

//        String options = "view";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Note Saved, What Next?");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                if (options[which].equals(getString(R.string.label_email))){
////                    emailNote();
////                }else if (options[which].equals(getString(R.string.label_preview))){
//                    viewPdf();
////                }else if (options[which].equals(getString(R.string.label_cancel))){
////                    dialog.dismiss();
////                }
//            }
//        });

        builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                viewPdf();
            }
        })

        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

//        builder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                viewPdf();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(getContext(), "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
