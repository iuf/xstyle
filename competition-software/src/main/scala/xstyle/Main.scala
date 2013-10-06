package xstyle.gui

import scala.swing._
import scala.swing.event._

class ActionButton(title:String, action: => Unit) extends Button(title) {
  reactions += {
    case e:ButtonClicked => action
  }
}

object Main extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "XStyle"
    var riders = List(
      Array("10", "Till"),
      Array("11", "Felix")
    )
    /*def ridersTable = new Table(rowData = riders.toArray, columnNames=Seq("ID", "Name"))
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new ActionButton("Add Rider", riders = (riders.toList ::: List("13","gossi")).toArray) {

      }
      contents += ridersTable
    }*/
  }
}
