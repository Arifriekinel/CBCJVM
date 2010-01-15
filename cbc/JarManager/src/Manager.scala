import scala.swing._
import scala.swing.event._
import java.net.Socket
import javax.swing.JOptionPane
import javax.swing.JFileChooser
import java.io.DataOutputStream
import java.io.DataInputStream
import java.io.FileInputStream

object Manager extends SimpleGUIApplication {
	var connection: Socket = null;
 	val listView = new ListView[String]
 	var connected = false
 	val deleteButton = new Button("Delete") {
		     enabled = false
			 reactions += {
			   case ButtonClicked(_) => {
			     val indices = listView.selection.indices
			     val ostream = new DataOutputStream(connection.getOutputStream)
			     for(a <- indices) {
			       ostream.writeUTF("delete " + listView.listData(a))
			     }
			     listView.listData = jarList
			   }
			 }
		   }
 	val transferButton = new Button("Transfer") {
		     enabled = false
			 reactions += {
			   case ButtonClicked(_) => {
			     val fc = new JFileChooser
			     //In response to a button click:
			     val ret = fc.showOpenDialog(null)
			     if(ret == JFileChooser.APPROVE_OPTION) {
			       val ostream = new DataOutputStream(connection.getOutputStream)
			       val file = fc.getSelectedFile
			       val stream = new FileInputStream(file)
			       var bytes = new Array[Byte](stream.available)
			       stream.read(bytes)
			       ostream.writeUTF("transfer " + file.getName)
			       ostream.write(bytes)
			     }
			   }
			 }
		   }
 	def jarList: List[String] = {
 	  if(connection == null) {
 	    return List[String]()
 	  }
 	  val ostream = new DataOutputStream(connection.getOutputStream)
 	  val istream = new DataInputStream(connection.getInputStream)
 	  ostream.writeUTF("listJars")
 	  Thread.sleep(100)
 	  val list = new ListView[String]
 	  var files = List[String]()
 	  while(istream.available > 0) {
 	     val file = istream.readUTF
 	     files = file :: files
 	  }
 	  files
 	}
	def top = new MainFrame {
	  import BorderPanel.Position._
	  contents = new BorderPanel {
		 add(new GridPanel(10, 1) {
		   contents += new Button("Connect") {
			 reactions += {
			   case ButtonClicked(_) => {
			     if(!connected) {
			    	val input = JOptionPane.showInputDialog("Please input the CBC's IP")
			     	connection = new Socket(input, 8411)
			     	connected = true
			     	deleteButton.enabled = true
			     	transferButton.enabled = true
			     	this.text = "Disconnect"
			     } else {
			    	val ostream = new DataOutputStream(connection.getOutputStream)
			    	ostream.writeUTF("exit")
			    	connection.close
			     	connection = null
			     	connected = false
			     	deleteButton.enabled = false
			     	transferButton.enabled = false
			     	this.text = "Connect"
			     }
			     listView.listData = jarList
			   }
			 }
		   }
		   contents += deleteButton
		   contents += transferButton
		 }, East)
		 add(new ScrollPane(listView), Center)
	  }
	}
}