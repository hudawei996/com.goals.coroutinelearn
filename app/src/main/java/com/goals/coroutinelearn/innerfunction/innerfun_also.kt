package com.goals.coroutinelearn.innerfunction

class innerfun_also {
}

fun main() {
    val str = "skljflakjkfljlskdjflsjdf"

    val r:String = str.also{
        true
        34.5f
        45
        'c'
        "e"
    }

    val r1:Int = 123.also{
        true
        34.5f
        "dkd"
        'c'
        4
    }

    //和apply是一样的，但是里边是it
    //apply 里边是 this
    str.also {
        println("str的原始数据是：$it")
    }.also {
        println("str转小写的效果是：${it.uppercase()}")
    }.also {
        println(it)
    }
    //also不会影响最终的返回数据，始终是原来的数据
}