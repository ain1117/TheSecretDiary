package fastcampus.aop_part1.thesecretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {

    private val diaryEditText : EditText by lazy { //edit텍스트 할당
        findViewById(R.id.diaryEditText)
    }

    private val handler = Handler(Looper.getMainLooper()) //메인 스레드에 연결된 핸들러 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE) // sharedPreference파일을 만든다.

        diaryEditText.setText(detailPreferences.getString("detail", ""))



        val runnable = Runnable { //사용자가 멈칫할때마다 저장한다.
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit{
                putString("detail", diaryEditText.text.toString())



            }
            Log.d("DiaryActivity", "SAVE!! ${diaryEditText.text.toString()}")
        }

        /* diaryEditText.addTextChangedListener{ //에딧텍스트가 수정될 때 마다 해당 리스너가 불려온다.
            detailPreferences.edit { //에디터를 불러와서
                putString("detail", diaryEditText.text.toString()) //디테일에 저장한다.
            }
        }*/

        /*ui스레드 = 메인 스레드가 있음
        //새로운 스레드 =/ 메인 스레드
        메인 스레드가 아닌 곳에서는 ui 체인지가 안되기 때문에, 메인 스레드와 다른 스레드를 연결하기 위해 핸들러를 사용한다. */

        diaryEditText.addTextChangedListener() {

            Log.d("diaryActivity", "TextChanged :: $it")
            handler.removeCallbacks(runnable)

            handler.postDelayed(runnable, 500)
        }


    }

}