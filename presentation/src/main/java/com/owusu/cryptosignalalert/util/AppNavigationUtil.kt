package com.owusu.cryptosignalalert.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.models.AlertListUIWrapper
import com.owusu.cryptosignalalert.util.ConstantsUI.Companion.FRAG_ALERT_LIST_DATA
import com.owusu.cryptosignalalert.util.ConstantsUI.Companion.FRAG_ALERT_LIST_TAG_NAME
import com.owusu.cryptosignalalert.views.fragments.FragmentAlertList

class AppNavigationUtil {
    companion object {

        fun navigateToFragmentAlertList(activity: AppCompatActivity) {

            if(doesFragmentExist(FRAG_ALERT_LIST_TAG_NAME, activity)) {
                return
            }

            //val bundle = Bundle()
            //bundle.putParcelable(FRAG_ALERT_LIST_DATA, alertListUIWrapper)

            val fragment = FragmentAlertList()
            addFragmentToView(
                getFragmentTransaction(activity),
                fragment,
                FRAG_ALERT_LIST_TAG_NAME)
        }

        private fun doesFragmentExist(tagName : String, activity: AppCompatActivity): Boolean {
            val fm = activity.supportFragmentManager
            if(fm.findFragmentByTag(tagName) == null) {
                return false
            }
            return true
        }

        private fun addFragmentToView(
            ft: FragmentTransaction,
            f: Fragment,
            tag: String) {
            addFragmentToView(ft, f, tag, null)
        }

        private fun addFragmentToView(
            ft: FragmentTransaction,
            f: Fragment,
            tag: String,
            bundle: Bundle?) {

            if (bundle != null) {
                f.arguments = bundle
            }

            ft.replace(R.id.fragment_container, f, tag)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        }

        private fun getFragmentTransaction(activity: AppCompatActivity): FragmentTransaction {
            return activity.supportFragmentManager.beginTransaction()
        }
    }
}