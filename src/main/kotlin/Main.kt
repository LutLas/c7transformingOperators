import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

fun main(args: Array<String>) {
    /*exampleOf("toList") {

        val subscriptions = CompositeDisposable()
        // 1
        val items = Observable.just("A", "B", "C",2)

        subscriptions.add(
                items
                        // 2
                        .toList()
                        .subscribeBy {
                            println(it)
                        }
        )
    }*/

    /*exampleOf("map") {

        val subscriptions = CompositeDisposable()

        subscriptions.add(
                // 1
                Observable.just("M", "C", "V", "I")
                        // 2
                        .map {
                            // 3
                            it.romanNumeralIntValue()
                        }
                        // 4
                        .subscribeBy {
                            println(it)
                        })
    }*/

    /*exampleOf("flatMap") {

        val subscriptions = CompositeDisposable()
        // 1
        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))
        // 2
        val student = PublishSubject.create<Student>()

        student
                // 3
                .flatMap { it.score }
                // 4
                .subscribe { println(it) }
                .addTo(subscriptions)

        student.onNext(ryan)
        ryan.score.onNext(85)
        student.onNext(charlotte)
        ryan.score.onNext(95)
        charlotte.score.onNext(100)
    }*/

    /*exampleOf("switchMap") {

        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))

        val student = PublishSubject.create<Student>()

        student
                .switchMap { it.score }
                .subscribe { println(it) }

        student.onNext(ryan)

        ryan.score.onNext(85)

        student.onNext(charlotte)

        ryan.score.onNext(95)

        charlotte.score.onNext(100)
    }*/

    exampleOf("materialize/dematerialize") {

        val subscriptions = CompositeDisposable()

        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))

        val student = BehaviorSubject.create<Student>()

        // 1
        val studentScore = student
                .switchMap { it.score.materialize() }
// 2
        subscriptions.add(studentScore
                // 1
                .filter {
                    if (it.error != null) {
                        println(it.error)
                        false
                    } else {
                        true
                    }
                }
                // 2
                .dematerialize<Int>()
                .subscribe {
                    println(it)
                }
                .addTo(subscriptions))
// 3
        student.onNext(ryan)
        ryan.score.onNext(85)

        ryan.score.onError(RuntimeException("Error!"))

        ryan.score.onNext(90)
// 4
        student.onNext(charlotte)
    }
}
