package uni.sd.data.ssvoos.reservas;

import uni.sd.ln.server.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaExisteException;
import uni.sd.ln.server.ssvoos.exceptions.ReservaInexistenteException;
import uni.sd.ln.server.ssvoos.exceptions.SemReservaDisponivelException;
import uni.sd.ln.server.ssvoos.exceptions.VooInexistenteException;
import uni.sd.ln.server.ssvoos.reservas.Reserva;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

public interface IReservasDAO {
    int saveReserva(Reserva r) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaExisteException, SemReservaDisponivelException;
    Reserva getReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
    Reserva getReservaPorID(int id) throws SQLException, ReservaInexistenteException, UtilizadorInexistenteException, VooInexistenteException;
    int getIDReserva(String email, String partida, String destino, LocalDateTime data) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
    Map<Integer, Reserva> getTodasReservasUtilizador(String email) throws SQLException, UtilizadorInexistenteException, VooInexistenteException;
    void removeReserva(String email, String partida, String destino, LocalDateTime dataVoo) throws SQLException, UtilizadorInexistenteException, VooInexistenteException, ReservaInexistenteException;
}
