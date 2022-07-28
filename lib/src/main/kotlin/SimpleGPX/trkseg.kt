package SimpleGPX

import java.util.*
import kotlin.math.*

internal val EARTH_RADIUS_KM = 6371;

data class trkseg(var trkpts: ArrayList<TrackPoint>);

internal fun degreesToRadians(degrees:Double):Double{
    return degrees * PI / 180;
}

internal fun distance_between_points(trackPoint1: TrackPoint, trackPoint2: TrackPoint):Double{

    var dLat = degreesToRadians(trackPoint2.latitude - trackPoint1.latitude);
    var dLon = degreesToRadians(trackPoint2.longitude - trackPoint1.longitude);

    var a = sin(dLat/2) * sin(dLat/2) +
            sin(dLon/2) * sin(dLon/2) * cos(trackPoint1.latitude) * cos(trackPoint2.latitude);
    var c = 2 * atan2(sqrt(a), sqrt(1-a));

    if (trackPoint1.eleDistance || trackPoint2.eleDistance){
        val flat_dist = EARTH_RADIUS_KM * c;
        return sqrt(flat_dist.pow(2) + (trackPoint2.elevation - trackPoint1.elevation).pow(2));
    }else{
        return EARTH_RADIUS_KM * c;
    }

}

fun calculateDistance(trkseg: trkseg):Double{

    var totalDistance:Double = 0.0;

    for(i in trkseg.trkpts.indices - 1){
        totalDistance += distance_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
    }

    return totalDistance;
}
