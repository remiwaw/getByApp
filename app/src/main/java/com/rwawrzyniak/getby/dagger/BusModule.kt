import com.rwawrzyniak.getby.core.GlobalEvent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Module
object BusModule {
    const val GLOBAL_EVENT_SUBJECT = "GLOBAL_EVENT_SUBJECT"

    @Provides
    @Singleton
    @Named(GLOBAL_EVENT_SUBJECT)
    fun provideTopSubject(): PublishSubject<GlobalEvent> {
        return PublishSubject.create()
    }
}