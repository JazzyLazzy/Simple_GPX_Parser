package SimpleGPX;

import java.util.ArrayList;

import static SimpleGPX.StringifyKt.Stringify;

/*Who doesn't love some forced OOP?*/
public class SimpleGPXTest {

    /*Ah yes, I do love me some good ol' boilerplate. Definitely
     a feature and not a fundamental design flaw or anything.
     */
    public static void main(String[] args){
        /*Let's create a track from the White House to the Kremlin.*/

        /*First, initiate a new GPX*/
        GPX gpx = new GPX("SimpleGPX_TEST","1.1");

        /*Create waypoints at the White House and the Kremlin.*/
        GPXParserLocation locWhiteHouse = new GPXParserLocation(38.89723, -77.03623);
        GPXWaypoint wptWhiteHouse = new GPXWaypoint(locWhiteHouse, "White House");
        GPXParserLocation locKremlin = new GPXParserLocation(55.7513066, 37.6159268);
        GPXWaypoint wptKremlin = new GPXWaypoint(locKremlin, "Kremlin");

        /*Create a track from White House to Kremlin
         * A track is made up of track segments, which are made up of track points.
         * It can get a little confusing.*/
        TrackPoint trackPointWhiteHouse = new TrackPoint(locWhiteHouse);
        TrackPoint trackPointKremlin = new TrackPoint(locKremlin);
        ArrayList<TrackPoint> whiteHouseKremlinTrkPoints = new ArrayList<TrackPoint>();
        whiteHouseKremlinTrkPoints.add(trackPointKremlin);
        whiteHouseKremlinTrkPoints.add(trackPointWhiteHouse);
        trkseg whiteHouseKremlinTrkSeg = new trkseg(whiteHouseKremlinTrkPoints);
        ArrayList<trkseg> whiteHouseKremlinTrkSegs = new ArrayList<trkseg>();
        whiteHouseKremlinTrkSegs.add(whiteHouseKremlinTrkSeg);
        trk whiteHouseKremlinTrack = new trk("White House Kremlin Track", whiteHouseKremlinTrkSegs);

        /*Finally, add the waypoints and track to the GPX object*/
        gpx.addWaypoint(wptWhiteHouse);
        gpx.addWaypoint(wptKremlin);
        gpx.addTrack(whiteHouseKremlinTrack);

        /*Now, we can write it to file.*/
        SimpleGPXWriter simpleGPXWriter = new SimpleGPXWriter("./White_House_Kremlin_Track");
        /*Connect our gpx*/
        simpleGPXWriter.connectGPX(gpx);
        simpleGPXWriter.writeGPX();

        /*Now, to read our gpx*/
        SimpleGPXParser simpleGPXParser = new SimpleGPXParser("./White_House_Kremlin_Track");
        GPX whiteHouseKremlinGPX = simpleGPXParser.parseGPX();
        whiteHouseKremlinGPX.getWaypoints();
        whiteHouseKremlinGPX.getTracks();

        /*Turn it into a string*/
        System.out.println(Stringify("./White_House_Kremlin_Track"));
        /*LMAO System.out.println **facepalm** */
    }

}
