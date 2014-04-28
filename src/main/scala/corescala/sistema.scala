import akka.actor.{ActorSystem, Props}

// imports para resposta
import akka.pattern.ask
import akka.util.Timeout
import concurrent._
import concurrent.duration._

object SistemaColetor extends App {
  // para resposta
  import ExecutionContext.Implicits.global
  implicit val timeout = Timeout(3 seconds)

  val system   = ActorSystem("sistema-coletor-system")
  val coletor  = system.actorOf(Props[ColetorParticipantes])

  coletor ! Participante("Felipe", "flpmaziero@outlook.com")
  coletor ! Participante("Maziero", "mazieropereira@igmail.com")
  coletor ! Participante("Vanderlei", "lei@igmail.com")
  coletor ! Participante("Alice", "azrmaziero@gmail.com")

  val future = coletor ? GetParticipantes
  future onSuccess {
    case msg => println("Recebi a lista de participantes: " + msg)
  }
  future onFailure {
    case e: Exception => println("Recebi um erro enquanto esperava uma resposta: " + e)
  }

  //system.shutdown()
}
