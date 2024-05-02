package xadrez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

    private int turno;
    private Color jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean check;

    private List<Peca> pecasDoTabuleiro = new ArrayList<>();
    private List<Peca> pecasCapturadas = new ArrayList<>();

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Color.WHITE;
        check = false;
        iniciarPartida();
    }

    public int getTurno() {
        return turno;
    }


    public Color getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean getCheck() {
        return check;
    }

    public PecaXadrez[][] getPecas(){
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for (int i=0; i<tabuleiro.getLinhas(); i++) {
            for (int j=0; j<tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
            }
        }
        return mat;
    }

    public boolean[][] movimentosPossiveis(XadrezPosicao origemPosicao){
        Posicao posicao = origemPosicao.toPosicao();
        validarPosicaoDeOrigem(posicao);
        return tabuleiro.peca(posicao).movimentosPossiveis();
    }

    public PecaXadrez MovimentoXadrez(XadrezPosicao origemPosicao, XadrezPosicao destinoPosicao ){
        Posicao origem = origemPosicao.toPosicao();
        Posicao destino = destinoPosicao.toPosicao();
        validarPosicaoDeOrigem(origem);
        validarPosicaoDeDestino(origem, destino);
        Peca capturaPeca = mover(origem, destino);
        if (testCheck(jogadorAtual)) {
            desfazerMovimento(origem, destino, capturaPeca);
            throw new XadrezException("Voce nao pode se colocar em check");
        }

        check = (testCheck(oponente(jogadorAtual))) ? true : false;

        proximoTurno();
        return (PecaXadrez)capturaPeca;
    }

    private Peca mover(Posicao origem, Posicao destino){
        Peca p = tabuleiro.removaPeca(origem);
        Peca capturaPeca = tabuleiro.removaPeca(destino);
        tabuleiro.colocarPeca(p, destino);

        if (pecasCapturadas != null) {
            pecasDoTabuleiro.remove(capturaPeca);
            pecasCapturadas.add(capturaPeca);
        }
        return capturaPeca;
    }

    private void desfazerMovimento(Posicao origem, Posicao destino, Peca capturaPeca) {
        Peca p = tabuleiro.removaPeca(destino);
        tabuleiro.colocarPeca(p, origem);
        if (capturaPeca != null) {
            tabuleiro.colocarPeca(capturaPeca, destino);
            pecasCapturadas.remove(capturaPeca);
            pecasDoTabuleiro.add(capturaPeca);
        }
    }

    private void validarPosicaoDeOrigem(Posicao posicao) {
        if (!tabuleiro.existePeca(posicao)){
            throw new XadrezException("Não existe peca na posicao de origem");
        }
        if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()){
            throw new XadrezException("A peca escolhida não é sua");
        } 
        if (!tabuleiro.peca(posicao).existeMovimentosPossiveis()){
            throw new XadrezException("Não existe movimentos possíveis para a peca escolhida");
        }
    }

    private void validarPosicaoDeDestino(Posicao origem, Posicao destino) {
        if (!tabuleiro.peca(origem).movimentoPossivel(destino)){
            throw new XadrezException("A peça escolhida não pode se mover para posição de destino");
        }
    }

    private void proximoTurno() {
        turno++;
        jogadorAtual = (jogadorAtual == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color oponente(Color cor) {
        return (cor == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private PecaXadrez Rei(Color cor) {
        List<Peca> list = pecasDoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
        for (Peca p : list) {
            if (p instanceof Rei) {
                return (PecaXadrez)p;
            }
        }
        throw new IllegalStateException("Não existe rei da cor " + cor + " no tabuleiro");
    }

    private boolean testCheck(Color cor){
        Posicao posicaoRei = Rei(cor).getXadrezPosicao().toPosicao();
        List<Peca> pecasOponente = pecasDoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
        for (Peca p : pecasOponente) {
            boolean[][] mat = p.movimentosPossiveis();
            if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]){
                return true;
            }
        }
        return false;
    }

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.colocarPeca(peca, new XadrezPosicao(coluna, linha).toPosicao());
        pecasDoTabuleiro.add(peca);
    }

    public void iniciarPartida() {
        colocarNovaPeca('c', 1, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('c', 2, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('d', 2, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('e', 2, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('e', 1, new Torre(tabuleiro, Color.WHITE));
        colocarNovaPeca('d', 1, new Rei(tabuleiro, Color.WHITE));

        colocarNovaPeca('c', 7, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('c', 8, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('d', 7, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('e', 7, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('e', 8, new Torre(tabuleiro, Color.BLACK));
        colocarNovaPeca('d', 8, new Rei(tabuleiro, Color.BLACK));
    }

    

}
