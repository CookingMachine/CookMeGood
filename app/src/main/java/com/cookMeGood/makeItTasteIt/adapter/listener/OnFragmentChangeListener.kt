package com.cookMeGood.makeItTasteIt.adapter.listener

import androidx.fragment.app.Fragment
import com.api.model.Category

interface OnFragmentChangeListener {

    fun replaceFragment(fragment: Fragment, category: Category)

}