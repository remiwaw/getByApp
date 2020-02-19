
import android.text.format.DateUtils
import com.rwawrzyniak.getby.date.DayHeaderDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val STANDARD_DATE_FORMAT = "dd-MM-yyyy"

//TODO change it to simpleDataFormat
val Calendar.toDayHeaderDto: DayHeaderDto
    get() = DayHeaderDto(
        DateUtils.getDayOfWeekString(
            get(Calendar.DAY_OF_WEEK),
            DateUtils.LENGTH_SHORTER
        ),
        get(Calendar.DAY_OF_MONTH)
    )

fun Calendar.addDays(days: Int) : Calendar = (clone() as Calendar)
    .apply { add(Calendar.DAY_OF_MONTH, days) }

val String.asCalenderddMMyyy: Calendar
    get() {
        val stringDate = this
        return Calendar.getInstance().apply {
            val sdf = SimpleDateFormat(STANDARD_DATE_FORMAT, Locale.ENGLISH)
            val date: Date = sdf.parse(stringDate)
            time = date
        }
    }

