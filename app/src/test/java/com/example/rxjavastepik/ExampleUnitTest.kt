package com.example.rxjavastepik

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

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
        source.subscribe { s ->
            println("Received $s")
        }
    }

    @Test
    fun disposable() {
        var disposable: Disposable? = null
        val source = Observable.just("disposableMy")
        disposable = source.subscribe { s ->
            println("Received $s")
            //clear resourses
            println(disposable)
            disposable?.dispose()
            println(disposable)
        }
    }


    @Test
    fun compositeDisposable() {
        val compositeDisposable = CompositeDisposable()
        var disposable: Disposable? = null
        val source = Observable.just("disposableMy")
        compositeDisposable.addAll((source.subscribe { s -> println("Received1 $s") }))
        compositeDisposable.addAll((source.subscribe { s -> println("Received2 $s") }))
        println(compositeDisposable)
        compositeDisposable.clear()
        println(compositeDisposable)
    }

    @Test
    fun take() {
        val source = Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        source.take(3).subscribe { s -> println("take $s") }
    }

    @Test
    fun takeWhile() {
        val source = Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        source.takeWhile { x -> x < 6 }.subscribe { s -> println("take $s") }
    }

    @Test
    fun distinct() {
        val source = Observable.fromArray(1, 2, 3, 4, 5, 6, 3, 8, 9, 5, 11, 12)
        source.distinct().subscribe { s -> println("take $s") }
    }

    @Test
    fun elementAt() {
        val source = Observable.fromArray(1, 2, 3, 4, 5, 6, 3, 8, 9, 5, 11, 12)
        source.elementAt(0).subscribe { s -> println("take $s") }
    }


    //*****************
    data class BookOld(
        val list: String,
        val size: Int
    )

    data class BookNew(
        val list: String,
        val size: Int
    )

    object BookConverter {
        fun toNewObject(old: BookOld): BookNew {
            return BookNew(
                list = old.list,
                size = old.size / 4
            )
        }
    }

    @Test
    fun map() {
        val book = BookOld(list = "trtr3t", size = 450)
        val source = Observable.just(book)
        source
            .map { old -> BookConverter.toNewObject(old) }
            .subscribe { s -> println(s) }
    }
    //*****************

    @Test
    fun flatMap() {
        val initialSource: Observable<String> = Observable.just("day")

        initialSource.flatMap {
            if (it == "day") {
                return@flatMap Observable.just("Mon", "Tue", "The")
            } else {
                return@flatMap Observable.just("wen", "sat", "fry")
            }
        }.subscribe { s -> println(s) }

    }

    @Test
    fun merge() {
        //излучает каждые 2 секунды, берем первые три
        val source1 = Observable.interval(1, TimeUnit.SECONDS)
            .take(6)
            .map { l -> l + 1 }
            .map { l -> "Source1 $l second" }

        //излучает каждые 400мс
        val source2 = Observable.interval(400, TimeUnit.MILLISECONDS)
            .map { l -> (l+1)*400 }
            .map { l -> "Source2 $l millisecond" }

        Observable.merge(source1, source2)
            .subscribe{i -> println("print $i")}
        Thread.sleep(10000)
    }

    @Test
    fun concat() {
        //излучает каждые 2 секунды, берем первые три
        val source1 = Observable.interval(1, TimeUnit.SECONDS)
            .take(6)
            .map { l -> l + 1 }
            .map { l -> "Source1 $l second" }

        //излучает каждые 400мс
        val source2 = Observable.interval(400, TimeUnit.MILLISECONDS)
            .map { l -> (l+1)*400 }
            .map { l -> "Source2 $l millisecond" }

        Observable.concat(source1, source2)
            .subscribe{i -> println("print $i")}
        Thread.sleep(10000)
    }


}