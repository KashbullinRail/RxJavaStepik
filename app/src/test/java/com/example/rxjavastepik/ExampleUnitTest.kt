package com.example.rxjavastepik

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Test
import java.util.concurrent.Callable

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun observable() {

        val months = Observable.just(
            "jan", "feb", "mar", "apr",
            "may", "jun", "jul", "aug"
        )

        months.subscribe(object : Observer<String> {

            override fun onSubscribe(d: Disposable) {
                println("onSubscribe;" + Thread.currentThread().name + "\n")
            }

            override fun onNext(t: String) {
                println("onNext $t")
            }

            override fun onError(e: Throwable) {
                println("onError $e")
            }

            override fun onComplete() {
                println("\n onComplete")
            }

        })

    }

    @Test
    fun fromIterable() {

        val list = listOf(
            "jan", "feb", "mar", "apr",
            "may", "jun", "jul", "aug"
        )

        val months = Observable.fromIterable(list)

        months.subscribe(object : Observer<String> {

            override fun onSubscribe(d: Disposable) {
                println("onSubscribe;" + Thread.currentThread().name + "\n")
            }

            override fun onNext(t: String) {
                println("onNext $t")
            }

            override fun onError(e: Throwable) {
                println("onError $e")
            }

            override fun onComplete() {
                println("\n onComplete")
            }

        })

    }


    @Test
    fun fromArray() {

        val list = listOf(
            "jan", "feb", "mar", "apr",
            "may", "jun", "jul", "aug"
        )

        val months = Observable.fromArray(list)

        months.subscribe(object : Observer<List<String>> {

            override fun onSubscribe(d: Disposable) {
                println("onSubscribe;" + Thread.currentThread().name + "\n")
            }

            override fun onNext(t: List<String>) {
                println("onNext $t")
            }

            override fun onError(e: Throwable) {
                println("onError $e")
            }

            override fun onComplete() {
                println("\n onComplete")
            }

        })

    }

    @Test
    fun fromCallable() {
       val callable = Observable.fromCallable {
           return@fromCallable System.currentTimeMillis().toString()
       }
        callable.subscribe()
        println(callable)
    }



}