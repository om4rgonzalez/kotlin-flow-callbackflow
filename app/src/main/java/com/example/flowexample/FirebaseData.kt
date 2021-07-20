package com.example.flowexample

import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FirebaseData {

    private val CHILD = "prefix"
    private val QUANTITY_CHARACTERS_CHILD = "quantityCharacters"
    private val QUANTITY_CHARACTERS_DEFAULT = 0
    private val QUANTITY_CHARACTERS_PREFERENCES_DEFAULT = 4
    private val QUANTITY_CHARACTERS_PREFERENCES = "QuantityCharacters"
    val LAST_GENERATED_DEFAULT = ""


    private val lastPrefixReference: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference
            .child("countries")
            .child("BRT")
            .child("document")
            .child("prefix")
            .child("lastGenerated")


    @ExperimentalCoroutinesApi
    fun getDataFromFirebase() = callbackFlow<String> {
        lastPrefixReference.also { lastGeneratedReferenceIt ->
            lastGeneratedReferenceIt.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.value.toString().toInt().inc().also { lastGeneratedIncIt ->
                        lastGeneratedReferenceIt.setValue(lastGeneratedIncIt)
                        offer(lastGeneratedIncIt.toString())
                        channel.close()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    offer(LAST_GENERATED_DEFAULT)
                    channel.close()
                }
            })

            awaitClose()
        }
    }



}