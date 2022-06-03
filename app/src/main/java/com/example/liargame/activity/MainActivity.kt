package com.example.liargame.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.liargame.DEFINES.DEFINES
import com.example.liargame.DEFINES.SubjectEnum
import com.example.liargame.R
import com.example.liargame.fragment.GameFragment
import com.example.liargame.fragment.SettingFragment
import com.example.liargame.listener.OnGameEventListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * 메인화면
 *
 * TODO
 * 1. 게임을 플레이할 수 있는 실제 Fragment를 넣어둠
 * 2. 주제 표출
 * 3. 주제 변경
 * 4. 재시작
 * 5. 뒤로가기 (앱 종료)
 * 6. 무작위로 제시어 선택하기
 */
class MainActivity : AppCompatActivity(), OnGameEventListener {

    private var mFragment : Fragment? = null

    private var backPressedTime : Int = 0

    private var transaction : FragmentTransaction? = null

    private var mWord : String? = null

    /**
     * 변경 버튼, 재시작 visible 처리
     * 
     * true 
     * 1. 변경 버튼 보이게 
     * 2. 재시작 버튼 안 보이게
     * 
     * false
     * 1. 변경 버튼 안 보이게
     * 2. 재시작 버튼 보이게
     */
    private var clickableSubjectBtn : Boolean by Delegates.observable(true) { _, oldValue, newValue ->
        Log.d("[MainActivity]", "clickableSubjectBtn -> old : $oldValue / new : $newValue")
        if (mFragment != null) {
            when (newValue) {
                true -> {
                    findViewById<ConstraintLayout>(R.id.activity_main_subject_btn_layout).visibility = View.VISIBLE
                    findViewById<ImageButton>(R.id.activity_main_restart_button).visibility = View.GONE
                }
                false -> {
                    findViewById<ConstraintLayout>(R.id.activity_main_subject_btn_layout).visibility = View.GONE
                    findViewById<ImageButton>(R.id.activity_main_restart_button).visibility = View.VISIBLE
                }
            }
        } else {
            Log.d("[MainActivity]", "clickableSubjectBtn -> Fragment is null")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기 Fragment (SettingFragment) call
        transaction = supportFragmentManager.beginTransaction()
        transaction!!.replace(R.id.activity_main_game_frame_layout, SettingFragment(this))
        transaction!!.commit()

        initWidgets()
    }

    override fun onResume() {
        super.onResume()

        // 변경 버튼 visible 분기
        when (mFragment) {
            is SettingFragment -> {
                clickableSubjectBtn = true
            }
            else -> {
                clickableSubjectBtn = false
            }
        }
    }

    // 뒤로가기 버튼 클릭 시 호출
    override fun onBackPressed() {
        if (backPressedTime == 0) {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            backPressedTime = System.currentTimeMillis().toInt()
        } else {
            if (System.currentTimeMillis() - backPressedTime > 2000) {
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                backPressedTime = System.currentTimeMillis().toInt()
            } else {
                super.onBackPressed()
                moveTaskToBack(true)
                finish()
            }
        }
    }

    // 버튼 클릭 이벤트 동작 선언
    private fun initWidgets() {

        // 변경 버튼
        findViewById<ConstraintLayout>(R.id.activity_main_subject_btn_layout).setOnClickListener {
            // 팝업을 띄울지 다른 Fragment를 띄우게 할 지 정해야 함.
        }

        // 재시작 버튼
        findViewById<ImageButton>(R.id.activity_main_restart_button).setOnClickListener {
            resetGame()
        }
    }

    override fun onGameStartListener() {
        Log.d("[MainActivity]", "onGameStartListener called -> Game Start!!")
        try {
            selectAnswer()
            if (mWord != null) {
                transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction!!.add(R.id.activity_main_game_frame_layout, GameFragment(mWord!!, this))
                transaction!!.addToBackStack(null)
                transaction!!.commit()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    override fun onGameEndListener() {
        Log.d("[MainActivity]"," onGameEndListener called -> Game End!!")
        resetGame()
    }

    private fun resetGame() {
        try {
            transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction!!.replace(R.id.activity_main_game_frame_layout, SettingFragment(this))
            transaction!!.commit()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun selectAnswer() {
        var mList : ArrayList<String>? = null
        when (DEFINES.SUBJECT) {
            SubjectEnum.OBJECT -> mList = DEFINES.WORD.OBJECT_LIST
            SubjectEnum.FOOD -> mList = DEFINES.WORD.FOOD_LIST
            SubjectEnum.JOB -> mList = DEFINES.WORD.JOB_LIST
        }
        var index = Random().nextInt(mList!!.size)
        mWord = mList.get(index)
    }
}