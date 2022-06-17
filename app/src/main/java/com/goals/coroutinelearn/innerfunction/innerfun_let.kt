package com.goals.coroutinelearn.innerfunction

//TODO 51.kotlin语言的let内置函数
//普通方式 对集合第一个元素相加
//let方式 对集合第一个元素相加

//普通方式 对值判断null,并返回
//let方式 对值判断null,并返回
fun main() {
    //普通方式 对集合第一个元素相加
    val list = listOf(5, 4, 8, 3, 7, 2, 1)
    val value1 = list.first()//第一个元素
    val result1 = value1 + value1
    println(result1)

    //let方式
    val result2 = list.let {
        //it == list集合
        //匿名函数的最后一行，作为返回值
        //apply和let的区别
        //apply不会根据匿名函数中的改变，而改变原来的对象值
        //let仅将最后一行，当作需要返回的值，随便你是什么
        it.first() + it.first()
    }
    println(result2)

    println()

    println(getMethod1("Derry"))
}

fun getMethod1(s: String): String {
    return if (s == null) "你传递的是Null，你在搞什么飞机" else "欢迎回来${s}"
}

//简化版本 传参后边加上个问号，表示允许为空
fun getMethod2(value: String?) = if (value == null) "你传递的是Null，你在搞什么飞机" else "欢迎回来${value}"

//let方式 对值判断Null，并返回
fun getMethod3(value: String?): String {
    return value?.let {
        "欢迎回来${it}非常欢迎"
    } ?: "你传递的内容是Null，你在搞什么飞机"
}

//let方式 对值判断Null，并返回 简化版本
fun getMethod4(value: String?) = value?.let { "欢迎回来${it}非常欢迎" } ?: "你传递的内容是Null，你在搞什么飞机"
