package com.arnoldvaz27.remarques.activities;

public class test {
    //CREATE NOTE ACTIVITY
    /*    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            if (sharedText.startsWith("https://") || sharedText.startsWith("http://")) {
                layoutWebURL.setVisibility(View.VISIBLE);
                MoreUrl.setVisibility(View.VISIBLE);
                textWebURL.setText(sharedText);
            } else {
                inputNoteText.setText(sharedText);
            }
        }
    }

    void handleSendImage(Intent intent) {
        selectImageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (selectImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageNote.setImageBitmap(bitmap);
                imageNote.setVisibility(View.VISIBLE);
                findViewById(R.id.imageMoreImage).setVisibility(View.VISIBLE);
                selectedImagePath = getPathFromUri(selectImageUri);
            } catch (Exception e) {
                Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    /*    private void SelectingFileType() {
        addingFileType = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.filetypelayout, (ViewGroup) findViewById(R.id.layoutFileType));

        sheetView.findViewById(R.id.layoutPdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "pdf";
                Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                startActivity(intent);
                addingFileType.dismiss();
            }
        });
        sheetView.findViewById(R.id.layoutWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "docx";
                Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                startActivity(intent);
                addingFileType.dismiss();
            }
        });
        sheetView.findViewById(R.id.layoutExcel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "xlsx";
                Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                startActivity(intent);
                addingFileType.dismiss();
            }
        });
        sheetView.findViewById(R.id.layoutPowerpoint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingFileType.dismiss();
                AddingClip();
            }
        });
        sheetView.findViewById(R.id.layoutText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "txt";
                Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                startActivity(intent);
                addingFileType.dismiss();
            }
        });
        sheetView.findViewById(R.id.layoutZip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "zip";
                Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                startActivity(intent);
                addingFileType.dismiss();
            }
        });
        sheetView.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "zip";
                Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                startActivity(intent);
                addingFileType.dismiss();
            }
        });
        addingFileType.setContentView(sheetView);
        addingFileType.show();
    }*/

    /*

    inputtitle
            time
    timelive
            subtitle
    weburl
            note*/

/*    private void settingColor()
    {

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.layout_miscellaneous, (ViewGroup) findViewById(R.id.layoutMiscellaneous));
        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty())
        {
            switch (alreadyAvailableNote.getColor())
            {
                case "#FDBE3B" :
                    sheetView.findViewById(R.id.viewColor2).performClick();

                    setSubTitleIndicatorColor();

                    break;
                case "#FF4842" :
                    sheetView.findViewById(R.id.viewColor3).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#3a52fc" :
                    sheetView.findViewById(R.id.viewColor4).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#000000" :
                    sheetView.findViewById(R.id.viewColor5).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#EE82EE" :
                    sheetView.findViewById(R.id.viewColor6).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#800080" :
                    sheetView.findViewById(R.id.viewColor7).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#000080" :
                    sheetView.findViewById(R.id.viewColor8).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#0d98ba" :
                    sheetView.findViewById(R.id.viewColor9).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#7cfc00" :
                    sheetView.findViewById(R.id.viewColor10).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#006400" :
                    sheetView.findViewById(R.id.viewColor11).performClick();
                    setSubTitleIndicatorColor();
                    break;
                case "#fb7268" :
                    sheetView.findViewById(R.id.viewColor12).performClick();
                    setSubTitleIndicatorColor();
                    break;
            }
        }
    }*/

    /*
     * ₹
     *
     * */

//    private void createPDF1() throws Exception {
//        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
//                    showMessage(new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//                        }
//                    });
//                    return;
//                }
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
//            }
//        } else {
//            Toast.makeText(this, "TEXT copy has been saved to this device successfully", Toast.LENGTH_LONG).show();
//            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/My Notes/"+"Word Files");
//            if (!docsFolder.exists()) {
//                docsFolder.mkdir();
//            }
//            Random r = new Random( System.currentTimeMillis() );
//            int randomCode =  ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000));
//            String randCode = Integer.toString(randomCode);
//            pdfFile = new File(docsFolder.getAbsolutePath(), randCode+".doc");
//            OutputStream outputStream = new FileOutputStream(pdfFile);
//            Document document = new Document();
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            Font f=new Font(Font.FontFamily.TIMES_ROMAN,18f,Font.BOLD, BaseColor.RED);
//            document.add(new Paragraph("Title",f));
//
//            Font f1 =new Font(Font.FontFamily.TIMES_ROMAN,15f,Font.NORMAL, BaseColor.BLACK);
//            document.add(new Paragraph("\n\n"+inputNoteTitle.getText().toString(),f1));
//
//            Font f2 =new Font(Font.FontFamily.TIMES_ROMAN,18f,Font.BOLD, BaseColor.RED);
//            document.add(new Paragraph("\n\n"+"Note",f2));
//
//            Font f3 =new Font(Font.FontFamily.TIMES_ROMAN,15f,Font.BOLD, BaseColor.BLACK);
//            document.add(new Paragraph("\n\n"+inputNoteText.getText().toString(),f3));
//
//
//            document.close();
//        }
//    }
    //CREATE NOTE ACTIVITY CLOSE


}
