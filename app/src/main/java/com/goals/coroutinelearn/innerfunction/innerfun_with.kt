package com.goals.coroutinelearn.innerfunction

class innerfun_with {
}

fun main() {
    //with = run
    //和run基本上一样，就是使用的时候有点区别
    val str = "李元霸"

    //具名操作
    with(str, ::println)

    println("具名操作=====")
    //类似一串处理
    val r1: Int = with(str, ::getStringLen)
    val r2 = with(r1, ::getLenInfo)
    val r3 = with(r2, ::getInfoMap)
    with(r3, ::show)

    println("匿名操作=====")
    //匿名操作
    with(with(with(with(str){
        this.length
        length
    }){
        "你的字符串长度是：$this"
    }){
        "[$this]"
    }){
        println(this)
    }
}

fun getStringLen(str: String) = str.length
fun getLenInfo(len: Int) = "你的字符串长度是：$len"
fun getInfoMap(info: String) = "[$info]"
fun show(content: String) = println(content)