package com.goals.coroutinelearn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.app.NotificationManagerCompat
import com.sakuqi.httplibrary.HttpAgentClient
import com.sakuqi.httplibrary.HttpRequest
import com.sakuqi.httplibrary.data.HttpMethod
import com.sakuqi.httplibrary.data.ResponseData
import com.sakuqi.httplibrary.engine.OkHttpEngine
import com.sakuqi.httplibrary.request.executeSync
import kotlinx.coroutines.*
import com.goals.coroutinelearn.databinding.ActMainBinding
//import com.goals.coroutinelearn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //TODO ActivityMainBinding这个类名是根据 activity_main.xml 来生成的，不是根据你的Activity的名称来的
    //TODO 如果xml布局改名叫 act_main.xml就会生成一个 ActMainBinding.class的类，来管理这个布局文件中的所有控件
   private lateinit var binding : ActMainBinding

   private lateinit var scope: CoroutineScope

   lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        initViewBinding()

        coroutine1()

        coroutine2()

        createNotificationChannel()
    }

    /**
     * 这是单纯的显示一个通知，在activity中，而不是在service中启动前台service（这种通知不会在点击后消失
     * 只会在结束这个前台服务才能消失）
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val CHANNEL_ID = "CHANNEL_ID"
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, LandingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this, 0, intent, 0)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(123, builder.build())
            }

            /*val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
                action = ACTION_SNOOZE
                putExtra(EXTRA_NOTIFICATION_ID, 0)
            }
            val snoozePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_snooze, getString(R.string.snooze), snoozePendingIntent)*/

        }
    }

    /**
     * 非全局协程
     */
    private fun coroutine2() {
        HttpAgentClient.newBuilder()
            .setAgent(OkHttpEngine::class.java)
            .setConnectTimeOut(10000)
            .setReadTimeOut(10000)
            .build()


        scope = CoroutineScope(Dispatchers.Main)
        //        scope = CoroutineScope(Job() +Dispatchers.Main)


        //获取网络数据，更新UI
        //1，scope控制协程生命周期
        asyncCoroutine()


        //2，job控制生命周期
        job = Job()
        asyncGlobalScope()
    }

    /**
     * 全局协程
     */
    private fun coroutine1() {
        GlobalScope.launch(Dispatchers.Main) {
            for (i in 20 downTo 1) {
                binding.textView.text = "count down $i ..." // update text
                delay(1000)
            }
            binding.textView.text = "Done!"
        }
    }

    private fun initViewBinding() {
        binding = ActMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun asyncCoroutine() {
        scope.launch {
            val baidu = async(Dispatchers.IO) {
                val result = HttpRequest.newBuilder()
                    .setUrl("http://www.baidu.com")
                    .setMethod(HttpMethod.GET)
                    .build()
                    .executeSync<ResponseData>()
                Log.d("TAG", result.toString())
                //最后一个为返回值
                result.data
            }
        }
    }

    // ---------------------GlobalScope.async-----------------------
    /**
     * GlobalScope.async 使用：
     * async 方法会启动一个新的协程，并使用一个名为 await 的关键词，等待耗时方法执行结束的返回结果。
     */
    private fun asyncGlobalScope() {
        // GlobalScope.async 使用
        job = GlobalScope.launch(Dispatchers.Main) {
            // TODO 执行主线程任务                 // main thread
            // 第一个异步网络请求
            val taobaoData = async(Dispatchers.IO) { // IO thread
                // TODO IO线程 网络请求
                // 返回值为String的Http同步网络请求
                /*HttpAgent.get_Sync(
                    "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=18701636688", null, null,
                    String::class.java
                )*/
                val result = HttpRequest.newBuilder()
                    .setUrl("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=18701636688")
                    .setMethod(HttpMethod.GET)
                    .build()
                    .executeSync<ResponseData>()
                Log.d("TAG_taobao", result.toString())
                //返回数据
                result.data
            }
            // 第二个异步网络请求
            val baiduData = async(Dispatchers.IO) { // IO thread
                // TODO IO线程 网络请求
                // 返回值为String的Http同步网络请求
                /*HttpAgent.get_Sync(
                    "https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=18701636688", null, null,
                    String::class.java
                )*/

               val result = HttpRequest.newBuilder()
                    .setUrl("https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=18701636688")
                    .setMethod(HttpMethod.GET)
                    .build()
                    .executeSync<ResponseData>()
                Log.d("TAG_baidu", result.toString())
                //返回数据
                result.data
            }
            // 待两个结果都返回后
            val resultData: String = (taobaoData.await() + baiduData.await())

            // 展示UI，这里是在UI线程中执行的
            binding.textView1.text = resultData           // main thread
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //当Activity销毁时取消scope管理的所有协程
        scope.cancel()
        job.cancel()
    }
}