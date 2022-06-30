package SimpleGPX

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
    val locWhiteHouse = GPXParserLocation(38.89723, -77.03623)
    val wptWhiteHouse = GPXWaypoint(locWhiteHouse, "White House")
    val locKremlin = GPXParserLocation(55.7513066, 37.6159268)
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
    println(Stringify("./White_House_Kremlin_Track"))

    val bogusGPXParser = SimpleGPXParser("./bogus.gpx");
    val bogusGPX = bogusGPXParser.parseGPX()
    println(Stringify(bogusGPX, "./sugob.gpx" ))


    val testparse = SimpleGPXParser("../sample-gpx/BrittanyJura/large_test.gpx");
    println("start parse:" + LocalDateTime.now().toString());
    val mgpx = testparse.parseGPX();
    /*val diff = LinkedList<Long>();
    for (i in 0..1000) {
        val start = System.currentTimeMillis();
        val mgpx = testparse.parseGPX();
        val end = System.currentTimeMillis();
        diff.add(end - start)
    }
    File("./somefile1.txt").printWriter().use { out ->
        diff.forEach {
            out.println("$it")
        }
    }*/
    /*println("end parse:" + LocalDateTime.now().toString())
    println("start loop:" + LocalDateTime.now().toString());
    val ll = LinkedList<Int>()
    for (i in 0..4000000){
        ll.addLast(i);
    }
    println("start loop 2:" + LocalDateTime.now().toString());
    val al = java.util.ArrayList<Int>()
    for (i in 0..4000000){
        al.add(i);
    }
    println("end loop 2:" + LocalDateTime.now().toString());
    //val diff = end - start
    //println(diff)
    //println("size" + mgpx.tracks[0].trksegs[0].trkpts.size)*/
}