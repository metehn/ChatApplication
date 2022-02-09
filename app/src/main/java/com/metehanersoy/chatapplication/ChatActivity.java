package com.metehanersoy.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    EditText messageText;
    private ArrayList<String> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
/*
        chatMessages.add("sss");
        chatMessages.add("12312312");
        chatMessages.add("sfskd");
*/
        messageText = findViewById(R.id.chat_activity_message_text);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(chatMessages);

        RecyclerView.LayoutManager recyclerViewManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        getData();

    }

//Menu yaratıldığında çağırılacak metod, menu bağlama işlemini burada yapıyoruz
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

//Menuden item seçildiğinde çalışacak metod
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.option_menu_signout){
            auth.signOut();
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if(item.getItemId() == R.id.option_menu_profile){

            Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
            startActivity(intent);

        }else{

        }

        return super.onOptionsItemSelected(item);
    }

   public void sendMessage(View view){

        FirebaseUser user = auth.getCurrentUser();

        String messageToSend = messageText.getText().toString();

        UUID uuid = UUID.randomUUID();

        String uuidString = uuid.toString();
        String userEmailString = user.getEmail();

       databaseReference.child("Chats").child(uuidString).child("usermessage").setValue(messageToSend);
       databaseReference.child("Chats").child(uuidString).child("usermessagetime").setValue(ServerValue.TIMESTAMP);
       databaseReference.child("Chats").child(uuidString).child("useremail").setValue(userEmailString).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
           }
       });

        messageText.setText("");
   }

   public void getData() {

        DatabaseReference newDatabaseReference = database.getReference("Chats");
       Query query = newDatabaseReference.orderByChild("usermessagetime");
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               chatMessages.clear();

               for( DataSnapshot ds : snapshot.getChildren()){

                   HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                   String useremail = hashMap.get("useremail");
                   String userMessage = hashMap.get("usermessage");

                   chatMessages.add(userMessage);
                   recyclerViewAdapter.notifyDataSetChanged();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });

   }

}