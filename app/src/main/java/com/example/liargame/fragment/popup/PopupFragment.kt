package com.example.liargame.fragment.popup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.liargame.DEFINES.DEFINES
import com.example.liargame.DEFINES.SubjectEnum
import com.example.liargame.R
import com.example.liargame.listener.OnPopupDismissListener
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class PopupFragment(bundle : Bundle, answer : String?, list : ArrayList<String>, onPopupDismissListener: OnPopupDismissListener) : DialogFragment() {

    private var mBundle : Bundle

    private var mAnswer : String? = null

    private var mList : ArrayList<String>? = null

    private var buttonList : ArrayList<TextView>? = null

    private var mPopupDismissListener : OnPopupDismissListener

    private var mAnswerIndex : Int = 0

    init {
        mBundle = bundle
        mAnswer = answer
        mList = list
        mPopupDismissListener = onPopupDismissListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonList = arrayListOf(
            view.findViewById(R.id.popup_0),
            view.findViewById(R.id.popup_1),
            view.findViewById(R.id.popup_2),
            view.findViewById(R.id.popup_3),
            view.findViewById(R.id.popup_4),
            view.findViewById(R.id.popup_5),
            view.findViewById(R.id.popup_6),
            view.findViewById(R.id.popup_7),
            view.findViewById(R.id.popup_8)
        )

        for (i in 0 until buttonList!!.size) {
            buttonList!![i].visibility = View.INVISIBLE
            buttonList!![i].isClickable = false
        }

        try {

            when (mBundle.getString("BRANCH")) {
                "주제" -> {
                    view.findViewById<TextView>(R.id.fragment_popup_title_text)?.text = "주제를 선택해주세요."
                    for (i in 0 until mList!!.size) {
                        buttonList!![i].visibility = View.VISIBLE
                        buttonList!![i].text = mList!![i]
                        buttonList!![i].isClickable = true
                    }
                }
                "제시어" -> {
                    view.findViewById<TextView>(R.id.fragment_popup_title_text)?.text = "제시어를 선택해주세요."
                    mAnswerIndex = Random().nextInt(DEFINES.HINT_COUNT)
                    for (i in 0 until DEFINES.HINT_COUNT) {
                        buttonList!![i].visibility = View.VISIBLE
                        buttonList!![i].text = mList!![i]
                        buttonList!![i].isClickable = true
                    }
                    buttonList!![mAnswerIndex].text = mAnswer
                }
                else -> {
                    Log.d("[PopupFragment]", "onViewCreate() -> BRANCH is null")
                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }

    }

    override fun onStart() {
        super.onStart()

        if (view != null) {
            for (i in 0 until buttonList!!.size) {
                buttonList!![i].setOnClickListener {
                    if (i == mAnswerIndex) {
                        mPopupDismissListener.onPopupDismissListener(false)
                        dismiss()
                    }
                    mPopupDismissListener.onPopupDismissListener(true)
                    dismiss()
                }
            }
        }
    }
}