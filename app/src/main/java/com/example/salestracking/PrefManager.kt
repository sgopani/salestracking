package com.example.salestracking

import android.content.Context
import android.content.SharedPreferences
import java.net.Inet6Address

class PrefManager(var context: Context) {
    private  var mPreference: SharedPreferences

    init {
        mPreference = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    companion object {
        private const val PREF_NAME = "Sales-pref";
        private const val PRIVATE_MODE = 0;
        private const val KEY_FULL_NAME = "FULL_NAME";
        private const val KEY_EMAIL = "EMAIL";
        private const val ADDRESS="ADDRESS"
        private const val PHONENO="PHONENO"
        private const val COMPANYID="COMPANYID"
        private const val DOJ="DOJ"
        private const val ISCHECKEDIN="ISCHECKEDiN"
    }
    fun setFullName(fullName: String) {
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putString(KEY_FULL_NAME, fullName);
        editor.apply()
    }
    fun getFullName() : String? {
        return mPreference.getString(KEY_FULL_NAME, "")
    }
    fun setEmail(email: String) {
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply()
    }

    fun getEmail() : String? {
        return mPreference.getString(KEY_EMAIL, "")
    }
    fun setAddress(address: String) {
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putString(ADDRESS, address);
        editor.apply()
    }

    fun getAddress() : String? {
        return mPreference.getString(ADDRESS, "")
    }

    fun setPhone(phoneNo: String) {
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putString(PHONENO, phoneNo);
        editor.apply()
    }

    fun getPhoneNo() : String? {
        return mPreference.getString(PHONENO, "")

    }
    fun setCompanyID(companyId: String) {
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putString(COMPANYID, companyId);
        editor.apply()
    }

    fun getCompanyId() : String? {
        return mPreference.getString(COMPANYID, "")
    }
    fun setDOJ(timeInMillisecond: Long) {
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putLong(DOJ, timeInMillisecond);
        editor.apply()
    }

    fun getDOJ() : Long? {
        return mPreference.getLong(DOJ, 0L)
    }
    fun setIsCheckedIn(ischeckIn:Boolean){
        val editor : SharedPreferences.Editor = mPreference.edit();
        editor.putBoolean(ISCHECKEDIN, ischeckIn);
        editor.apply()
    }
    fun getIsCheckedIn(): Boolean {
        return mPreference.getBoolean(ISCHECKEDIN,false)
    }


}


//<?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//xmlns:app="http://schemas.android.com/apk/res-auto"
//xmlns:tools="http://schemas.android.com/tools"
//android:layout_width="match_parent"
//android:layout_height="match_parent"
//tools:context=".profile.ProfileInfo">
//
//<Button
//android:id="@+id/add_employee_button3"
//android:layout_width="0dp"
//android:layout_height="48dp"
//android:layout_marginStart="32dp"
//android:layout_marginLeft="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:layout_marginBottom="32dp"
//android:background="@drawable/btn_custom"
//android:text="@string/sign_Out"
//app:layout_constraintBottom_toBottomOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent" />
//
//<Button
//android:id="@+id/add_employee_button2"
//android:layout_width="0dp"
//android:layout_height="48dp"
//android:layout_marginStart="32dp"
//android:layout_marginLeft="32dp"
//android:layout_marginTop="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:background="@drawable/btn_custom"
//android:text="@string/update"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/et_profile_address" />
//
//<EditText
//android:id="@+id/et_profile_address"
//android:layout_width="0dp"
//android:layout_height="@dimen/edittext_height"
//android:layout_marginStart="32dp"
//android:layout_marginLeft="32dp"
//android:layout_marginTop="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:autofillHints=""
//android:background="@drawable/et_custom"
//android:ems="10"
//android:enabled="false"
//android:hint="@string/admin_address"
//android:inputType="textCapSentences|textMultiLine"
//android:paddingLeft="10dp"
//android:textSize="@dimen/text_size"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="1.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/et_profile_email" />
//
//<EditText
//android:id="@+id/et_profile_email"
//android:layout_width="0dp"
//android:layout_height="@dimen/edittext_height"
//android:layout_marginStart="32dp"
//android:layout_marginLeft="32dp"
//android:layout_marginTop="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:autofillHints=""
//android:background="@drawable/et_custom"
//android:ems="10"
//android:enabled="false"
//android:hint="@string/admin_email"
//android:inputType="textEmailAddress"
//android:paddingLeft="10dp"
//android:textSize="@dimen/text_size"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="1.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/et_profilePhoneno" />
//
//<EditText
//android:id="@+id/et_profilePhoneno"
//android:layout_width="0dp"
//android:layout_height="@dimen/edittext_height"
//android:layout_marginStart="32dp"
//android:layout_marginLeft="32dp"
//android:layout_marginTop="32dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:autofillHints=""
//android:background="@drawable/et_custom"
//android:ems="10"
//android:enabled="false"
//android:hint="@string/admin_phone"
//android:inputType="phone"
//android:paddingLeft="10dp"
//android:textSize="@dimen/text_size"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/et_profile_name" />
//
//<EditText
//android:id="@+id/et_profile_name"
//android:layout_width="0dp"
//android:layout_height="@dimen/edittext_height"
//android:layout_marginStart="32dp"
//android:layout_marginLeft="32dp"
//android:layout_marginTop="48dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:autofillHints=""
//android:background="@drawable/et_custom"
//android:ems="10"
//android:enabled="false"
//android:hint="@string/name"
//android:inputType="textPersonName"
//android:paddingLeft="10dp"
//android:textSize="@dimen/text_size"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toTopOf="parent" />
//</androidx.constraintlayout.widget.ConstraintLayout>