package firebase.smdeveloper.vvsn.todo_list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import firebase.smdeveloper.vvsn.todo_list.Adapter.ListItemAdapter;
import firebase.smdeveloper.vvsn.todo_list.Model.ToDo;

public class MainActivity extends AppCompatActivity {
    List<ToDo> toDoList = new ArrayList<>();
    FirebaseFirestore db;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    public MaterialEditText title,desc;//I made public because i want to access from ListAdapter
    public boolean isUpdate = false;//flag to check is update or is add new
    public String idUpdate = "";//Id of item need to update

    ListItemAdapter adapter;

    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init Firebase
        db = FirebaseFirestore.getInstance();

        dialog = new SpotsDialog(this);
        title = (MaterialEditText)findViewById(R.id.item_title);
        desc = (MaterialEditText)findViewById(R.id.item_desc);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add New
                if(isUpdate)
                {
                    setData(title.getText().toString(),desc.getText().toString());
                }
                else
                {
                    updateData(title.getText().toString(),desc.getText().toString());
                    isUpdate = !isUpdate;//reset Flag
                }

            }
        });

        listItem = (RecyclerView)findViewById(R.id.list_todo);


        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        loadData(); //load data from FireStore
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("DELETE"))
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        db.collection("ToDoList")
                .document(this.toDoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }

    private void updateData(String title, String desc) {
        db.collection("ToDoLIst").document(idUpdate)
                .update("title",title,"desc",desc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Update !",Toast.LENGTH_SHORT).show();
                    }
                });
        //Realtime update refresh data
        db.collection("ToDoList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void setData(String title, String desc) {
        //Random id
        String id = UUID.randomUUID().toString();
        Map<String,Object> todo =new HashMap<>();
        todo.put("id",id);
        todo.put("title",title);
        todo.put("desc",desc);

        db.collection("ToDoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Refresh data
                loadData();
            }
        });
    }

    private void loadData() {

        dialog.show();
        if(toDoList.size() > 0)
            toDoList.clear(); //Remove old Value

        db.collection("ToDoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult())
                        {
                            ToDo todo = new ToDo(doc.getString("id"),
                                        doc.getString("title"),
                                        doc.getString("desc"));
                            toDoList.add(todo);

                        }
                        adapter = new ListItemAdapter(MainActivity.this,toDoList);
                        listItem.setAdapter(adapter);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
