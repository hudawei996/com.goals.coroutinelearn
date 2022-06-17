package com.goals.coroutinelearn.innerfunction

class innerfun_run {
}

fun main() {
    val str = "goals is ok"
    val r1: Float = str.run {
        this.length
        true
        543.4f
    }
    println(r1)

    //具名函数 配合 run函数
    str.run {

    }

    val r2: Boolean = str
        .run(::isLong)//最后一行决定返回类型
        .run {
            this
        }


    val r3: String = str
        .run(::isLong)
        .run(::showText)//最后一行决定返回类型
        .run {
            this
        }

    str
        .run(::isLong)
        .run(::showText)//最后一行决定返回类型
        .run(::println)
        .run {
            this
        }

    println("let实现多层调用=====")

    //let实现多层
    str.let(::isLong)
        .let(::showText)
        .let(::println)
        .let{
            println(it)
        }

    println()

    //上面全部使用具名函数调用给run执行 下边全是匿名函数调用run
    //这种一串处理下来，真的很像RxJava了
    str.run {
        //依据判断，将str转换成了boolean
        length > 5
    }.run {
        //根据上一个结果，将结果转换成字符串
        if (this) "你的字符串合格" else "你的字符串不合格"
    }.run {
        //给处理后的字符串加一个中括号
        "[${this}]"
    }.run(::println)

}

fun isLong(str: String): Boolean = (str.length > 5)

fun showText(isLong: Boolean): String = if (isLong) "你的字符串合格" else "你的字符串不合格"