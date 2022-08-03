package SimpleGPX

data class Pace(var minutes:Int, var seconds:Double) {
    var speed:Double? = null;
}

fun paceToSpeed(pace:Pace):Pace {
    val decimal_pace = pace.minutes + pace.seconds / 60;
    pace.speed = 60 / decimal_pace;
    return pace;
}
