package SimpleGPX

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

internal val EARTH_RADIUS_KM = 6371;

data class trkseg(var trkpts: ArrayList<TrackPoint>){
    var distance:Double? = null;
    inner class ElevationStruct {
        var totalElevation: Double? = null;
        var elevationGain: Double? = null;
        var elevationLoss: Double? = null;
    }
    var pace:Pace? = null;
}

class TrksegStruct{
    var distance:Double = 0.0;
    inner class ElevationStruct{
        var totalElevation:Double = 0.0;
        var elevationGain:Double = 0.0;
        var elevationLoss:Double = 0.0
    }
    var pace:Pace? = null;
}

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
        trackPoint1.distToNext = sqrt(flat_dist.pow(2) + (elevation_between_points(trackPoint1,
                trackPoint2)).pow(2));
        trackPoint1.distToNext!!;
    }else{
        trackPoint1.distToNext = EARTH_RADIUS_KM * c;
        trackPoint1.distToNext!!;
    }

}

internal fun elevation_between_points(trackPoint1: TrackPoint, trackPoint2: TrackPoint):Double{
    return trackPoint2.elevation - trackPoint1.elevation;
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

    var distance:Double = 0.0;
    if (trkseg.distance == null){
        distance = calculateDistance(trkseg);
    }else{
        distance = trkseg.distance!!;
    }
    var time = GPXParserLocation.time_to_long(trkseg.trkpts.last().time) -
            GPXParserLocation.time_to_long(trkseg.trkpts.first().time);
    time = time / 1000;
    val pace = time / distance;
    return Pace(floor(pace / 60).toInt(),pace % 60);
}

fun calculatePace(trkseg: trkseg, index1:Int, index2:Int, threshold: Int = 1):Pace{

    var index1 = index1 - threshold;
    var index2 = index2 + threshold;
    if (index1 < 0){
        index1 = 0;
    }
    if (index2 >= trkseg.trkpts.size) {
        index2 = trkseg.trkpts.size - 1;
    }

    var totalDistance = 0.0;
    val last = index2 - 1;
    for (i in index1 until last){
        totalDistance += distance_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
    }

    var time = GPXParserLocation.time_to_long(trkseg.trkpts[index1].time) -
            GPXParserLocation.time_to_long(trkseg.trkpts[index2].time);
    time /= 1000;
    val pace = time / totalDistance;
    return Pace(floor(pace / 60).toInt(),pace % 60);

}

fun calculatePace(trkseg: trkseg, index:Int, threshold: Int = 1):Pace{

    var index1 = index - threshold;
    var index2 = index + threshold;
    if (index1 < 0){
        index1 = 0;
    }
    if (index2 >= trkseg.trkpts.size) {
        index2 = trkseg.trkpts.size - 1;
    }

    var totalDistance = 0.0;
    val last = index2 - 1;
    for (i in index1 until last){
        totalDistance += distance_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
    }

    var time = GPXParserLocation.time_to_long(trkseg.trkpts[index1].time) -
            GPXParserLocation.time_to_long(trkseg.trkpts[index2].time);
    time /= 1000;
    val pace = time / totalDistance;
    return Pace(floor(pace / 60).toInt(),pace % 60);

}

fun calculateElevation(trkseg: trkseg):Array<Double>{

    var totalElevation:Double = 0.0;
    var elevationGain:Double = 0.0;
    var elevationLoss:Double = 0.0;
    val last = trkseg.trkpts.size - 1;
    for(i in 0 until last){
        val elevationDiff = elevation_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
        if (elevationDiff < 0){
            elevationLoss += elevationDiff;
        }else{
            elevationGain += elevationDiff;
        }
        totalElevation += elevationDiff;
    }
    return arrayOf(totalElevation, elevationGain, elevationLoss);

}

fun calculateDist_Ele_Pace(trkseg: trkseg):TrksegStruct{

    var totalDistance:Double = 0.0;
    var elevationData = TrksegStruct().ElevationStruct();

    val last = trkseg.trkpts.size - 1;
    for(i in 0 until last){
        totalDistance += distance_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
        val elevationDiff = elevation_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
        if (elevationDiff < 0){
            elevationData.elevationLoss += elevationDiff;
        }else{
            elevationData.elevationGain += elevationDiff;
        }
        elevationData.totalElevation += elevationDiff;
    }
    var time = GPXParserLocation.time_to_long(trkseg.trkpts.last().time) -
            GPXParserLocation.time_to_long(trkseg.trkpts.first().time);
    time = time / 1000;
    val pace = time / totalDistance;
    val trksegStruct = TrksegStruct();
    trksegStruct.pace = Pace(floor(pace / 60).toInt(),pace % 60);
    trksegStruct.distance = totalDistance;
    return trksegStruct;

}

fun calculateDist_Ele_Pace(trkseg: trkseg, index:Int, threshold: Int = 1):TrksegStruct{

    var totalDistance:Double = 0.0;
    val trksegStruct = TrksegStruct();

    var index1 = index - threshold;
    var index2 = index + threshold;
    if (index1 < 0){
        index1 = 0;
    }
    if (index2 >= trkseg.trkpts.size) {
        index2 = trkseg.trkpts.size - 1;
    }

    val last = trkseg.trkpts.size - 1;
    for(i in index1 until index2){
        totalDistance += distance_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
        val elevationDiff = elevation_between_points(trkseg.trkpts[i], trkseg.trkpts[i + 1]);
        if (elevationDiff < 0){
            trksegStruct.ElevationStruct().elevationLoss += elevationDiff;
        }else{
            trksegStruct.ElevationStruct().elevationGain += elevationDiff;
        }
        trksegStruct.ElevationStruct().totalElevation += elevationDiff;
    }
    var time = GPXParserLocation.time_to_long(trkseg.trkpts.last().time) -
            GPXParserLocation.time_to_long(trkseg.trkpts.first().time);
    time /= 1000;
    val pace = time / totalDistance;
    trksegStruct.pace = Pace(floor(pace / 60).toInt(),pace % 60);
    trksegStruct.distance = totalDistance;
    return trksegStruct;

}