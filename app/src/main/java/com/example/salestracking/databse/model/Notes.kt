package com.example.salestracking.databse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Notes (

        @PrimaryKey(autoGenerate = true)
        var id:Long=0L,

        @ColumnInfo(name = "title")
        var title:String,

        @ColumnInfo(name = "note")
        var note:String
):Serializable



//<?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android" xmlns:tools="http://schemas.android.com/tools"
//android:orientation="vertical"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content">
//
//<androidx.cardview.widget.CardView
//android:layout_margin="4dp"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content">
//
//<RelativeLayout
//android:padding="8dp"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content">
//
//<TextView
//android:textAppearance="@style/Base.TextAppearance.AppCompat.Headline"
//tools:text="My First Note"
//android:layout_marginBottom="10dp"
//android:id="@+id/text_view_title"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"/>
//
//<TextView
//android:textAppearance="@style/Base.TextAppearance.AppCompat.Medium"
//tools:text="Note Description"
//android:layout_below="@id/text_view_title"
//android:id="@+id/text_view_note"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"/>
//
//</RelativeLayout>
//
//</androidx.cardview.widget.CardView>
//
//
//</LinearLayout>