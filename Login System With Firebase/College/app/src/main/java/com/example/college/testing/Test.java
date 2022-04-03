package com.example.college.testing;

import com.example.college.db.DatabaseHelper;
import com.example.college.db_modules.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class Test {
    private static DatabaseHelper db0 = new DatabaseHelper(0); // user
    private static DatabaseHelper db1 = new DatabaseHelper(1); // chats

    public static void writeMessage(int count)
    {
        for (int i = 0; i < count; i++)
        {
            Message message = new Message();
            message.setMessageText("test" + i);
            message.setMessageTime(new Date().getTime());
            message.setMessageUser("tester");
            db1.addMessage(message);
        }
    }
    
    
    public static void getAllMessages()
    {
        db1.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                assert message != null;
                System.out.println(message.getMessageText());
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
