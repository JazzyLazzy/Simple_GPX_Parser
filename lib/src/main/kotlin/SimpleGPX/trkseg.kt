package SimpleGPX

import java.util.*
import kotlin.math.*

internal val EARTH_RADIUS_KM = 6371;

data class trkseg(var trkpts: ArrayList<TrackPoint>)

internal fun degreesToRadians(degrees:Double):Double{
    return degrees * PI / 180;
}

internal fun distance_between_points(trackPoint1: TrackPoint, trackPoint2: TrackPoint):Double{

    val dLat = degreesToRadians(trackPoint2.latitude - trackPoint1.latitude);
    val dLon = degreesToRadians(trackPoint2.longitude - trackPoint1.longitude);

    val lat1 = degreesToRadians(trackPoint1.latitude);
    val lat2 = degreesToRadians(trackPoint2.latitude);

    trackPoint1.nextPoint = trackPoint2;
    trackPoint2.prevPoint = trackPoint1;

    val a = sin(dLat/2) * sin(dLat/2) +
            sin(dLon/2) * sin(dLon/2) * cos(lat1) * cos(lat2);
    val c = 2 * atan2(sqrt(a), sqrt(1-a));

    return if (trackPoint1.eleDistance || trackPoint2.eleDistance){
        val flat_dist = EARTH_RADIUS_KM * c;
        trackPoint1.distToNext = sqrt(flat_dist.pow(2) + (trackPoint2.elevation - trackPoint1.elevation).pow(2));
        trackPoint1.distToNext!!;
    }else{
        trackPoint1.distToNext = EARTH_RADIUS_KM * c;
        trackPoint1.distToNext!!;
    }

}

fun calculateDistance(trkseg: trkseg):Double{

    var totalDistance:Double = 0.0;

    val last = trkseg.trkpts.size - 1;
    for(i in 0 until last){
        totalDistance += distance_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
    }

    return totalDistance;
}

fun calculatePace(trkseg: trkseg):Pace{

    val distance = calculateDistance(trkseg);
    val time = GPXParserLocation.time_to_long(trkseg.trkpts.last().time) -
            GPXParserLocation.time_to_long(trkseg.trkpts.first().time);
    val pace = time / distance;
    return Pace(floor(pace / 60000).toInt(),pace % 60000);

}

fun calculateElevation(trkseg: trkseg){}