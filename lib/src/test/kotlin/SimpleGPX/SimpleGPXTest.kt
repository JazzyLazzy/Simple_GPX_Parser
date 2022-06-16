package SimpleGPX

class SampleGPXTest {
}

fun main(){
    val bogusGPX = SimpleGPXParser("./bogus.gpx");
    bogusGPX.parseGPX()
}