package com.example.college.db;


import com.example.college.db_modules.Message;
import com.example.college.db_modules.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class DatabaseHelper {
    private DatabaseReference databaseReference;


    public DatabaseHelper(int type)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://college-c1047-default-rtdb.europe-west1.firebasedatabase.app");
        if (type == 0)
        { // User
            databaseReference = db.getReference(User.class.getSimpleName());
        }else { // Chat
            databaseReference = db.getReference(Message.class.getSimpleName());
        }


    }


    public Task<Void> addUser(User user, String id)
    {
        return databaseReference.child(id).setValue(user);
    }

    public Task<Void> addMessage(Message message)
    {
        return databaseReference.push().setValue(message);
    }


    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }


    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public DatabaseReference getDatabaseReference()
    {
        return databaseReference;
    }

}
