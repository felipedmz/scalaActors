import akka.actor._
import collection.mutable.ListBuffer
import concurrent.duration._
import SupervisorStrategy._
import java.util.concurrent._
import java.net._
import akka.routing._

class ColetorParticipantes extends Actor {
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _: TimeoutException => Resume
    case _: UnknownHostException => Restart
  }

  //val notificacao = context.actorOf(Props[EnviadorEmails])
  val roteadorEmail = context.actorOf(Props[EnviadorEmails].withRouter(RoundRobinRouter (nrOfInstances = 5)))

  val listaParticipantes = new ListBuffer[String]()

  def receive = {
    case Participante(nome, email) => 
      listaParticipantes += nome
      roteadorEmail ! EnviarEmail(email, "Parabéns " + nome + " você entrou na minha lista!")
    case GetParticipantes => 
      sender ! listaParticipantes.toList
  }
}

case class Participante(nome: String, email: String)
case object GetParticipantes

class EnviadorEmails extends Actor {
  def receive = {
    case EnviarEmail(email, corpo) => 
      println("email: " + email)
      println("corpo: " + corpo)
  }
}

case class EnviarEmail(email: String, corpo: String)
