package fastcampus.aop_part1.thesecretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1 :NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.NumberPicker1)
            .apply { //apply 함수 사용 시 this를 통해 바로 객체에 값 할당 가능
                minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker2 :NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.NumberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val numberPicker3 :NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.NumberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton : AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    } //AppCompatButton인 이유 : 기본 테마 스타일 적용 안하고 내가 원하는 배경색같은거 설정하려고..(테마없는버튼)

    private val changePasswordButton : AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePsswordMode = false //패스워드 변경 모드 들어갔을 때 값 boolean으로 설정

    override fun onCreate(savedInstanceState: Bundle?) { //뷰가 다 그려지는 시점 = onCreate실행
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener{ //open버튼을 누르면(비밀번호를 입력하고 다이어를 열기 위한 자물쇠 버튼)

            if (changePsswordMode) { //일단 비번 재설정 모드인지 확인해서, 만약 그렇다면 열리지 않고 돌아가도록 한다.
                Toast.makeText(this, "비밀번호 재설정 중입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //SharedPreferences : 데이터를 파일로 저장한다. 파일이 앱의 폴더 내에 저장되므로 앱을 삭제할 경우 데이터도 함께 삭제된다.
            //ㄴ데이터를 타입에 따라서 관리해준다.


           val passwordPrefercences = getSharedPreferences("password", Context.MODE_PRIVATE) //이 파일을 다른 액티비티에서 사용할 수 있도록 함. getSharedPreferences(String name, int mode)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}" //사용자가 선택한 넘버피커 3개의 값을 가져와서 저장한다.

            if ( passwordPrefercences.getString("password", "000").equals(passwordFromUser)){ //현재 prefercence 파일에 등록되어있는 비밀번호와 사용자가 입력한 비밀번호가 같을 경우,
                //패스워드 성공

                startActivity(Intent(this, DiaryActivity::class.java))


            //startActivity()
            } else {
                //패스워드 실패
                showErrorAlertDialog()

            }

        }

        changePasswordButton.setOnClickListener{ //비밀번호 바꾸기 버튼을 클릭했을 경우
            val passwordPrefercences = getSharedPreferences("password", Context.MODE_PRIVATE) //이 파일을 다른 앱에서 사용할 수 있도록 공유
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}" //패스워드를 띄워쓰기 없이 연속적인 숫자 3자리(넘버피커) 값으로 설정함.


            if (changePsswordMode) {
                //비밀번호를 바꾸는 기능

                passwordPrefercences.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePsswordMode =false //모드 종료
                changePasswordButton.setBackgroundColor(Color.BLACK) //버튼 색 복귀

            }else {
                //비밀번호가 맞는지를 확인한 후, changePasswordMode를 활성화 한다.

                if ( passwordPrefercences.getString("password", "000").equals(passwordFromUser)){ //사용자가 비밀번호를 올바르게 입력했을 경우

                    changePsswordMode = true //비밀번호 변경 모드 true
                    Toast.makeText(this, "변경할 패스워드를 입력해 주세요.", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED) //버튼 색을 다르게 표시해, 변경 모드가 켜졌다는걸 가시화함

                } else {
                    //패스워드 실패 : 비밀번호를 변경할 수 없도록 막음
                   showErrorAlertDialog()
                }

            }

        }

    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("학인"){ _, _ -> }
            .create()
            .show()

    }



}