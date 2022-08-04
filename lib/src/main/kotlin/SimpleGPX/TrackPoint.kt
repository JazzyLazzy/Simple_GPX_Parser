package SimpleGPX

import java.time.LocalDateTime

open class TrackPoint : GPXParserLocation {

    constructor(latitude: Double, longitude: Double, elevation: Double, time: Long) :
            super(latitude, longitude, elevation, time)

    constructor(latitude: Double, longitude: Double, elevation: Double, time: LocalDateTime) :
            super(latitude, longitude, elevation, time)

    constructor(location: GPXParserLocation) :
            super(location.latitude, location.longitude, location.elevation, location.time)

    constructor(latitude:Double, longitude: Double, elevation: Double) :
            super(latitude, longitude, elevation)

    constructor(latitude:Double, longitude: Double) :
            super(latitude, longitude)

    var eleDistance:Boolean = false;
    var nextPoint:TrackPoint? = null;
    var prevPoint:TrackPoint? = null;
    var distToNext:Double? = null;

}