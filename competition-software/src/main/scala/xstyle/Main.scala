package xstyle.gui

//import scala.swing._
//import scala.swing.event._

import xstyle.libxstyle._
import Helpers._
import xstyle.simulation._

//class ActionButton(title:String, action: => Unit) extends Button(title) {
  //reactions += {
    //case e:ButtonClicked => action
  //}
//}

object Main extends App {//SimpleSwingApplication {
  val riders = readRiders("riders")
  val round = Round(riders)
  for( group <- round.randomStartingGroups ) {
    println(s"Starting group ${group.id+1} (judged by groups ${round.judgingAssignment(group.id).map(_+1).mkString(",")}):")
    println(group.riders.map(_.name).mkString("  ", "\n  ", ""))
    println()
  }

  println(s"judging:\n${round.judgingAssignment.zipWithIndex.map{case (judges,group) => s"  Group ${group+1} judged by groups ${judges.map(_+1).mkString(",")}"}.mkString("\n")}\n")




  val s = new Simulation(riders.size, parallel = false)
  println(s.toString)


  //[>def top = new MainFrame {
    //title = "XStyle"
    //var riders = List(
      //Array("10", "Till"),
      //Array("11", "Felix")
    //)
    //def ridersTable = new Table(rowData = riders.toArray, columnNames=Seq("ID", "Name"))
    //contents = new BoxPanel(Orientation.Vertical) {
      //contents += new ActionButton("Add Rider", riders = (riders.toList ::: List("13","gossi")).toArray) {

      //}
      //contents += ridersTable
    //}
  //}*/
}
