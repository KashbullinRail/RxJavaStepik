package com.example.rxjavastepik

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
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
            return@fromCallable
            val time = System.currentTimeMillis()
        }
        callable.subscribe()
        println(callable)
        Thread.sleep(5000)
        callable.subscribe()
        println(callable)
    }

    @Test
    fun createMethod() {

        val source = PublishSubject.create<String>()
        source.subscribe(getObserver())
        source.onNext("one")
        source.onNext("Two")
        source.onNext("Three")

    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {

            override fun onSubscribe(d: Disposable) {
                println("onSubscribe ${d.isDisposed}")
            }

            override fun onNext(t: String) {
                println("onNext $t")
            }

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onComplete() {
                println("onComplete")
            }

        }
    }

    @Test
    fun single() {
        val source = Single.just("MySingle")
        source.subscribe { s -> println("Received $s")
        }
    }

    @Test
    fun disposable() {
        var disposable: Disposable? = null
        val source = Observable.just("disposableMy")
        disposable = source.subscribe {
            s -> println("Received $s")
            //clear resourses
            println(disposable)
            disposable?.dispose()
            println(disposable)
        }

    }

}