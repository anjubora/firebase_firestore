package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.CaseMap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";
    private EditText title;
    private EditText description;
    private TextView textView;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());

        title=findViewById(R.id.title);
        description=findViewById(R.id.description);

    }

    public void saveNote(View v){
        String t=title.getText().toString();
        String d=description.getText().toString();
        Map<String,Object> note=new HashMap<>();
        note.put(KEY_TITLE,t);
        note.put(KEY_DESCRIPTION,d);

        db.collection("Notebook").add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){

                    public void onSuccess(DocumentReference documentReference){
                        Toast.makeText(MainActivity.this,"note saved",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void loadNotes(View v){

        db.collection("Notebook")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String data="";
                            for (QueryDocumentSnapshot document : task.getResult()) {

                             data+="Title :"+document.get(KEY_TITLE)+"\nDescription :"+document.get(KEY_DESCRIPTION)+"\n\n";

                            }
                            textView.setText(data);


                        } else {
                            Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}
