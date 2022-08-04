package SimpleGPX

import javax.sound.midi.Track

import java.time.LocalDateTime
import kotlin.collections.ArrayList


/*This is an example implementation of Simple GPX Parser.
Here you will learn to create a new GPX, create waypoints
and tracks, write to file, and read from file.
 */

fun main(){

    /*Let's create a track from the White House to the Kremlin.*/

    /*First, initiate a new GPX*/
    val gpx = GPX("SimpleGPX_TEST","1.1");

    /*Create waypoints at the White House and the Kremlin.*/
    val locWhiteHouse = GPXParserLocation(38.89723, -77.03623, 0.0)
    val wptWhiteHouse = GPXWaypoint(locWhiteHouse, "White House")
  //  51.49945266389364, -0.12480920155048388
    val locKremlin = GPXParserLocation(51.49945266389364, -7.12480920155048388, 0.0)
    val wptKremlin = GPXWaypoint(locKremlin, "Kremlin")

    /*Create a track from White House to Kremlin
    * A track is made up of track segments, which are made up of track points.
    * It can get a little confusing.*/
    val trackPointWhiteHouse = TrackPoint(locWhiteHouse)
    val trackPointKremlin = TrackPoint(locKremlin)
    val whiteHouseKremlinTrkPoints = ArrayList<TrackPoint>()
    whiteHouseKremlinTrkPoints.add(trackPointKremlin)
    whiteHouseKremlinTrkPoints.add(trackPointWhiteHouse)
    val whiteHouseKremlinTrkSeg = trkseg(whiteHouseKremlinTrkPoints)
    val whiteHouseKremlinTrkSegs = ArrayList<trkseg>()
    whiteHouseKremlinTrkSegs.add(whiteHouseKremlinTrkSeg)
    val whiteHouseKremlinTrack = trk("White House Kremlin Track", whiteHouseKremlinTrkSegs)

    /*Finally, add the waypoints and track to the GPX object*/
    gpx.addWaypoint(wptWhiteHouse)
    gpx.addWaypoint(wptKremlin)
    gpx.addTrack(whiteHouseKremlinTrack)

    /*Now, we can write it to file.*/
    val simpleGPXWriter = SimpleGPXWriter("./White_House_Kremlin_Track.gpx")
    /*Connect our gpx*/
    simpleGPXWriter.connectGPX(gpx)
    simpleGPXWriter.writeGPX()

    /*Now, to read our gpx*/
    val simpleGPXParser = SimpleGPXParser("./White_House_Kremlin_Track.gpx")
    val whiteHouseKremlinGPX= simpleGPXParser.parseGPX()
    whiteHouseKremlinGPX.waypoints
    whiteHouseKremlinGPX.tracks

    /*Turn it into a string*/
    println(Stringify("./White_House_Kremlin_Track.gpx"))

    println(calculateDistance(whiteHouseKremlinTrkSeg))

    val distparse = SimpleGPXParser("./Serqueaux_Dieppe.gpx");
    var distance = 0.0;
    val distgpx = distparse.parseGPX();
    println(distgpx.tracks.size);
    println(distgpx.tracks[0].trksegs.size);
    for (trk in distgpx.tracks){
       for (trkseg in trk.trksegs){
            distance += calculateDistance(trkseg);
       }
    }
    println(distance);
}