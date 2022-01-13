package uni.sd.ln.ssvoos;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uni.sd.data.ssvoos.reservas.ReservasDAO;
import uni.sd.ln.ssutilizadores.exceptions.UtilizadorInexistenteException;
import uni.sd.ln.ssvoos.exceptions.*;
import uni.sd.ln.ssvoos.reservas.Reserva;
import uni.sd.ln.ssvoos.voos.Voo;
import uni.sd.data.ssvoos.voos.VoosDAO;


import static java.lang.Integer.parseInt;

public class SSVooFacade implements ISSVoo {
    private VoosDAO vDAO;
    private ReservasDAO reservasDAO;
    private boolean diaAberto = true;
    private String email;

    public SSVooFacade(String email) throws SQLException {
        vDAO = new VoosDAO();
        reservasDAO = new ReservasDAO();
        this.email = email;
    }

    /**
     * Reserva um voo de acordo com um id e uma data.
     *
     * @param partida origem do voo
     * @param destino destino do voo
     * @param data Data do voo a reservar
     */
    @Override
    public int reservarVoo(String partida, String destino, LocalDate data) throws VooInexistenteException,
            SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        Voo v = vDAO.getVoo(partida, destino);
        LocalDate dataNow = LocalDate.now();
        Reserva r = new Reserva(email, partida, destino, data, dataNow);
        reservasDAO.saveReserva(r);

        return reservasDAO.getIDReserva(email, partida, destino, data);
    }

    /**
     * Cancela um voo já reservado.
     * 
     * @param idReserva ID da reserva
     */
    @Override
    public void cancelarVoo(int idReserva) throws ReservaInexistenteException, VooInexistenteException, SQLException, UtilizadorInexistenteException {
        Reserva r = reservasDAO.getReservaPorID(idReserva);

        String email = r.getEmailUtilizador();
        String partida = r.getPartida();
        String destino = r.getDestino();
        LocalDate data = r.getDataReserva();

        reservasDAO.removeReserva(email, partida, destino, data);
    }

    /**
     * Insere um novo voo na lista de voos fazendo todas as verificações necessárias. 
     * Para executar este método é necessário um nível de autoridade igual ou superior a 1 (Administrador).
     * 
     * @param partida Local de partida
     * @param destino Local de chegada
     * @param capacidade Capacidade do voo
     * 
     * TODO: Adicionar uma verificação para o nível de permissões do utilizador.
     */
    @Override
    public void addInfo(String partida, String destino, int capacidade)
            throws VooExisteException, CapacidadeInvalidaException, PartidaDestinoIguaisException, SQLException {
        if(capacidade <= 0) {
            throw new CapacidadeInvalidaException("Capacidade: " + capacidade);
        }
        if(partida.equals(destino)) {
            throw new PartidaDestinoIguaisException("Partida: " + partida + " | Destino: " + destino);
        }

        Voo novoVoo = new Voo(partida,destino,capacidade);
        vDAO.saveVoo(novoVoo);
    }

    /**
     * Encerra o dia.
     * Se o dia já estiver encerrado atira um DiaJaEncerradoException.
     */
    @Override
    public void encerrarDia() throws DiaJaEncerradoException {
        if(!diaAberto) {
            throw new DiaJaEncerradoException();
        }
        diaAberto = false;
    }

    /**
     * Abre o dia.
     * Se o dia já estiver aberto atira um DiaJaAbertoException.
     */
    @Override
    public void abrirDia() throws DiaJaAbertoException {
        if(diaAberto) {
            throw new DiaJaAbertoException();
        }
        diaAberto = true;
    }

    /**
     * Reserva uma série de voos de acordo com uma percurso,
     * uma data de início e fim.
     * Ou pelo menos foi o que percebi do exercício.
     * 
     * @param voos Lista de voos
     * @param dataInicio Limite inferior da data de reserva
     * @param dataFim Limite superior da data de reserva
     */
    @Override
    public List<Integer> reservarVooPorPercurso(List<String> voos, LocalDate dataInicio, LocalDate dataFim)
            throws VooInexistenteException, SQLException, UtilizadorInexistenteException, ReservaExisteException, ReservaInexistenteException {
        // TODO EU ESTOU A PÔR A FAZER NOVA RESERVA COM INTERVALOS DE 1 DIA MAS NÃO ACHO BEM POR CAUSA DA DATA DE FIM
        List<Integer> res = new ArrayList<Integer>();
        int hours = 0, i;
        for(i = 0; i < voos.size(); i++){
            int idReserva = reservarVoo(voos.get(i), voos.get(i+1), dataInicio.plusDays(1));
            res.add(idReserva);
            hours += 12;
        }
        return res;
    }

    /**
     * Retorna a lista de todos os voos registados no sistema.
     * 
     * @return Lista com todos os voos do sistema.
     */
    @Override
    public List<Voo> obterListaVoo() throws SQLException {
        return vDAO.getTodosVoos();
    }

    /**
     * Retorna uma lista com todos os percursos possíveis
     * que começem em partida e acabem em destino
     * com um máximo de 3 saltos.
     * 
     * @param partida Local de partida
     * @param destino Local de destino
     * @return Lista com todos os caminhos possíveis começados em partida
     *         e acabados em destino com um máximo de 3 saltos
     */
    @Override
    public List<Integer> obterPercursosPossiveis(String partida, String destino) {
        // TODO ACABAR MÉTODO
        List<Integer> res = new ArrayList<>();


        return res;
    }
    
}
