package com.goals.coroutinelearn.innerfunction

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.nio.file.Paths

//TODO 50.kotlin语言的apply内置函数
@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val info = "Goals Ni Hao"

    //普通方式
    println("info字符串的长度是：${info.length}")
    println("info最后一个字符是：${info[info.length - 1]}")
    println("info全部转小写是：${info.lowercase()}")

    println()

    //apply内置函数的方式
    //apply特点：apply始终返回info本身
    val infoNew: String = info.apply {
        //一般大部分情况下，匿名函数，都会有一个it,
        //但是apply函数不会有it,却会持有当前this == info本身
        println("apply匿名函数里打印的：$this")

        println("info字符串的长度是：${this.length}")
        println("info最后一个字符是：${this[this.length - 1]}")
        println("info全部转成小写是：${this.lowercase()}")

        println()

        //this可以省略，可写成一下形式
        println("info字符串的长度是：${length}")
        println("info最后一个字符是：${this[length - 1]}")
        println("info全部转成小写是：${lowercase()}")

        println()

        //真正使用apply函数的写法规则如下：
        //info.apply特点：apply函数始终是返回 "info本身"
        //所以可以链式调用
        info.apply {
            println("长度是：${length}")
        }.apply {
            println("最后一个字符是：${this[length - 1]}")
        }.apply {
            println("全部转成小写是：${lowercase()}")
        }


        println()

        //打印出当前目录
        //工作目录 = /Volumes/SSD1T/xiangxue-run-project/CoroutineLearn
        val path = Paths.get("")
            .toAbsolutePath().toString()
        println("工作目录 = $path")

        //普通写法
//        val file = File("../a.txt")
        val file = File("a.txt")
        println(file.absolutePath)
        file.setExecutable(true)
        file.setReadable(true)
        println(file.readLines())

        //apply写法
        //匿名函数里面 持有的this == file本身
        val fileName :File = file.apply {
            setExecutable(true)
        }.apply {
            setReadable(true)
        }.apply {
            println(file.readLines())
        }

    }

    println("apply 返回的值：$infoNew")
}