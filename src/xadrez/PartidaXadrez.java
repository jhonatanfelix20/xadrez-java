package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

    private Tabuleiro tabuleiro;

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8, 8);
        iniciarPartida();
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
        return (PecaXadrez)capturaPeca;
    }

    private Peca mover(Posicao origem, Posicao destino){
        Peca p = tabuleiro.removaPeca(origem);
        Peca capturaPeca = tabuleiro.removaPeca(destino);
        tabuleiro.colocarPeca(p, destino);
        return capturaPeca;
    
    }

    private void validarPosicaoDeOrigem(Posicao posicao) {
        if (!tabuleiro.existePeca(posicao)){
            throw new XadrezException("Não existe peca na posicao de origem");
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

    private void colocarNovaPeca(char coluna, int linha, PecaXadrez peca) {
        tabuleiro.colocarPeca(peca, new XadrezPosicao(coluna, linha).toPosicao());
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
