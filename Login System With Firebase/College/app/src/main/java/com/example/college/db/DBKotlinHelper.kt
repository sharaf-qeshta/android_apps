package com.example.college.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.college.db_modules.Message
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class DBKotlinHelper: ViewModel() {
    private var dbchats = FirebaseDatabase.getInstance("https://college-c1047-default-rtdb.europe-west1.firebasedatabase.app").getReference(Message.getSimpleName())
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> get() = _message

    fun addMessage(message: Message)
    {
        val id = dbchats.push().key
        dbchats.child(id!!).setValue(message).addOnSuccessListener {
            _result.value = null

        }.addOnFailureListener {
            _result.value = it.cause as Exception?
        }
    }





    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val message = snapshot.getValue(Message::class.java)
            _message.value = message
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
           print("changed")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            print("removed")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            print("moved")
        }

        override fun onCancelled(error: DatabaseError) {
            print("cancelled")
        }

    }


    fun getRealTimeUpdate(){
        dbchats.addChildEventListener(childEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        dbchats.removeEventListener(childEventListener)
    }
}