package com.goals.coroutinelearn.lateinit

class lateinit_my {
   //lateinit val AAA;//val 无法后边再修改了，还怎么延迟初始化

   //等会儿再来初始化，先定义再说，所以没有初始化
    lateinit var responseResultInfo:String

    //模拟服务器加载
    fun loadRequest(){//延时初始化，属于懒加载，用到再给你加载
        responseResultInfo = "服务器加载成功，恭喜你"
    }

    fun showResponseResult(){
        //由于你没有给他初始化，所以只要用到它，就会崩溃
        //if (responseResultInfo == null) println()
        //println("responseResultInfo:$responseResultInfo")

        if (::responseResultInfo.isInitialized){
            println("responseResultInfo:$responseResultInfo")
        }else{
            println("你都没初始化加载，你是不是忘记加载了")
        }
    }


}

fun main() {
    val p = lateinit_my()

    //使用他之前，加载一下（用到它才加载，就属于，懒加载）
    //代码调用一下加载，by lazy { xxx }相当于之前就写好了，不用再调用一下赋值的代码
    //p.loadRequest()

    //使用他
    p.showResponseResult()
}