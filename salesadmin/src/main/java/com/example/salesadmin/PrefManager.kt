package com.example.salesadmin

import android.content.Context
import android.content.SharedPreferences

class PrefManager (var context: Context) {
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
}