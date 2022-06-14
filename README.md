# com.goals.coroutinelearn
coroutine learn
简单协程学习

```java
//....
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

```
