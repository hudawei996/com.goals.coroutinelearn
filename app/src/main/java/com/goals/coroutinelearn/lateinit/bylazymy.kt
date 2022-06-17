package com.goals.coroutinelearn.lateinit

class bylazymy {

    //下边是不使用惰性初始化 by lazy 普通方式
    // （饿汉式，这个类一开始就会去初始化对象，没有起到懒加载的效果）
//    val databaseData1 = readSQLServerDatabaseAction()

    val databaseData2 by lazy { readSQLServerDatabaseAction() }

    private fun readSQLServerDatabaseAction(): String {
        println("开始读取数据库。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("加载读取数据库中。。。。")
        println("结束读取数据库。。。。")
        return "database data load success ok."
    }
}

fun main() {
    //不使用惰性加载初始化 by lazy 普通方式（饿汉式）
    val p = bylazymy()

    println("对象创建完成")

    for (i in 1..5){
        Thread.sleep(1000)
        println("休眠时间$i")
    }

    println("即将开始使用。。。")


    //表现差异：对象被创建之后就会被赋值，然后在调用
//    println("最终显示：${p.databaseData1}")

    //表现差异：对象被创建之后，不会被立马赋值，等到调用之后才赋值
    println("最终显示：${p.databaseData2}")
}