package xadrez;

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