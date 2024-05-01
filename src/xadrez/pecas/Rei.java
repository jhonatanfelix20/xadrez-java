package xadrez.pecas;

import xadrez.PecaXadrez;
import xadrez.Color;
import tabuleiro.Tabuleiro;

public class Rei extends PecaXadrez{

    public Rei(Tabuleiro tabuleiro, Color cor) {
        super(tabuleiro, cor);
    }

    @Override
    public String toString(){
        return "R";
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        return mat;
    }

}
